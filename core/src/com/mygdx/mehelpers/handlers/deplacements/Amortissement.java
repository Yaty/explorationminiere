/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.deplacements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Classe gérant le déplacement du mineur en mode amortissement
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Amortissement extends Deplacement {
    private final Vector2 targetPosition;
    private boolean hasTarget;
    private final float VITESSE_AMORTISSEMENT = 150f;
    
    /**
     * @param mapHandler
     * @param positionMineur
     */
    public Amortissement(MapHandler mapHandler, Vector2 positionMineur) {
        super(mapHandler, positionMineur);
        targetPosition = new Vector2(0, 0);
        hasTarget = false;
    }
    /**
     * Méthode appelé à chaque frame quand le mineur est en mode
     * amortissement. Celle-ci créer un vecteur objectif que le mineur au fur
     * et a mesure du temps va rejoindre pour finalemet s'arrêter.
     */
    @Override
    public void move() {
        // On arrête correctement le mineur et on reset toutes les variables        
        if(Math.abs(positionMineur.x - targetPosition.x) < 0.02f) {
            positionMineur.x += targetPosition.x - positionMineur.x; // On recale le mineur correctement
            // Il y a pas mal de doublon ci-dessous qu'on pourrait éviter, à corriger dans le lot 2
            Mineur.dirMineur = Mineur.Direction.Arret;
            Mineur.etat = Mineur.Etat.Arret;
            Mineur.MINEUR_BOUGE = false;
            Mineur.wasMoving = false;
            hasTarget = false; // Idem
            return;
        }
         
        if(mapHandler.isLadderHere((int) positionMineur.x, (int) positionMineur.y)) 
            Mineur.isOnEchelle = true;
        else if(Mineur.isOnEchelle)
            Mineur.isOnEchelle = false;
        
        int x, y;
        switch(Mineur.dirMineur) {
                case Gauche:
                    x = (int) (positionMineur.x - (0.5 + Mineur.LARGEUR/2)); // Position de la case à gauche ou le mineur va devoir aller
                    y = (int) positionMineur.y;
                    if(!hasTarget && !mapHandler.isCellSurfaceHere(x, y) && !Mineur.etat.equals(Etat.Arret)) { // Si pas d'objectif et pas de tiled (bloc) en x, y
                        targetPosition.set((float) ((int) positionMineur.x - (0.5 + Mineur.LARGEUR/2)),  positionMineur.y);
                        hasTarget = true;
                    }
                    break;
                case Droite:
                    x = (int) ((positionMineur.x + 1) + (0.5 - Mineur.LARGEUR/2));
                    y = (int) positionMineur.y;
                    if(!hasTarget && !mapHandler.isCellSurfaceHere(x, y) && !Mineur.etat.equals(Etat.Arret)) {
                        targetPosition.set((float) ((int) ( positionMineur.x + 1) + (0.5 - Mineur.LARGEUR/2)),  positionMineur.y);
                        hasTarget = true;
                    }
                    break;
        }
        velocite.x = (targetPosition.x - positionMineur.x) * VITESSE_AMORTISSEMENT * Gdx.graphics.getDeltaTime();   
        velocite.x = MathUtils.clamp(velocite.x, -MAX_VELOCITE, MAX_VELOCITE); // On borne la velocite
        velocite.add(0, GRAVITE);   // Ajout gravité
        velocite.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé durant la dernière frame
        collision.handle(); // Gestion des colisions
        positionMineur.add(velocite); // Ajout de la velocite au vecteur position
        velocite.scl(1/Gdx.graphics.getDeltaTime()); // On remet velocite comme avant
    } 
    
    @Override
    public Vector2 getTargetPosition() {
        return targetPosition;
    }
}
