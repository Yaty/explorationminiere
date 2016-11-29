/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Item;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
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
    private final int idPierre, idDiamant, idCharbon, idTerre, idEmeraude, idGlowstone, idOr, idHerbe, idFer, idLapis, idEchelle, idPilier,idTNT;
    private final TiledMapTileSets tileSet;
    private final int rayonTNT = 2; 
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
        if(!isLadderHere(x,y)){
            Cell cell = new Cell();
            cell.setTile(tileSet.getTile(idEchelle));
            layerObjets.setCell(x, y, cell);
            mineur.getInventaire().store(Item.ECHELLE, 1);
        }else{
            layerObjets.setCell(x, y, null);

        }
    }
    
    public void setPilier(int x, int y){
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
    
    public void ramassePilier(int x, int y){
        layerObjets.setCell(x,y,null);
    }
    
    public void setTNT(int x, int y){
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
    
    public void explodeTNT(final int x, final int y){
        for(int i = -rayonTNT ; i <= rayonTNT; i++ ){
            for(int j = -rayonTNT ; j <= rayonTNT ; j++){
                if(getObject(x+i, y+j)!=idTNT ||(i == 0 && j == 0)){
                    layerObjets.setCell(x+i, y+j, null);
                    if(getBloc(x+i, y+j)==idDiamant) victory = true;
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
    private boolean isCellSurfaceHere(int x, int y) {
        return layerSurface.getCell(x, y) != null;
    }
    
    public boolean isCellObjectHere(int x, int y){
        return layerObjets.getCell(x, y) == null;
    }
    
    
    private boolean isCellDessousPilier(int xBloc, int yBloc){
        int xCellUp = xBloc;
        int yCellUp = yBloc+1;
        if(layerObjets.getCell(xCellUp, yCellUp) != null && layerObjets.getCell(xCellUp, yCellUp).getTile() != null) {
            if(layerObjets.getCell(xCellUp, yCellUp).getTile().getId() == idPilier){
                    return true;
            }
        }
        return false;
    }
    
    private boolean isCellDessous(int xBloc, int yBloc){
        int xCellUp = xBloc;
        int yCellUp = yBloc+1;
        if(layerSurface.getCell(xCellUp, yCellUp) != null && layerSurface.getCell(xCellUp, yCellUp).getTile() != null) {
            if(layerObjets.getCell(xCellUp, yCellUp).getTile().getId() == idPilier){
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Fais tomber une ou plusieurs pierres sur 1 ou plusieur blocs de haut s'il y a rien en dessous.
     * @param xBloc l'entier en abscisse
     * @param yBloc l'entier en ordonnée
     */
    private void pierreTombe(int xBloc, int yBloc){ 
        Cell cell = new Cell();
        cell.setTile(tileSet.getTile(idPierre));
        //On parcourt les blocs des bas en haut tant que ce sont des blocs de pierre        
        if(layerSurface.getCell(xBloc, yBloc+1).getTile() != null && layerSurface.getCell(xBloc, yBloc+1) != null){
            while(layerSurface.getCell(xBloc, yBloc+1).getTile().getId() == idPierre){
                int yBlocCible = yBloc;
                int xBlocCible = xBloc;
                //On parcourt les blocs de haut vers le bas tant que le bloc cible est vide 
                while(layerSurface.getCell(xBlocCible, yBlocCible) == null){
                    yBlocCible--;
                    if(layerObjets.getCell(xBlocCible, yBlocCible+1) != null){
                        if(layerObjets.getCell(xBlocCible, yBlocCible).getTile().getId() == idPilier){
                            break;
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
    
    private void pillierTombe(int xBloc, int yBloc){
        Cell cell = new Cell();
        cell.setTile(tileSet.getTile(idPilier));
        //System.out.println(layerObjets.getCell(xBloc, yBloc+1));
        if(layerObjets.getCell(xBloc, yBloc+1).getTile() != null && layerObjets.getCell(xBloc, yBloc+1)!= null){
            while(layerObjets.getCell(xBloc, yBloc+1).getTile().getId()==idPilier){
                int yBlocCible = yBloc;
                int xBlocCible = xBloc;
                
                while(layerObjets.getCell(xBlocCible, yBlocCible+1) != null){
                    yBlocCible --;
                    if(layerSurface.getCell(xBlocCible, yBlocCible+1) != null){
                        break;
                    }
                    layerObjets.setCell(xBloc, yBloc+1,null);
                    layerObjets.setCell(xBlocCible, yBlocCible+1, cell);
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
                    if(idBlock != idPierre && isCellSurfaceHere(xBloc, yBloc) && mineur.isMineurAuSol() && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {                
                        if(layerSurface.getCell(xBloc, yBloc).getTile().getId() == idDiamant) victory = true;
                        mineur.gestionArgent(layerSurface.getCell(xBloc, yBloc).getTile().getId());
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
     Durée = 1000ms + 100 ms tout les 10 blocs * la vitesse de la pioche
    */
    private int calculDureeMinage() {
        int profondeur = layerSurface.getHeight() - (int) mineur.getPosition().y;
        return (int) (1000 + 100 * (profondeur/10) * mineur.getInventaire().getPioche().getVitesse()); // On ajoute la vitesse de la pioche, la cast arrondit au millième -> pas génant
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
    
}