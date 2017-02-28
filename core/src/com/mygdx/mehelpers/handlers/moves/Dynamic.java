/* 
 * Copyright 2017 
 * - Hugo Da Roit - Benjamin Lévêque
 * - Alexis Montagne - Alexis Clément
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygdx.mehelpers.handlers.moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Miner;
import com.mygdx.gameobjects.Miner.State;
import static com.mygdx.mehelpers.AssetLoader.run_sound;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Handle the miner movement dynamically
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Dynamic extends Move {
    
    /**
     * @param positionMiner position of the miner
     * @param mapHandler the map handler
     */
    public Dynamic(MapHandler mapHandler, Vector2 positionMiner) {
        super(mapHandler, positionMiner);
    }
    
    /**
     * Called every frame.
     * Make the miner move smoothly
     */
    @Override
    public void move() {
        System.out.print("Ici ");
        boolean launchDestruction = false;
        int x = (int) (positionMineur.x + Miner.WIDTH/2);
        int y = (int) positionMineur.y;
        switch(Miner.direction) {
            case TOP:
                if(!mapHandler.isCellSurfaceHere(x, y+1)) { // Si pas de bloc en x et y + 1
                    if(Miner.isOnLadder) {
                        velocity.y = LADDER_VELOCITY;
                        Miner.minerOnTheGround = false;
                        Miner.state = State.LADDER_CLIMBING;
                        if(Gdx.input.isKeyPressed(19) && mapHandler.getBloc(x, y-1) ==0){
                                velocity.y = GRAVITY; //faut rester appuyé
                        }
                        velocity.y = LADDER_VELOCITY;
                        if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.Z)){
                            velocity.y = 0.2f - GRAVITY; //faut rester appuyé
                        } 
                    } else if(!Miner.state.equals(State.JUMPING)) {
                        velocity.y = JUMPING_VELOCITY;
                        Miner.minerOnTheGround = false;
                        Miner.state = State.JUMPING;
                    }
                } else {
                    // Lancer animation ici ?
                    launchDestruction = true;
                    y++;
                }
                if(Miner.isOnLadder && velocity.y==0){
                    launchDestruction = true;
                }
                break;
            case RIGHT:
                if(!mapHandler.isCellSurfaceHere(x+1, y)) {
                    velocity.x = VELOCITY_MAX;
                    Miner.headTowardsRight = true;
                    Miner.state = State.MOVING;
                } else {
                    launchDestruction = true;
                    x++;
                }
                break;
            case BOTTOM:
                if(Miner.state.equals(State.LADDER_CLIMBING)) {
                    Miner.state = State.LADDER_CLIMBING;
                    velocity.x = 0;
                    velocity.y = -LADDER_VELOCITY;
                    if(Gdx.input.isKeyPressed(20) || Gdx.input.isKeyJustPressed(47)){
                        velocity.y = GRAVITY; //faut rester appuyé
                    }
                } else if(mapHandler.isCellSurfaceHere(x, y-1) && velocity.epsilonEquals(new Vector2(), 0.02f)) {
                    launchDestruction = true;
                    y--;
                }
                break;
            case LEFT:
                if(!mapHandler.isCellSurfaceHere(x-1, y)) {
                    velocity.x = -VELOCITY_MAX; // il y a un autre truc qui met à cette valeur ...
                    Miner.headTowardsRight = false;
                    Miner.state = State.MOVING;
                } else {
                    launchDestruction = true;
                    x--;
                }
                break;
            default:
                break;
        }
        
        if (launchDestruction)
            mapHandler.destructionBloc(x, y);
        
        velocity.x = MathUtils.clamp(velocity.x, -VELOCITY_MAX, VELOCITY_MAX); // On borne
        velocity.add(0, GRAVITY); // Ajout gravité si sur echelle
        if(Math.abs(velocity.x) < 1) { // Si le velocity est trop faible on stop le mineur
            velocity.x = 0; // Et on detecte dans la boucle pour changer de mode de depla
            if(Miner.minerOnTheGround) Miner.stopMiner();
        }
        velocity.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé pendant la frame
        collision.handle(); // Gestion des colisions
        System.out.println("ON AJOUTE " + velocity);
        positionMineur.add(velocity);
        velocity.scl(1/Gdx.graphics.getDeltaTime());
        if(Miner.isOnLadder && (Gdx.input.isKeyJustPressed(19)) && mapHandler.getBloc(x, y-1) ==0 && Miner.state.equals(State.LADDER_CLIMBING) )
            velocity.y=0f;
        
        
    }
}