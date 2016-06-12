/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.Deplacement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;

/**
 *
 * @author Hugo
 */
public class Amortissement extends Deplacement {

    private final Vector2 targetPosition;
    private boolean hasTarget;
    private final float VITESSE_AMORTISSEMENT = 150f;
    
    /**
     *
     * @param mineur
     */
    public Amortissement(Mineur mineur) {
        super(mineur);
        targetPosition = new Vector2(0, 0);
        hasTarget = false;
    }

    /**
     *
     */
    @Override
    public void move() {
        // On arrête correctement le mineur et on reset toutes les variables        
        if(Math.abs(mineur.getPosition().x - targetPosition.x) < 0.02f) {
            mineur.getPosition().x += targetPosition.x - mineur.getPosition().x; // On recale le mineur correctement
            // Il y a pas mal de doublon ci-dessous qu'on pourrait éviter, à corriger dans le lot 2
            mineur.setDirectionMineur(Mineur.Direction.Arret); // Direction = Arret
            mineur.setEtatMineur(Mineur.Etat.Arret); // Etat = Arret
            mineur.setMoving(false); // Ne bouge pas
            mineur.setWasMoving(false); // On reset
            hasTarget = false; // Idem
        }
        int x, y;
        switch(mineur.getDirectionMineur()) {
                case Gauche:
                    x = (int) (mineur.getPosition().x - (0.5 + mineur.getLARGEUR()/2)); // Position de la case à gauche ou le mineur va devoir aller
                    y = (int) mineur.getPosition().y;
                    if(!hasTarget && !collision.isTiledHere(x, y)) { // Si pas d'objectif et pas de tiled (bloc) en x, y
                        targetPosition.set((float) ((int) mineur.getPosition().x - (0.5 + mineur.getLARGEUR()/2)),  mineur.getPosition().y);
                        hasTarget = true;
                    }
                    break;
                case Droite:
                    x = (int) ((mineur.getPosition().x + 1) + (0.5 - mineur.getLARGEUR()/2));
                    y = (int) mineur.getPosition().y;
                    if(!hasTarget && !collision.isTiledHere(x, y)) {
                        targetPosition.set((float) ((int) ( mineur.getPosition().x + 1) + (0.5 - mineur.getLARGEUR()/2)),  mineur.getPosition().y);
                        hasTarget = true;
                    }
                    break;    
                default:
                    break;
        }
        velocite.x = (targetPosition.x - mineur.getPosition().x) * VITESSE_AMORTISSEMENT * Gdx.graphics.getDeltaTime();   
        velocite.x = MathUtils.clamp(velocite.x, -mineur.getMAX_VELOCITE(), mineur.getMAX_VELOCITE()); // On borne la velocite
        velocite.add(0, mineur.getGRAVITE());   // Ajout gravité
        velocite.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé durant la dernière frame
        collision.handleCollision(); // Gestion des colisions
        mineur.getPosition().add(velocite); // Ajout de la velocite au vecteur position
        velocite.scl(1/Gdx.graphics.getDeltaTime()); // On remet velocite comme avant
    }
    
}
