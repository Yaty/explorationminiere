/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.handlers.deplacements.Deplacement;

/**
 *
 * @author Hugo
 */
public class CollisionHandler implements Handler {
    private Deplacement deplacement;
    private final Array<Rectangle> tiles = new Array<Rectangle>();
    private final Pool<Rectangle> rectPool = new Pool<Rectangle> () {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Rectangle mineurRect;
    private int debutX, debutY, finX, finY;
    private final int largeurMap;
    
    /**
     * @param deplacement objet Deplacement
     */
    public CollisionHandler(Deplacement deplacement) {
        this.deplacement = deplacement;
        largeurMap = GameWorld.MAP_WIDTH;
    }
   
    /**
     * Gère les collisions en  abscisse
     */
    private void handleCollisionX() {
        mineurRect.set(deplacement.getPositionMineur().x, deplacement.getPositionMineur().y, Mineur.LARGEUR, Mineur.HAUTEUR);
        debutX = (int) deplacement.getPositionMineur().x;
        finX = (int) (deplacement.getPositionMineur().x + Mineur.LARGEUR);

        if(Mineur.dirMineur.equals(Mineur.Direction.Droite)) { // Si vers la droite
            // hitbox à droite
            debutX = finX = (int) (deplacement.getPositionMineur().x + Mineur.LARGEUR + deplacement.getVelocite().x);
            if((deplacement.getPositionMineur().x + Mineur.LARGEUR + deplacement.getVelocite().x) >= largeurMap) {
                deplacement.getVelocite().x = 0;
                Mineur.dirMineur = Mineur.Direction.Arret;
                Mineur.etat = Mineur.Etat.Arret;         
                return;
            }
            
        } else if(Mineur.dirMineur.equals(Mineur.Direction.Gauche)) { // Vers la gauche
            // hitbox à gauche
            debutX = finX = (int) (deplacement.getPositionMineur().x + deplacement.getVelocite().x);
            if((deplacement.getPositionMineur().x + deplacement.getVelocite().x) <= 0) {
                deplacement.getVelocite().x = 0;
                Mineur.dirMineur = Mineur.Direction.Arret;
                Mineur.etat = Mineur.Etat.Arret;
                return;
            }
        }
        
        debutY = (int) deplacement.getPositionMineur().x;
        finY = (int)(deplacement.getPositionMineur().y + Mineur.LARGEUR);

        getTiles(debutX, debutY, finX, finY, tiles); // Voir méthode
        mineurRect.x += deplacement.getVelocite().x;
        for(Rectangle tile : tiles) {
            if(mineurRect.overlaps(tile)) { // Si notre rectangle contient le rectangle tile on arrête le bonhomme et on stop
                deplacement.getVelocite().x = 0;
                Mineur.dirMineur = Mineur.Direction.Arret;
                Mineur.etat = Mineur.Etat.Arret;
                break;
            }
        }
        mineurRect.x = deplacement.getPositionMineur().x; // On remet en t, plus en t+1
    }
    
    /**
     * Gère les collisions en ordonnée
     */    
    private void handleCollisionY() {           
        if(deplacement.getVelocite().y > 0) {
            debutY = finY = (int) (deplacement.getPositionMineur().y + Mineur.HAUTEUR + deplacement.getVelocite().y);
        } else {
            debutY = finY = (int) (deplacement.getPositionMineur().y + deplacement.getVelocite().y);
            if((deplacement.getPositionMineur().y + deplacement.getVelocite().y) <= 0) {
                deplacement.getVelocite().y = 0;                
            }
        }
        debutX = (int)(deplacement.getPositionMineur().x);
        finX = (int) (deplacement.getPositionMineur().x + Mineur.LARGEUR);
        getTiles(debutX, debutY, finX, finY, tiles);
        mineurRect.y += deplacement.getVelocite().y;
        for(Rectangle tile : tiles) {
            if(mineurRect.overlaps(tile)) {
                // Notre hitbox est en colision
                // On reset la position en y
                if(deplacement.getVelocite().y > 0) {
                    // Repositionnement (tile est le bloc qui va entrer en colision)
                    deplacement.getPositionMineur().y = tile.y - Mineur.HAUTEUR;
                } else {
                    // Position du mineur au dessus du tile.y (car tile.y pointe vers le bas, on ajoute la hauteur)
                    deplacement.getPositionMineur().y = tile.y + tile.height;
                    // Changement d'état
                    Mineur.mineurAuSol = true;
                }
                deplacement.getVelocite().y = 0;
                break;                    
            }
        }
    }
    
    
    /**
     * Méthode qui va récuperer les blocs autour du mineur et
     * les ajouter dans la list tiles.
     */
    private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        // On va recuperer tout les tiles dans le rectangle de coordonnes (startx, starty, finx, finy) existantes
        TiledMapTileLayer layer = deplacement.mapHandler.getLayerSurface(); // Une couche qui va contenir tout les "walls"
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

    public void dispose() {}

    @Override
    public void handle() {
        mineurRect = rectPool.obtain(); // On recupere un objet Rectangle dans notre pool
        handleCollisionX();
        handleCollisionY();
        rectPool.free(mineurRect); // Libération de mineurRect dans la pool
    }

    @Override
    public void reload(Object... objects) {
        deplacement = (Deplacement) objects[0];
    }
}
