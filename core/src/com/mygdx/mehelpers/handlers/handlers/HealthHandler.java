/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.mineurobjects.Health;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class HealthHandler implements Handler {
    private MapHandler mapHandler;
    private Health mineurHealth;
    private Vector2 positionMineur;
    
    public HealthHandler(Health mineurHealth, MapHandler mapHandler, Vector2 positionMineur) {
        this.mapHandler = mapHandler;
        this.mineurHealth = mineurHealth;
        this.positionMineur = positionMineur;
    }
    
    @Override
    public void handle() {
        // Note : Toutes la gestion ne se passe pas ici (pour la saut c'est dans Deplacement et pour le descente c'est dans CellsHandler)
        //Si la vie du mineur tombe en dessous de 0
        if(mapHandler.isMineurInBase() || mapHandler.isMineurInSurface())
            mineurHealth.add(0.0005f);
        else if(Mineur.MINEUR_BOUGE)
            mineurHealth.remove(0.0005f);
        
        // mineur.getHealth().remove(0.01f);
        
        
        if(mineurHealth.getHealth() <= 0f){
            mineurHealth.setHealth(1);
            makeMineurRespawn();
        } else if(mineurHealth.getHealth() > 1f)
            mineurHealth.setHealth(1);
    }

    private void makeMineurRespawn() {
        Vector2 respawnPos = MapHandler.getSpawnPosition();
        positionMineur.x = respawnPos.x;
        positionMineur.y = respawnPos.y;
    }

    @Override
    public void reload(Object... objects) {
        mapHandler = (MapHandler) objects[0];
        mineurHealth = (Health) objects[1];
        positionMineur = (Vector2) objects[2];
    }
}
