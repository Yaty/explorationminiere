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
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Miner;
import com.mygdx.mehelpers.handlers.moves.Braking;
import com.mygdx.mehelpers.handlers.moves.Move;
import com.mygdx.mehelpers.handlers.moves.Dynamic;

/**
 * Class to handle the shifting
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class MoveHandler implements Handler {
    private Move move;
    private MapHandler mapHandler;
    private Vector2 positionMineur;
    
    /**
     * Create a move handler
     * @param mapHandler a reference to the map handler
     * @param positionMineur the miner's position
     */
    public MoveHandler(MapHandler mapHandler, Vector2 positionMineur) {
        this.mapHandler = mapHandler;
        this.positionMineur = positionMineur;
    }

    /**
     * Handle the movement.
     * Called every frame.
     */
    @Override
    public void handle() {
        if(Miner.DPL_AMORTISSEMENT && !(move instanceof Braking)) {
            move = new Braking(mapHandler, positionMineur);
        } else if (Miner.DPL_FLUIDE && !(move instanceof Dynamic)) {
            move = new Dynamic(mapHandler, positionMineur);
        }
        
        if(move != null) {
            move.move();
        }
    }

    /**
     * Reload the class variables
     * @param objects the objects with a specific order (:/)
     */
    @Override
    public void reload(Object... objects) {
        move = null;
        mapHandler = (MapHandler) objects[0];
        positionMineur = (Vector2) objects[1];
    }
}
