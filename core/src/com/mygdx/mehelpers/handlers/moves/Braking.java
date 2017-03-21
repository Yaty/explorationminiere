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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Miner;
import com.mygdx.gameobjects.Miner.State;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Handle the movement of the Miner, used when we are braking
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Braking extends Move {
    private final Vector2 targetPosition;
    private boolean hasTarget;
    private final float BRAKING_SPEED = 150f;
    public static boolean brakingEnded = false;
    
    /**
     * @param mapHandler a map handler
     * @param positionMineur the miner position
     */
    public Braking(MapHandler mapHandler, Vector2 positionMineur) {
        super(mapHandler, positionMineur);
        targetPosition = new Vector2();
        hasTarget = false;
    }
    
    /**
     * Called every frame to handle the miner braking
     */
    @Override
    public void move() {
        // On arrête correctement le mineur et on reset toutes les variables        
        if(positionMineur.epsilonEquals(targetPosition, 0.02f)) {
            positionMineur.x += targetPosition.x - positionMineur.x; // On recale le mineur correctement
            // Il y a pas mal de doublon ci-dessous qu'on pourrait éviter, à corriger dans le lot 2
            brakingEnded = true;
            Miner.stopMiner();
            hasTarget = false; // Idem
            return;
        }

        float x, y;
        switch(Miner.direction) {
                case LEFT:
                    x = (int) (positionMineur.x) - 1 + (0.5f - Miner.WIDTH/2); // Position de la case à gauche ou le mineur va devoir aller
                    y = (int) positionMineur.y;
                    if(!hasTarget && !mapHandler.isCellSurfaceHere((int) x, (int) y)) { // Si pas d'objectif et pas de tiled (bloc) en x, y
                        targetPosition.set(x, y);
                        hasTarget = true;
                    }
                    break;
                case RIGHT:
                    x = (int) (positionMineur.x) + 1 + (0.5f - Miner.WIDTH/2);
                    y = (int) positionMineur.y;
                    if(!hasTarget && !mapHandler.isCellSurfaceHere((int) x, (int) y)) {
                        targetPosition.set(x, y);
                        hasTarget = true;
                    }
                    break;
        }
        if(hasTarget) {
            velocity.x = (targetPosition.x - positionMineur.x) * BRAKING_SPEED * Gdx.graphics.getDeltaTime();   
            velocity.x = MathUtils.clamp(velocity.x, -VELOCITY_MAX, VELOCITY_MAX); // On borne la velocity
            velocity.add(0, GRAVITY);   // Ajout gravité
            velocity.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé durant la dernière frame
            collision.handle(); // Gestion des colisions
            positionMineur.add(velocity); // Ajout de la velocity au vecteur position
            velocity.scl(1/Gdx.graphics.getDeltaTime()); // On remet velocity comme avant
        }
    } 
}
