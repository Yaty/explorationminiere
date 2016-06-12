/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.Deplacement;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.gameobjects.Mineur;

/**
 *
 * @author Hugo
 */
public class Collision {
    private final Deplacement deplacement;
    private final Array<Rectangle> tiles = new Array<Rectangle>();
    private final Pool<Rectangle> rectPool = new Pool<Rectangle> () {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Rectangle mineurRect;
    private int debutX, debutY, finX, finY;
    
    /**
     *
     * @param deplacement
     */
    public Collision(Deplacement deplacement) {
        this.deplacement = deplacement;
    }
    
    /**
     *
     */
    public void handleCollision() {
        deplacement.getMineur().getCellsHandler().getCellsSurfaceAroundMineur();
        mineurRect = rectPool.obtain(); // On recupere un objet Rectangle dans notre pool
        handleCollisionX();
        handleCollisionY();
        rectPool.free(mineurRect); // Libération de mineurRect dans la pool
    }
   
    
    private void handleCollisionX() {
        mineurRect.set(deplacement.getMineur().getPosition().x, deplacement.getMineur().getPosition().y, deplacement.getMineur().getLARGEUR(), deplacement.getMineur().getHAUTEUR());
        debutX = (int) deplacement.getMineur().getPosition().x;
        finX = (int) (deplacement.getMineur().getPosition().x + deplacement.getMineur().getLARGEUR());

        if(deplacement.getMineur().getDirectionMineur().equals(Mineur.Direction.Droite)) { // Si vers la droite
            // hitbox à droite
            debutX = finX = (int) (deplacement.getMineur().getPosition().x + deplacement.getMineur().getLARGEUR() + deplacement.getVelocite().x);
        } else if(deplacement.getMineur().getDirectionMineur().equals(Mineur.Direction.Gauche)) { // Vers la gauche
            // hitbox à gauche
            debutX = finX = (int) (deplacement.getMineur().getPosition().x + deplacement.getVelocite().x);
        }
        
        debutY = (int) deplacement.getMineur().getPosition().x;
        finY = (int)(deplacement.getMineur().getPosition().y + deplacement.getMineur().getLARGEUR());

        getTiles(debutX, debutY, finX, finY, tiles); // Voir méthode
        mineurRect.x += deplacement.getVelocite().x;
        for(Rectangle tile : tiles) {
            if(mineurRect.overlaps(tile)) { // Si notre rectangle contient le rectangle tile on arrête le bonhomme et on stop
                deplacement.getVelocite().x = 0;
                deplacement.getMineur().setDirectionMineur(Mineur.Direction.Arret);
                deplacement.getMineur().setEtatMineur(Mineur.Etat.Arret);
                break;
            }
        }
        mineurRect.x = deplacement.getMineur().getPosition().x; // On remet en t, plus en t+1
    }
    
    private void handleCollisionY() {
        // if the koala is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom            
        if(deplacement.getVelocite().y > 0) {
            debutY = finY = (int) (deplacement.getMineur().getPosition().y + deplacement.getMineur().getHAUTEUR() + deplacement.getVelocite().y);
        } else {
            debutY = finY = (int) (deplacement.getMineur().getPosition().y + deplacement.getVelocite().y);
        }
        debutX = (int)(deplacement.getMineur().getPosition().x);
        finX = (int) (deplacement.getMineur().getPosition().x + deplacement.getMineur().getLARGEUR());
        getTiles(debutX, debutY, finX, finY, tiles);
        mineurRect.y += deplacement.getVelocite().y;
        for(Rectangle tile : tiles) {
            if(mineurRect.overlaps(tile)) {
                // Notre hitbox est en colision
                // On reset la position en y
                if(deplacement.getVelocite().y > 0) {
                    // Repositionnement (tile est le bloc qui va entrer en colision)
                    deplacement.getMineur().getPosition().y = tile.y - deplacement.getMineur().getHAUTEUR();
                } else {
                    // Position du mineur au dessus du tile.y (car tile.y pointe vers le bas, on ajoute la hauteur)
                    deplacement.getMineur().getPosition().y = tile.y + tile.height;
                    // Changement d'état
                    deplacement.getMineur().setMineurAuSol(true);
                }
                deplacement.getVelocite().y = 0;
                break;                    
            }
        }
    }
    
    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isTiledHere(int x, int y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) deplacement.getMineur().getMap().getLayers().get("surface");
        return layer.getCell(x, y) != null; // True si cellule non vide
    }

    private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        // On va recuperer tout les tiles dans le rectangle de coordonnes (startx, starty, finx, finy) existantes
        TiledMapTileLayer layer = (TiledMapTileLayer) deplacement.getMineur().getMap().getLayers().get("surface"); // Une couche qui va contenir tout les "walls"
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
}
