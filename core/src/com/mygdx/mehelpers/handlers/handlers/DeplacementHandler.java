/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.handlers.deplacements.Amortissement;
import com.mygdx.mehelpers.handlers.deplacements.Deplacement;
import com.mygdx.mehelpers.handlers.deplacements.Fluide;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class DeplacementHandler implements Handler {
    private Deplacement deplacement;
    private MapHandler mapHandler;
    private Vector2 positionMineur;
    
    public DeplacementHandler(MapHandler mapHandler, Vector2 positionMineur) {
        this.mapHandler = mapHandler;
        this.positionMineur = positionMineur;
    }
    
    public Deplacement getDeplacement() {
        return deplacement;
    }

    @Override
    public void handle() {
        if(Mineur.DPL_AMORTISSEMENT && !(deplacement instanceof Amortissement)) {
            deplacement = new Amortissement(mapHandler, positionMineur);
        } else if (Mineur.DPL_FLUIDE && !(deplacement instanceof Fluide)) {
            deplacement = new Fluide(mapHandler, positionMineur);
        }
        
        if(deplacement != null) {
            /*if(deplacement.getVelocite().isZero() && !Mineur.etat.equals(Mineur.Etat.Miner)) {
                Mineur.dirMineur = Mineur.Direction.Arret;
                Mineur.etat = Mineur.Etat.Arret;
                wasMoving = DPL_AMORTISSEMENT = DPL_FLUIDE = false;
            } */
            deplacement.move();
        }
    }

    @Override
    public void reload(Object... objects) {
        deplacement = null;
        mapHandler = (MapHandler) objects[0];
        positionMineur = (Vector2) objects[1];
    }
}
