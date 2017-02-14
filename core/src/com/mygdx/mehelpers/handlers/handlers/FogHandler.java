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

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;


/**
 * Class to handle the fog of war
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class FogHandler implements Handler {
    private Vector2 positionMineur;
    private TiledMapTileLayer fogLayer;
    private final int RADIUS = 10;
    
    /**
     * Constructor
     * @param fogTileLayer the layer corresponding to the fog
     * @param positionMineur miner's position
     */
    public FogHandler(TiledMapTileLayer fogTileLayer, Vector2 positionMineur) {
        this.positionMineur = positionMineur;
        this.fogLayer = fogTileLayer;
    }
    
    /**
     * Handle the fog. Called very frame.
     * Remove fog from the miner (with a circle)
     */
    @Override
    public void handle() {
        int x = (int) positionMineur.x;
        int y = (int) positionMineur.y;
        
        for(int i = x - RADIUS ; i < x + RADIUS ; i++) {
            for(int j = y - RADIUS ; j < y + RADIUS ; j++) {
                if(Vector2.dst2(x, y, i, j) <= RADIUS) {
                    fogLayer.setCell(i, j, null);
                }
            }
        }
    }

    /**
     * Reload objects
     * @param objects
     */
    @Override
    public void reload(Object... objects) {
        this.fogLayer = (TiledMapTileLayer) objects[0];
        this.positionMineur = (Vector2) objects[1];
    }
}
