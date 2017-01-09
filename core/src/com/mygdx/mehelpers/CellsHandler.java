/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.BaseIntermediaire;
import com.mygdx.gameobjects.Item;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
import com.mygdx.gameworld.GameRenderer;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe gérant les cellules du jeu
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class CellsHandler {
    private boolean victory;
    private final Mineur mineur;
    private TiledMapTileLayer layerSurface, layerObjets;
    private final boolean[] cellsSAM; // CellsSurfaceAroundMineur
    private final int idPierre, idDiamant, idCharbon, idTerre, idEmeraude, idGlowstone, idOr, idHerbe, idFer, idLapis, idEchelle, idPilier, idTNT, idMagasin;
    private final TiledMapTileSets tileSet;
    private final int rayonTNT = 2; 
    private final LinkedList<BaseIntermediaire> bases;
    private final int HAUTEUR_SURFACE = 3;
    
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
        bases = new LinkedList();
        tileSet = mineur.getMap().getTileSets();
        idPierre = (Integer) mineur.getMap().getTileSets().getTileSet("stone.png").getProperties().get("firstgid");
        idDiamant = (Integer) mineur.getMap().getTileSets().getTileSet("diamond_block.png").getProperties().get("firstgid");
        idCharbon = (Integer) mineur.getMap().getTileSets().getTileSet("coal_ore.png").getProperties().get("firstgid");
        idTerre = (Integer) mineur.getMap().getTileSets().getTileSet("dirt.png").getProperties().get("firstgid");
        idEmeraude = (Integer) mineur.getMap().getTileSets().getTileSet("emerald_ore.png").getProperties().get("firstgid");
        idGlowstone = (Integer) mineur.getMap().getTileSets().getTileSet("glowstone.png").getProperties().get("firstgid");
        idOr = (Integer) mineur.getMap().getTileSets().getTileSet("gold_ore.png").getProperties().get("firstgid");
        idHerbe = (Integer) mineur.getMap().getTileSets().getTileSet("grass_side.png").getProperties().get("firstgid");
        idFer = (Integer) mineur.getMap().getTileSets().getTileSet("iron_ore.png").getProperties().get("firstgid");
        idLapis = (Integer) mineur.getMap().getTileSets().getTileSet("lapis_ore.png").getProperties().get("firstgid");
        idEchelle = (Integer) mineur.getMap().getTileSets().getTileSet("ladder.gif").getProperties().get("firstgid");
        idPilier = (Integer) mineur.getMap().getTileSets().getTileSet("pilier.gif").getProperties().get("firstgid");
        idTNT = (Integer) mineur.getMap().getTileSets().getTileSet("tnt.png").getProperties().get("firstgid");
        idMagasin = (Integer) mineur.getMap().getTileSets().getTileSet("magasin.gif").getProperties().get("firstgid");
    }
    
    public void reload() {
        layerSurface = (TiledMapTileLayer) mineur.getMap().getLayers().get("surface");
        layerObjets =  (TiledMapTileLayer) mineur.getMap().getLayers().get("objets");        
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
        if(layerObjets.getCell(x, y) != null){
            return layerObjets.getCell(x,y).getTile().getId() == idEchelle;
        }
        return false;
    }
    
    
    /**
     * Ajoute une échelle au coordonnée mis en paramètre
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void setLadder(int x,int y){
        System.out.println("je suis dans setLadder");
        if(!isLadderHere(x,y) && mineur.getInventaire().checkInventory(Item.ECHELLE) > 0){
            Cell cell = new Cell();
            cell.setTile(tileSet.getTile(idEchelle));
            layerObjets.setCell(x, y, cell);
            mineur.getInventaire().remove(Item.ECHELLE, -1);
        }else if(isLadderHere(x,y)){
            System.out.println("Il y a une echelle");
            layerObjets.setCell(x, y, null);
            mineur.getInventaire().remove(Item.ECHELLE, +1);

        }
    }
    
    
    
    public void setPilier(int x, int y){
        if(mineur.getInventaire().checkInventory(Item.PILIER) > 0) {
            Cell cell = new Cell();
            cell.setTile(tileSet.getTile(idPilier));
            if(mineur.isTeteVersLaDroite() && !isCellSurfaceHere(x+1, y)) {
                layerObjets.setCell(x+1, y, cell);
                mineur.getInventaire().remove(Item.PILIER, 1);
            } else if(!mineur.isTeteVersLaDroite() && !isCellSurfaceHere(x-1,y)) {
                layerObjets.setCell(x-1, y, cell);
                mineur.getInventaire().remove(Item.PILIER, 1);
            }
        }
    }
    
    public void ramassePilier(int x, int y){
        layerObjets.setCell(x,y,null);
    }
    
    public void setTNT(int x, int y){
        if(mineur.getInventaire().checkInventory(Item.TNT) > 0) {
            Cell cell = new Cell();
            cell.setTile(tileSet.getTile(idTNT));
            if(mineur.isTeteVersLaDroite() && !isCellSurfaceHere(x+1, y)) {
                layerObjets.setCell(x+1, y, cell);
                mineur.getInventaire().remove(Item.TNT, 1);
            } else if(!mineur.isTeteVersLaDroite() && !isCellSurfaceHere(x-1,y)) {
                layerObjets.setCell(x-1, y, cell);
                mineur.getInventaire().remove(Item.TNT, 1);
            }
        }
    }
    
    // Gestion de l'explosion de la TNT.
    // Si on regarde à gauche on fait péter la tnt à gauche
    // Si on regarde à droite on fait péter la tnt à droite
    // On applique un timer pour laisser au robot le temps de fuir avant d'exploser
    public void makeTNTexplode(int x, int y){
        if(mineur.isTeteVersLaDroite() && getObject(x+1, y)==idTNT) {
            final int x2 = x + 1;
            final int y2 = y;
            
            new Timer().schedule(new TimerTask(){
                                @Override
                                public void run(){
                                    explodeTNT(x2, y2);
                                }
                            }, 2500);
        } else if(!mineur.isTeteVersLaDroite() && getObject(x-1, y)==idTNT) {
            final int x2 = x - 1;
            final int y2 = y;
            
            new Timer().schedule(new TimerTask(){
                                @Override
                                public void run(){
                                    explodeTNT(x2, y2);
                                }
                            }, 2500);
        }
        
    }
    
    // On parcours tout le carré de "rayon" rayonTNT
    // On supprime tous les blocs dans ce carré
    // Si un des blocs est une TNT, on appelle cette 
    // méthode avec les coordonnées de cette TNT.
    
    public void explodeTNT(final int x, final int y){
        for(int i = -rayonTNT ; i <= rayonTNT; i++ ){
            for(int j = -rayonTNT ; j <= rayonTNT ; j++){
                if(getObject(x+i, y+j)!=idTNT ||(i == 0 && j == 0)){
                    if((int)mineur.getPosition().x == (x+i) && (int)mineur.getPosition().y == (y+j)) 
                        mineur.setHealth(0f);
                    layerObjets.setCell(x+i, y+j, null);
                    if(getBloc(x+i, y+j)==idDiamant) 
                        victory = true;
                    else layerSurface.setCell(x+i, y+j, null);
                }
                else explodeTNT(x+i, y+j);
            }  
        }
        
        
        /*int N = 2*rayonTNT+1;
 
        int xrec, yrec;  // Coordinates inside the rectangle

        // Draw a square of size N*N.
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                // Start from the left most corner point
                xrec = i-rayonTNT;
                yrec = j-rayonTNT;

                // If this point is inside the circle, print it
                if (xrec*xrec + yrec*yrec <= rayonTNT*rayonTNT+1 ){
                    if(getObject(x+i, y+j)!=idTNT ||(i == 0 && j == 0)){
                        layerSurface.setCell(x+i, y+j, null);
                        layerObjets.setCell(x+i, y+j, null);
                    }else{
                        explodeTNT(x+i, y+j);
                    }
                }
            }
        }*/
    }
    
    
    /**
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     * @return vrai si cell présente en x et y, faux sinon
     */
    public boolean isCellSurfaceHere(int x, int y) {
        return layerSurface.getCell(x, y) != null;
    }
    
    public boolean isCellObjectHere(int x, int y){
        return layerObjets.getCell(x, y) == null;
    }
    
    
    private boolean isCellDessousPilier(int xBloc, int yBloc){
        int xCellUp = xBloc;
        int yCellUp = yBloc+1;
        return getObject(xCellUp, yCellUp) == idPilier;
    }
    
    /**
     * Fais tomber une ou plusieurs pierres sur 1 ou plusieur blocs de haut s'il y a rien en dessous.
     * @param xBloc l'entier en abscisse
     * @param yBloc l'entier en ordonnée
     */
    private void pierreTombe(int xBloc, int yBloc){ 
        Cell cell = new Cell();
        cell.setTile(tileSet.getTile(idPierre));
        //On parcourt les blocs de bas en haut tant que ce sont des blocs de pierre        
        while(getBloc(xBloc, yBloc+1) == idPierre){
            int yBlocCible = yBloc;
            int xBlocCible = xBloc;
            //On parcourt les blocs de haut vers le bas tant que le bloc cible est vide 
            while(getBloc(xBlocCible, yBlocCible) == 0){
                System.out.println("Mineur : " + (int)mineur.getPosition().x + " " + (int)mineur.getPosition().y);
                System.out.println("BlocCible : " + xBlocCible + " " + yBlocCible);   
                if((int)mineur.getPosition().x == xBlocCible && (int)mineur.getPosition().y == yBlocCible) 
                    mineur.setHealth(0f);
                yBlocCible--;
                if(getObject(xBlocCible, yBlocCible) == idPilier){
                    break;
                }

            //On met les blocs aux bonnes positions
            layerSurface.setCell(xBloc, yBloc+1,null);
            layerSurface.setCell(xBlocCible, yBlocCible+1, cell);
            yBloc++;
            }
        }
    }
    
    private void pillierTombe(int xBloc, int yBloc){
        Cell cell = new Cell();
        cell.setTile(tileSet.getTile(idPilier));
        while(getObject(xBloc, yBloc+1)==idPilier){
            int yBlocCible = yBloc;
            int xBlocCible = xBloc;

            //while(getObject(xBlocCible, yBlocCible+1) != 0){
                yBlocCible --;
                if(getObject(xBlocCible, yBlocCible+1) != 0){
                    break;
                }
                layerObjets.setCell(xBloc, yBloc+1,null);
                layerObjets.setCell(xBlocCible, yBlocCible+1, cell);
                yBloc++;
            //}
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
                    int idBlock = (Integer) getBloc(xBloc, yBloc);
                    if(idBlock != idPierre && isCellSurfaceHere(xBloc, yBloc) && mineur.isMineurAuSol() && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {                
                        if(getBloc(xBloc, yBloc) == idDiamant) victory = true;
                        mineur.gestionArgent(getBloc(xBloc, yBloc));
                        layerSurface.setCell(xBloc, yBloc, null);
                        if (idBlock == idGlowstone) mineur.setHealth(mineur.getHealth()+0.2f);
                        else mineur.setHealth(mineur.getHealth()-0.01f);                      
                        if(isCellDessousPilier(xBloc, yBloc)){
                            System.out.println("trace3");
                            pillierTombe(xBloc, yBloc);
                        }else{
                            new Timer().schedule(new TimerTask(){
                                @Override
                                public void run(){
                                    pierreTombe(xBloc,yBloc);
                                }
                            }, 2500);
                        }
                    }
                }
            }
        }, dureeMinage);
    }
 
    /*
     Renvoi une durée en MS.
     Durée = 1000ms + 100 ms tout les 10 blocs / la vitesse de la pioche
    */
    private int calculDureeMinage() {
        int profondeur = layerSurface.getHeight() - (int) mineur.getPosition().y;
        return (int) ((1000 + 100 * (profondeur/10)) / mineur.getEquipement().getSlots().get(0).getItem().getParam()); // On ajoute la vitesse de la pioche, la cast arrondit au millième -> pas génant
    }

    public int getIdPierre() {
        return idPierre;
    }
    
    public int getIdDiamant() {
        return idDiamant;
    }

    public int getIdCharbon() {
        return idCharbon;
    }

    public int getIdTerre() {
        return idTerre;
    }

    public int getIdEmeraude() {
        return idEmeraude;
    }

    public int getIdGlowstone() {
        return idGlowstone;
    }

    public int getIdOr() {
        return idOr;
    }

    public int getIdHerbe() {
        return idHerbe;
    }

    public int getIdFer() {
        return idFer;
    }

    public int getIdLapis() {
        return idLapis;
    }

    public int getIdEchelle() {
        return idEchelle;
    }

    public int getIdPilier() {
        return idPilier;
    }
    
    public int getIdTNT(){
        return idTNT;
    }
    
    public int getIdMagasin() {
        return idMagasin;
    }

    public void setVictory(boolean b) {
        this.victory = b;
    }
    
    public int getBloc(int xBloc,int yBloc){
        if (layerSurface.getCell(xBloc, yBloc) != null){
            if(layerSurface.getCell(xBloc, yBloc).getTile() != null){
                return layerSurface.getCell(xBloc, yBloc).getTile().getId();
            }
            else return 0;
        }
        else return 0;
    }
    
    public int getObject(int xBloc,int yBloc){
        if (layerObjets.getCell(xBloc, yBloc) != null){
            if(layerObjets.getCell(xBloc, yBloc).getTile() != null){
                return layerObjets.getCell(xBloc, yBloc).getTile().getId();
            }
            else return 0;
        }
        else return 0;
    }
    
    private boolean tileIsInMap(int x, int y) {
        return x >= 0 && x <= mineur.getMap().getProperties().get("width", Integer.class)-1 && y >= 0 && x <= mineur.getMap().getProperties().get("height", Integer.class)-1;
    }

    public void genererBase(int x, int y) {
        if(mineur.getInventaire().checkInventory(Item.MAGASIN) > 0) {
            // Zone de 6*3
            for(int i = x-(BaseIntermediaire.LARGEUR/2) ; i <= x+(BaseIntermediaire.LARGEUR/2) ; i++) {
                for(int j = y ; j < y + BaseIntermediaire.HAUTEUR ; j++)
                    layerSurface.setCell(i, j, null);
            }
            genererMagasin(x, y);
            bases.add(new BaseIntermediaire(x-3, y));
            GameRenderer.setTist(bases);
        }
    }
        
    private void genererMagasin(int x, int y) {      
        setCellLayerObjet(x, y, tileSet.getTile(idMagasin), Item.MAGASIN);
    }
    
    private void setCellLayerSurface(int x, int y, TiledMapTile tile, Item item) {
        if(tile != null && mineur.getInventaire().firstSlotWithItem(item).getAmount() > 0) {
            Cell cell = new Cell();
            cell.setTile(tile);
            layerSurface.setCell(x, y, cell);
            mineur.getInventaire().remove(item, 1);
        }
    }
    
    private void setCellLayerObjet(int x, int y, TiledMapTile tile, Item item) {
        if(tile != null && mineur.getInventaire().firstSlotWithItem(item).getAmount() > 0) {
            Cell cell = new Cell();
            cell.setTile(tile);
            layerObjets.setCell(x, y, cell);
            mineur.getInventaire().remove(item, 1);
        }
    }
    
    private void setCellLayerObjet(int x, int y, TiledMapTile tile) {
        if(tile != null) {
            Cell cell = new Cell();
            cell.setTile(tile);
            layerObjets.setCell(x, y, cell);
        }
    }
    
    private void setCellLayerSurface(int x, int y, TiledMapTile tile) {
        if(tile != null) {
            Cell cell = new Cell();
            cell.setTile(tile);
            layerSurface.setCell(x, y, cell);
        }
    }

    public boolean isMineurInBase() {
        for(BaseIntermediaire base : bases) {
            if(base.contains(mineur.getPosition().x, mineur.getPosition().y))
                return true;
        }
        return false;
    }

    public boolean isMineurInSurface() {
        return layerSurface.getHeight()- mineur.getPosition().y <= HAUTEUR_SURFACE;
    }

    public BaseIntermediaire getBaseById(int idSelect) {
        return bases.get(idSelect);
    }
    
}