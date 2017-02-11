/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;


/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class FogHandler {
    private final TiledMapTileLayer fogLayer;
    private final TiledMapTile fogTile;
    private final int RAYON = 10;
    
    public FogHandler(TiledMapTileLayer tileSet, TiledMapTile tile) {
        this.fogLayer = tileSet;
        this.fogTile = tile;
    }
    
    public void handle(Vector2 positionMineur) {
        int x = (int) positionMineur.x;
        int y = (int) positionMineur.y;
        
        for(int i = x - RAYON ; i < x + RAYON ; i++) {
            for(int j = y - RAYON ; j < y + RAYON ; j++) {
                if(Vector2.dst2(x, y, i, j) <= RAYON) {
                    Cell cell = new Cell();
                    cell.setTile(fogTile);
                    fogLayer.setCell(i, j, cell);
                }
            }
        }
        
    }
}
