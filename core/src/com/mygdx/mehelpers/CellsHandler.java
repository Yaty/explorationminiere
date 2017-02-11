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
import com.badlogic.gdx.utils.Timer;
import com.mygdx.gameobjects.BaseIntermediaire;
import com.mygdx.gameobjects.Item;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
import com.mygdx.gameworld.GameRenderer;
import java.util.LinkedList;

/**
 * Classe gérant les cellules du jeu
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class CellsHandler {
    private boolean victory;
    private final Mineur mineur;
    private TiledMapTileLayer layerSurface, layerObjets;
    private final boolean[] cellsSAM; // CellsSurfaceAroundMineur
    public static int idPierre, idDiamant, idCharbon, idTerre, idEmeraude,
        idGlowstone, idOr, idHerbe, idFer, idLapis, idEchelle, idPilier,
        idTNT, idMagasin, idSolBase, idLave, idFog;
    private final TiledMapTileSets tileSets;
    private final int rayonTNT = 1; 
    private LinkedList<BaseIntermediaire> bases;
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
        bases = new LinkedList<BaseIntermediaire>();
        tileSets = mineur.getMap().getTileSets();
        idPierre = (Integer) tileSets.getTileSet("stone.png").getProperties().get("firstgid");
        idDiamant = (Integer) tileSets.getTileSet("diamond_block.png").getProperties().get("firstgid");
        idCharbon = (Integer) tileSets.getTileSet("coal_ore.png").getProperties().get("firstgid");
        idTerre = (Integer) tileSets.getTileSet("dirt.png").getProperties().get("firstgid");
        idEmeraude = (Integer) tileSets.getTileSet("emerald_ore.png").getProperties().get("firstgid");
        idGlowstone = (Integer) tileSets.getTileSet("glowstone.png").getProperties().get("firstgid");
        idOr = (Integer) tileSets.getTileSet("gold_ore.png").getProperties().get("firstgid");
        idHerbe = (Integer) tileSets.getTileSet("grass_side.png").getProperties().get("firstgid");
        idFer = (Integer) tileSets.getTileSet("iron_ore.png").getProperties().get("firstgid");
        idLapis = (Integer) tileSets.getTileSet("lapis_ore.png").getProperties().get("firstgid");
        idEchelle = (Integer) tileSets.getTileSet("ladder.gif").getProperties().get("firstgid");
        idPilier = (Integer) tileSets.getTileSet("pilier.gif").getProperties().get("firstgid");
        idTNT = (Integer) tileSets.getTileSet("tnt.png").getProperties().get("firstgid");
        idMagasin = (Integer) tileSets.getTileSet("magasin.gif").getProperties().get("firstgid");
        idSolBase = (Integer) tileSets.getTileSet("solBase.png").getProperties().get("firstgid");
        idLave = (Integer) tileSets.getTileSet("lave.gif").getProperties().get("firstgid");
        idFog = (Integer) tileSets.getTileSet("fog.gif").getProperties().get("firstgid");
    }
    
    public void reload() {
        layerSurface = (TiledMapTileLayer) mineur.getMap().getLayers().get("surface");
        layerObjets =  (TiledMapTileLayer) mineur.getMap().getLayers().get("objets");
        bases = new LinkedList<BaseIntermediaire>();
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
            if(distanceADroite < 0.1 && distanceADroite > 0) cellsSAM[1] = true;
        }
        else
            cellsSAM[1] = false;
        
        cellsSAM[2] = layerSurface.getCell(x, y-1)!=null;
        
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
        if(!isLadderHere(x,y) && mineur.getInventaire().checkInventory(Item.ECHELLE) > 0){
            Cell cell = new Cell();
            cell.setTile(tileSets.getTile(idEchelle));
            layerObjets.setCell(x, y, cell);
            mineur.getInventaire().remove(Item.ECHELLE, 1);
        }else if(isLadderHere(x,y)){
            layerObjets.setCell(x, y, null);
            mineur.getInventaire().store(Item.ECHELLE, 1);
        }
    }
    
    public void setPilier(int x, int y){
        if(mineur.getInventaire().checkInventory(Item.PILIER) > 0) {
            Cell cell = new Cell();
            cell.setTile(tileSets.getTile(idPilier));
            layerObjets.setCell(x, y, cell);
            mineur.getInventaire().remove(Item.PILIER, 1);
        }
    }
    
    public void ramassePilier(int x, int y){
        layerObjets.setCell(x,y,null);
    }
    
    public void setTNT(int x, int y){
        if(mineur.getInventaire().checkInventory(Item.TNT) > 0) {
            Cell cell = new Cell();
            cell.setTile(tileSets.getTile(idTNT));
            if(!isCellSurfaceHere(x+1, y)) {
                layerObjets.setCell(x+1, y, cell);
                mineur.getInventaire().remove(Item.TNT, 1);
            }
        }
    }
    
    // Gestion de l'explosion de la TNT.
    // On applique un timer pour laisser au robot le temps de fuir avant d'exploser
    public void makeTNTexplode(final int x, final int y){
        if(getObject(x, y)==idTNT) {
            new Timer().scheduleTask(new Timer.Task(){
                                @Override
                                public void run(){
                                    explodeTNT(x, y);
                                }
                            }, 2.5f);
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
                    if(getBloc(x+i, y+j)==idDiamant) {
                        victory = true;
                        return;
                    }
                    else layerSurface.setCell(x+i, y+j, null);
                }
                else explodeTNT(x+i, y+j);
            }  
        }
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
    
    private boolean isBlocAuDessus(int xBloc, int yBloc, int idBloc){
        int xCellUp = xBloc;
        int yCellUp = yBloc+1;
        return getObject(xCellUp, yCellUp) == idBloc || getBloc(xCellUp, yCellUp) == idBloc;
    }
    
    
    /**
     * Fais tomber une ou plusieurs pierres sur 1 ou plusieur blocs de haut s'il y a rien en dessous.
     * @param xBloc l'entier en abscisse
     * @param yBloc l'entier en ordonnée
     */
    private void faireTomberUnBlocSurfaceDeCoord(int xBloc, int yBloc, int idBloc){ 
        Cell cell = new Cell();
        cell.setTile(tileSets.getTile(idBloc));
        // Tant qu'il y a un bloc a faire tomber
        while(getBloc(xBloc, yBloc) == idBloc){
            int yBlocCible = yBloc-1;

            // Tant qu'il y a du vide en dessous
            int posYtmp = yBloc;
            while(getBloc(xBloc, yBlocCible) == 0 && getObject(xBloc, yBlocCible) != idPilier) {
                if(getObject(xBloc, yBlocCible) != 0 && getObject(xBloc, yBlocCible) != idPilier) {
                    layerObjets.setCell(xBloc, yBlocCible, null);
                }
                //On met les blocs aux bonnes positions
                layerSurface.setCell(xBloc, posYtmp, null);
                layerSurface.setCell(xBloc, yBlocCible, cell);
                if((int)mineur.getPosition().x == xBloc && (int)mineur.getPosition().y == yBlocCible) {
                    mineur.setHealth(-1f);
                }                
                posYtmp = yBlocCible;
                yBlocCible--;
            }
            yBloc++; // On passe au bloc supérieur
        }
    }
    
    // Check si il y a une pierre au dessus, si oui on appele pierre tombe
    private void faireTomberUnBlocObjetDeCoord(int xBloc, int yBloc, int idBloc){
        Cell cell = new Cell();
        cell.setTile(tileSets.getTile(idBloc));
        // Tant qu'il y a un bloc a faire tomber
        while(getObject(xBloc, yBloc) == idBloc){
            int yBlocCible = yBloc-1;

            // Tant qu'il y a du vide en dessous
            int posYtmp = yBloc;
            while(getObject(xBloc, yBlocCible) == 0 && getBloc(xBloc, yBlocCible) == 0) {
                //On met les blocs aux bonnes positions
                if(getBloc(xBloc, posYtmp+1) == idPierre) faireTomberUnBlocSurfaceDeCoord(xBloc, posYtmp, idPierre);
                layerObjets.setCell(xBloc, posYtmp, null);
                layerObjets.setCell(xBloc, yBlocCible, cell);
                posYtmp = yBlocCible;
                yBlocCible--;
            }
            yBloc++; // On passe au bloc supérieur
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
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() { // Fin du minage
                Vector2 positionLorsDuCassage = mineur.getPosition().cpy();
                if(isCellSurfaceHere(xBloc, yBloc) && mineur.isMineurAuSol() && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {
                    int idBlock = (Integer) getBloc(xBloc, yBloc);
                    if(idBlock != idPierre && idBlock != idSolBase && idBlock != idMagasin) {                
                        if(idBlock == idDiamant) {
                            victory = true;
                            return;
                        }
                        
                        mineur.gestionArgent(idBlock);
                        layerSurface.setCell(xBloc, yBloc, null);
                        
                        if (idBlock == idGlowstone) mineur.setHealth(mineur.getHealth()+0.2f);
                        else mineur.setHealth(mineur.getHealth()-0.01f);       
                        
                        if(isBlocAuDessus(xBloc, yBloc, idPilier)){
                            new Timer().scheduleTask(new Timer.Task(){
                                @Override
                                public void run() {
                                    faireTomberUnBlocObjetDeCoord(xBloc, yBloc+1, idPilier);
                                }
                            }, 1);
                        } else if (isBlocAuDessus(xBloc, yBloc, idPierre)) {
                            new Timer().scheduleTask(new Timer.Task(){
                                @Override
                                public void run(){
                                    faireTomberUnBlocSurfaceDeCoord(xBloc, yBloc+1, idPierre);
                                }
                            }, 2.5f);
                        }
                    }
                }
            }
        }, dureeMinage);
    }
 
    /*
     Renvoi une durée en s.
     Durée = 1s + 0.1s tout les 10 blocs / la vitesse de la pioche
    */
    private int calculDureeMinage() {
        int profondeur = layerSurface.getHeight() - (int) mineur.getPosition().y;
        return (int) ((1 + 0.1 * (profondeur/10)) / mineur.getEquipement().getSlots().get(0).getItem().getParam()); // On ajoute la vitesse de la pioche, la cast arrondit au millième -> pas génant
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
    
    public boolean coordIsInMap(int x, int y) {
        return x >= 0 && x <= mineur.getMap().getProperties().get("width", Integer.class)-1 && y >= 0 && x <= mineur.getMap().getProperties().get("height", Integer.class)-1;
    }
    private boolean isBaseGenerable(int x, int y) {
        BaseIntermediaire baseTmp = new BaseIntermediaire(x, y);
        int xFin = x + (int) baseTmp.width - 1;
        int yFin = y + (int) baseTmp.height - 2;
        return layerSurface.getCell(xFin, yFin) != null;
    }
    
    // X et y = position du mineur ou il a appuyé
    public void genererBase(int x, int y) {
        if(mineur.getInventaire().checkInventory(Item.BASE) > 0 && isBaseGenerable(x, y)) {
            BaseIntermediaire base = new BaseIntermediaire(x, y);
            int x2 = x;
            y--; // pour faire genere la base une case plus bas (pour que le mineur soit à l'entrée.
            for(int i = 0 ; i < base.height ; i++) {
                for(int j = 0 ; j < base.width ; j++) {
                    if(layerSurface.getCell(x2, y) != null) {
                        if(layerSurface.getCell(x2, y).getTile().getId() == idDiamant) {
                            victory = true;
                            return;
                        }
                    }
                    Cell cell = new Cell();
                    TiledMapTile tile = tileSets.getTile(BaseIntermediaire.CELLS[i][j]);
                    cell.setTile(tile);
                    if(tile != null && tile.getId() == idMagasin) { // Si magasin
                        layerSurface.setCell(x2, y, null);
                        layerObjets.setCell(x2, y, cell);
                    } else if (tile != null) { // Si non vide
                        layerSurface.setCell(x2, y, cell);
                    } else { // Le reste (le vide)
                        layerSurface.setCell(x2, y, null);
                        layerObjets.setCell(x2, y, null);
                    }
                    x2++;
                }
                x2 = x;
                y++;
            }
            mineur.getInventaire().remove(Item.BASE, 1);
            bases.add(base);
            GameRenderer.setTist(bases);
        }
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

    /**
     *
     * @deprecated performance issue
     */
    @Deprecated
    public void faireTomberBlocEnSuspension() {
        final int hauteur = mineur.getMap().getProperties().get("width", Integer.class);
        final int largeur = mineur.getMap().getProperties().get("height", Integer.class);
        
        for(int i = 0 ; i < largeur ; i++) {
            for(int j = 0 ; j < hauteur ; j++) {
                final Cell blocSurface = layerSurface.getCell(i, j);
                final Cell blocObjet = layerObjets.getCell(i, j);
                System.out.println(i + " " + j);
                if(blocSurface != null) {
                    final int idBlocSurface = blocSurface.getTile().getId();
                    if(idBlocSurface == idPierre) faireTomberUnBlocSurfaceDeCoord(i, j, idBlocSurface);
                } else if(blocObjet != null) {
                    final int idBlocObjet = blocObjet.getTile().getId();
                    if(idBlocObjet == idPilier || idBlocObjet == idTNT || idBlocObjet == idLave) faireTomberUnBlocObjetDeCoord(i, j, idBlocObjet);
                }
            }
        }
    }

    public void dispose() {}
    
}