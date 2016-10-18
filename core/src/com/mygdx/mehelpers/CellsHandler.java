/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
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
        Object idLadder = mineur.getMap().getTileSets().getTileSet("ladder.gif").getProperties().get("firstgid");
        int idLadder2 = (Integer)idLadder;
        if(layerObjets.getCell(x, y) != null){
            return layerObjets.getCell(x,y).getTile().getId() == idLadder2;
        }
        return false;
    }
    
    /**
     * Ajoute une échelle au coordonnée mis en paramètre
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void setLadder(int x,int y){
        Cell cell = new Cell();
        Object idLadder = mineur.getMap().getTileSets().getTileSet("ladder.gif").getProperties().get("firstgid");
        int idLadder2 = (Integer)idLadder;
        TiledMapTileSet tileSet = mineur.getMap().getTileSets().getTileSet("ladder.gif");
        cell.setTile(tileSet.getTile(idLadder2));
        layerObjets.setCell(x, y, cell);
    }
    
    public void setPilier(int x, int y){
        Cell cell = new Cell();
        Object idPilier = mineur.getMap().getTileSets().getTileSet("pilier.gif").getProperties().get("firstgid");
        int idPilier2 = (Integer)idPilier;
        TiledMapTileSet tileSet = mineur.getMap().getTileSets().getTileSet("pilier.gif");
        cell.setTile(tileSet.getTile(idPilier2));
        if(mineur.isTeteVersLaDroite())
            layerObjets.setCell(x+1, y, cell);
        else
            layerObjets.setCell(x-1, y, cell);
    }
    /**
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     * @return vrai si cell présente en x et y, faux sinon
     */
    private boolean isCellSurfaceHere(int x, int y) {
        return layerSurface.getCell(x, y) != null;
    }
    
    public boolean isCellObjectHere(int x, int y){
        return layerObjets.getCell(x, y) == null;
    }
    
    
    private boolean isCellDessous(int xBloc, int yBloc){
        int xCellUp = xBloc;
        int yCellUp = yBloc+1;
        Object idPierre =  mineur.getMap().getTileSets().getTileSet("stone.png").getProperties().get("firstgid");
        int idPierre2 = (Integer) idPierre;
        if(layerSurface.getCell(xCellUp, yCellUp) != null && layerSurface.getCell(xCellUp, yCellUp).getTile() != null) {
            if(layerSurface.getCell(xCellUp, yCellUp).getTile().getId() == idPierre2){
                    return true;
            }
        }
        return false;
    }
    
    private void pierreTombe(int xBloc, int yBloc){ 
        Cell cell = new Cell();
        Object idPierre = mineur.getMap().getTileSets().getTileSet("stone.png").getProperties().get("firstgid");
        int idPierre2 = (Integer)idPierre;
        TiledMapTileSet tileSet = mineur.getMap().getTileSets().getTileSet("stone.png");
        cell.setTile(tileSet.getTile(idPierre2));
        //On parcourt les blocs des bas en haut tant que ce sont des blocs de pierre
        Object idPilier =  mineur.getMap().getTileSets().getTileSet("pilier.gif").getProperties().get("firstgid");
        int idPilier2 = (Integer) idPilier;
        
        if(layerSurface.getCell(xBloc, yBloc+1).getTile() != null && layerSurface.getCell(xBloc, yBloc+1) != null){
            while(layerSurface.getCell(xBloc, yBloc+1).getTile().getId() == idPierre2){
                int yBlocCible = yBloc;
                int xBlocCible = xBloc;
                //On parcourt les blocs de haut vers le bas tant que le bloc cible est vide 
                while(layerSurface.getCell(xBlocCible, yBlocCible) == null){
                    yBlocCible--;
                    System.out.println("1");
                    if(layerObjets.getCell(xBlocCible, yBlocCible) != null){
                        System.out.println("2");
                        System.out.println("GET ID:" + layerObjets.getCell(xBlocCible, yBlocCible).getTile().getId());
                        System.out.println("idPilier2:" + idPilier2);
                        if(layerObjets.getCell(xBlocCible, yBlocCible).getTile().getId() == idPilier2 ){
                            System.out.println("Pilier en dessous");
                            break;
                        }
                        else {
                            System.out.println("Aucun pilier en dessous");
                        }
                    }
                //On met les blocs aux bonnes positions
                layerSurface.setCell(xBloc, yBloc+1,null);
                layerSurface.setCell(xBlocCible, yBlocCible+1, cell);
                yBloc++;
                }
            }
        }
    }        
 
    
    /**
     * Détruit un bloc à la position passée
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void destructionBloc(int x, int y) {  
        final int xBloc = x;
        final int yBloc = y;

        final Vector2 positionLancement = mineur.getPosition().cpy();
        // Faudrait lamper vers le bloc ou il va
        // Commencement du minage
        mineur.setEtatMineur(Etat.Miner);
        int dureeMinage = calculDureeMinage();
        //System.out.println("Durée minage : " + dureeMinage);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() { // Fin du minage
                Vector2 positionLorsDuCassage = mineur.getPosition().cpy();
                if(isCellSurfaceHere(xBloc, yBloc) && mineur.isMineurAuSol() && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {
                    int idBlock = (Integer) layerSurface.getCell(xBloc, yBloc).getTile().getId();
                    int idPierre = (Integer) mineur.getMap().getTileSets().getTileSet("stone.png").getProperties().get("firstgid");
                    System.out.println(mineur.getMap().getTileSets().getTileSet("stone.png") + " " + mineur.getMap().getTileSets().getTileSet("stone.png").getProperties() + " " + mineur.getMap().getTileSets().getTileSet("stone.png").getProperties().get("firstgid"));
                    int idGlow = (Integer) mineur.getMap().getTileSets().getTileSet("glowstone.png").getProperties().get("firstgid");
                    if(idBlock != idPierre && isCellSurfaceHere(xBloc, yBloc) && mineur.isMineurAuSol() && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {
                        Object idDiam =  mineur.getMap().getTileSets().getTileSet("diamond_block.png").getProperties().get("firstgid");
                        int idDiam2 = (Integer) idDiam;
                        
                    System.out.println("le mineur est"+ mineur.getMap());
                    
                        if(layerSurface.getCell(xBloc, yBloc).getTile().getId() == idDiam2) {
                            victory = true;
                        } 
                                layerSurface.setCell(xBloc, yBloc, null);
                                if (idBlock == idGlow){
                                    mineur.setHealth(mineur.getHealth()+0.2f);
                                }
                                else{
                                    mineur.setHealth(mineur.getHealth()-0.01f);
                                }
                        
                        if(isCellDessous(xBloc, yBloc)){
                            new Timer().schedule(new TimerTask(){
                                @Override
                                public void run(){
                                    pierreTombe(xBloc,yBloc);
                                }
                            }, 3000);
                        }
                    }
                }
            }
        }, dureeMinage);
    }
    
    /*
     Renvoi une durée en MS.
     Durée = 1000ms + 100 ms tout les 10 blocs
    */
    private int calculDureeMinage() {
        int profondeur = layerSurface.getHeight() - (int) mineur.getPosition().y;
        return 1000 + 100 * (profondeur/10);
    }
}