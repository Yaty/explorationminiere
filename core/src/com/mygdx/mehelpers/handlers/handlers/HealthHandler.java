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
import com.mygdx.gameobjects.minerobjects.Health;

/**
 * Class which represent the Miner
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class HealthHandler implements Handler {
    private MapHandler mapHandler;
    private Health mineurHealth;
    private Vector2 positionMineur;
    
    /**
     * Constructor
     * @param mineurHealth the health miner object
     * @param mapHandler the map handler
     * @param positionMineur miner's position
     */
    public HealthHandler(Health mineurHealth, MapHandler mapHandler, Vector2 positionMineur) {
        this.mapHandler = mapHandler;
        this.mineurHealth = mineurHealth;
        this.positionMineur = positionMineur;
    }
    
    /**
     * Handle the miner's life.
     * Called every frame.
     */
    @Override
    public void handle() {
        if(mapHandler.isMineurInBase() || mapHandler.isMineurInSurface())
            mineurHealth.add(0.0005f);
        else if(!Miner.state.equals(Miner.State.STOPPED))
            mineurHealth.remove(0.0005f);
        
        if(mineurHealth.getHealth() <= 0f){
            mineurHealth.setHealth(1);
            positionMineur.set(MapHandler.getSpawnPosition());
        } else if(mineurHealth.getHealth() > 1f)
            mineurHealth.setHealth(1);
    }
    
    /**
     * Add life to the miner
     * @param amount the amount to add
     */
    public void addLife(float amount) {
        mineurHealth.add(amount);
    }
    
    /**
     * Remove life to the miner
     * @param amount the amount to remove
     */
    public void removeLife(float amount) {
        mineurHealth.remove(amount);
    }

    /**
     * Reload objects
     * @param objects
     */
    @Override
    public void reload(Object... objects) {
        mapHandler = (MapHandler) objects[0];
        mineurHealth = (Health) objects[1];
        positionMineur = (Vector2) objects[2];
    }

    /**
     * Make the miner die.
     * Not so sweet ...
     */
    public void die() {
        mineurHealth.setHealth(-1);
    }
}
