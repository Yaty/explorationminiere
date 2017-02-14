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

import com.mygdx.mehelpers.handlers.handlers.CollisionHandler;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Abstract class to handle the miner movement
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public abstract class Move {
    public static float GRAVITY = -0.4f, VELOCITY_MAX = 4f, JUMPING_VELOCITY = 8f, LADDER_VELOCITY = 6f;
    protected CollisionHandler collision;
    public MapHandler mapHandler;
    protected Vector2 velocity, positionMineur;
    
    /**
     * @param positionMineur the miner position
     * @param mapHandler the map handler
     */
    public Move(MapHandler mapHandler, Vector2 positionMineur) {
        velocity = new Vector2(0,0);
        this.positionMineur = positionMineur;
        this.mapHandler = mapHandler;
        collision = new CollisionHandler(this);
    }
    
    /**
     * Get the miner position
     * @return position of the miner
     */
    public Vector2 getPositionMineur() {
        return positionMineur;
    }

    /**
     * Get the velocity
     * @return the velocity vector
     */
    public Vector2 getVelocity() {
        return velocity;
    }
    
    /**
     * Move the miner
     */
    public abstract void move();

    /**
     * Called when the game is being closed to shutdown properly the game
     */
    public void dispose() {
        collision.dispose();
    }

}
