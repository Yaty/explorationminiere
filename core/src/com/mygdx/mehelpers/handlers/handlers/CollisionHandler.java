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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.gameobjects.Miner;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.handlers.moves.Move;

/**
 * Handle the collision
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class CollisionHandler implements Handler {
    private Move move;
    private final Array<Rectangle> tiles = new Array<Rectangle>();
    private final Pool<Rectangle> rectPool = new Pool<Rectangle> () {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Rectangle minerRect;
    private int startX, startY, endX, endY;
    
    /**
     * @param move the move instance which handle the shifting
     */
    public CollisionHandler(Move move) {
        this.move = move;
    }
   
    /**
     * Handle collisions in the x-axis
     */
    private void handleCollisionX() {
        minerRect.set(move.getPositionMineur().x, move.getPositionMineur().y, Miner.WIDTH, Miner.HEIGHT);
        startX = (int) move.getPositionMineur().x;
        endX = (int) (move.getPositionMineur().x + Miner.WIDTH);

        if(Miner.direction.equals(Miner.Direction.RIGHT)) { // Si vers la droite
            // hitbox à droite
            startX = endX = (int) (move.getPositionMineur().x + Miner.WIDTH + move.getVelocity().x);
            if((move.getPositionMineur().x + Miner.WIDTH + move.getVelocity().x) >= GameWorld.MAP_WIDTH) {
                move.getVelocity().x = 0;
                Miner.direction = Miner.Direction.STOPPED;
                Miner.state = Miner.State.STOPPED;         
                return;
            }
            
        } else if(Miner.direction.equals(Miner.Direction.LEFT)) { // Vers la gauche
            // hitbox à gauche
            startX = endX = (int) (move.getPositionMineur().x + move.getVelocity().x);
            if((move.getPositionMineur().x + move.getVelocity().x) <= 0) {
                move.getVelocity().x = 0;
                Miner.direction = Miner.Direction.STOPPED;
                Miner.state = Miner.State.STOPPED;
                return;
            }
        }
        
        startY = (int) move.getPositionMineur().x;
        endY = (int)(move.getPositionMineur().y + Miner.WIDTH);

        getTiles(startX, startY, endX, endY, tiles); // Voir méthode
        minerRect.x += move.getVelocity().x;
        for(Rectangle tile : tiles) {
            if(minerRect.overlaps(tile)) { // Si notre rectangle contient le rectangle tile on arrête le bonhomme et on stop
                move.getVelocity().x = 0;
                Miner.direction = Miner.Direction.STOPPED;
                Miner.state = Miner.State.STOPPED;
                break;
            }
        }
        minerRect.x = move.getPositionMineur().x; // On remet en t, plus en t+1
    }
    
    /**
     * Handle collisions in the y-axis
     */    
    private void handleCollisionY() {           
        if(move.getVelocity().y > 0) {
            startY = endY = (int) (move.getPositionMineur().y + Miner.HEIGHT + move.getVelocity().y);
        } else {
            startY = endY = (int) (move.getPositionMineur().y + move.getVelocity().y);
            if((move.getPositionMineur().y + move.getVelocity().y) <= 0) {
                move.getVelocity().y = 0;                
            }
        }
        startX = (int)(move.getPositionMineur().x);
        endX = (int) (move.getPositionMineur().x + Miner.WIDTH);
        getTiles(startX, startY, endX, endY, tiles);
        minerRect.y += move.getVelocity().y;
        for(Rectangle tile : tiles) {
            if(minerRect.overlaps(tile)) {
                // Notre hitbox est en colision
                // On reset la position en y
                if(move.getVelocity().y > 0) {
                    // Repositionnement (tile est le bloc qui va entrer en colision)
                    move.getPositionMineur().y = tile.y - Miner.HEIGHT;
                } else {
                    // Position du mineur au dessus du tile.y (car tile.y pointe vers le bas, on ajoute la hauteur)
                    move.getPositionMineur().y = tile.y + tile.height;
                    // Changement d'état
                    Miner.minerOnTheGround = true;
                }
                move.getVelocity().y = 0;
                break;                    
            }
        }
    }
    
    /**
     * Get tiles around startX, startY, endX and endY into tiles
     */
    private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        // On va recuperer tout les tiles dans le rectangle de coordonnes (startx, starty, finx, finy) existantes
        TiledMapTileLayer layer = move.mapHandler.getLayerSurface(); // Une couche qui va contenir tout les "walls"
        rectPool.freeAll(tiles); // Libère nos objets dans la pool
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                        if (cell != null) {
                                Rectangle rect = rectPool.obtain();
                                rect.set(x, y, 1, 1);
                                tiles.add(rect);
                        }
                }
        }
    } 

    /**
     * Called when the game is shutting down
     */
    public void dispose() {}

    /**
     * Handle the collision
     */
    @Override
    public void handle() {
        minerRect = rectPool.obtain(); // On recupere un objet Rectangle dans notre pool
        handleCollisionX();
        handleCollisionY();
        rectPool.free(minerRect); // Libération de mineurRect dans la pool
    }

    /**
     * Reload the move object
     * @param objects move is in the first place
     */
    @Override
    public void reload(Object... objects) {
        move = (Move) objects[0];
    }
}
