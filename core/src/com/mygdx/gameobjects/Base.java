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
package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Class which represent a base
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Base extends Rectangle {    
    // XXXXX
    // X   X WITH X = wall, M shop and empty = nothing
    //    MX
    // XXXXX

    /**
     * Representation of the base in blocks
     * XXXXX
     * X   X WITH X = wall, M shop and empty = nothing
     *    MX
     * XXXXX
     */
    public static final int[][] CELLS =
    {
        //SOL
        {MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround},
        // Air
        {0, 0, 0, MapHandler.idStore, MapHandler.idBaseGround},
        // Air
        {MapHandler.idBaseGround, 0, 0, 0, MapHandler.idBaseGround},
        // Plafond
        {MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround, MapHandler.idBaseGround}
    };
    
    /**
     * Create a base with an origin
     * @param x0 the x-origin
     * @param y0 the y-origin
     */
    public Base(int x0, int y0) {
        super(x0, y0, CELLS[0].length, CELLS.length);
    } 

    /**
     * Get the coordinate of the base
     * @return a 2D vector
     */
    public Vector2 getPos() {
        return new Vector2(x, y);
    }   
    
}
