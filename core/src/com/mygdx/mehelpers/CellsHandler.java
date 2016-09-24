/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Direction;
import com.mygdx.gameobjects.Mineur.Etat;
import com.mygdx.mehelpers.Deplacement.Fluide;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe gérant les cellules du jeu
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class CellsHandler {
    private boolean victory, cassageEnCour;
    private final Mineur mineur;
    private final TiledMapTileLayer layerSurface, layerObjets;
    private final boolean[] cellsSAM; // CellsSurfaceAroundMineur
    
    /**
     * Constructeur par défaut
     * @param mineur Référence au mineur
     */
    public CellsHandler(Mineur mineur) {
        this.mineur = mineur;
        this.victory = false;
        this.layerSurface = (TiledMapTileLayer) mineur.getMap().getLayers().get("surface");
        this.layerObjets =  (TiledMapTileLayer) mineur.getMap().getLayers().get("objets");
        this.cellsSAM = new boolean[4];  
    }
    
    /**
     * @return la valeur de la variable victory
     */
    public boolean isVictory(){
        return this.victory;
    }
    

    /**
     * Remplit un tableau pour savoir s'il y a des blocs autour du mineur
     */
    public void getCellsSurfaceAroundMineur() {
        int y = (int) mineur.getPosition().y;
        int x = (int) mineur.getPosition().x;
        
        //   0
        // 3 M 1
        //   2
        if(layerSurface.getCell(x, y+1)!=null)
            cellsSAM[0] = true;
        else
            cellsSAM[1] = false;  
        
        if(layerSurface.getCell(x+1, y)!=null) {
            float distanceADroite = x+1 - (mineur.getPosition().x + mineur.getLARGEUR());
            if(distanceADroite < 0.1 && distanceADroite > 0) cellsSAM[1] = true;;
        }
        else
            cellsSAM[1] = false;
        
        if(layerSurface.getCell(x, y-1)!=null)
            cellsSAM[2] = true;
        else
            cellsSAM[2] = false;
        
        if(layerSurface.getCell(x-1, y)!=null) {
            float distanceAGauche = mineur.getPosition().x - x;
            if(distanceAGauche < 0.1 && distanceAGauche > 0) cellsSAM[3] = true;
        }
        else
            cellsSAM[3] = false;   
    }
    
    /**
     * @return un tableau de booléen qui indique si un bloc et existant autour du mineur
     */
    public boolean[] getCellsSAM() {
        return cellsSAM;
    }
    
    /**
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     * @return vrai s'il y a une échelle à cette position, non sinon
     */    
    public boolean isLadderHere(int x,int y){
        if(layerObjets.getCell(x, y) != null)
            return layerObjets.getCell(x,y).getTile().getId() == 5;
        return false;
    }
    
    /**
     * Ajoute une échelle au coordonnée mis en paramètre
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void setLadder(int x,int y){
        Cell cell = new Cell();
        TiledMapTileSet tileSet = mineur.getMap().getTileSets().getTileSet("ladder");
        cell.setTile(tileSet.getTile(5));
        layerObjets.setCell(x, y, cell);
    }
    
    /**
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     * @return vrai si cell présente en x et y, faux sinon
     */
    private boolean isCellSurfaceHere(int x, int y) {
        return layerSurface.getCell(x, y) != null;
    }
 
    
    /**
     * Détruit un bloc à la position passée
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void destructionBloc(int x, int y) {  
        final int xBloc = x;
        final int yBloc = y;
        if(isCellSurfaceHere(x, y) && !cassageEnCour && mineur.isMineurAuSol() && layerSurface.getCell(x, y).getTile().getId() != 6 ) {
            if(layerSurface.getCell(x, y).getTile().getId() == 4)
                victory = true;
            mineur.setEtatMineur(Etat.Miner);
            mineur.setDirectionMineur(Direction.Arret);
            cassageEnCour = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    layerSurface.setCell(xBloc, yBloc, null);
                    mineur.setEtatMineur(Etat.Arret);
                    mineur.setDirectionMineur(Direction.Arret);
                    cassageEnCour = false;
                }
            }, 1000);
        }
    }    
    
}