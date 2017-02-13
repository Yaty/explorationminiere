/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;


/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class FogHandler implements Handler {
    private Vector2 positionMineur;
    private TiledMapTileLayer fogLayer;
    private final int RAYON = 10;
    
    public FogHandler(TiledMapTileLayer fogTileLayer, Vector2 positionMineur) {
        this.positionMineur = positionMineur;
        this.fogLayer = fogTileLayer;
    }
    
    @Override
    public void handle() {
        int x = (int) positionMineur.x;
        int y = (int) positionMineur.y;
        
        for(int i = x - RAYON ; i < x + RAYON ; i++) {
            for(int j = y - RAYON ; j < y + RAYON ; j++) {
                if(Vector2.dst2(x, y, i, j) <= RAYON) {
                    fogLayer.setCell(i, j, null);
                }
            }
        }
    }

    @Override
    public void reload(Object... objects) {
        this.fogLayer = (TiledMapTileLayer) objects[0];
        this.positionMineur = (Vector2) objects[1];
    }
}
