/* 
 * Copyright 2017 
 * - Hugo Da Roit - Benjamin Lévêque
 * - Alexis Montagne - Alexis Clément
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.gameobjects.Base;
import com.mygdx.gameobjects.minerobjects.Item;
import com.mygdx.gameobjects.Miner;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.minexploration.MEGame;
import java.util.LinkedList;

/**
 * Map handler
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class MapHandler implements Handler {
    private TiledMap map;
    private HealthHandler healthHandler;
    private Miner miner;
    private TiledMapTileLayer layerSurface, layerObjets;
    public static int idStone, idDiamond, idCoal, idDirt, idEmerald,
        idGlowstone, idGold, idGrass, idIron, idLapis, idLadder, idPillar,
        idTNT, idStore, idBaseGround, idLava, idFog;
    private final int HEIGHT_SURFACE = 3, RADIUS_TNT = 3;
    private TiledMapTileSets tileSets;
    private LinkedList<Base> bases;
    
    /**
     * Constructor
     * @param map the map
     * @param miner the miner
     */
    public MapHandler(TiledMap map, Miner miner) {
        this.miner = miner;
        this.map = map;
        
        bases = new LinkedList<Base>();
        
        layerSurface = (TiledMapTileLayer) map.getLayers().get("surface");
        layerObjets =  (TiledMapTileLayer) map.getLayers().get("objets");
        tileSets = map.getTileSets();
        idStone = (Integer) tileSets.getTileSet("stone.png").getProperties().get("firstgid");
        idDiamond = (Integer) tileSets.getTileSet("diamond_block.png").getProperties().get("firstgid");
        idCoal = (Integer) tileSets.getTileSet("coal_ore.png").getProperties().get("firstgid");
        idDirt = (Integer) tileSets.getTileSet("dirt.png").getProperties().get("firstgid");
        idEmerald = (Integer) tileSets.getTileSet("emerald_ore.png").getProperties().get("firstgid");
        idGlowstone = (Integer) tileSets.getTileSet("glowstone.png").getProperties().get("firstgid");
        idGold = (Integer) tileSets.getTileSet("gold_ore.png").getProperties().get("firstgid");
        idGrass = (Integer) tileSets.getTileSet("grass_side.png").getProperties().get("firstgid");
        idIron = (Integer) tileSets.getTileSet("iron_ore.png").getProperties().get("firstgid");
        idLapis = (Integer) tileSets.getTileSet("lapis_ore.png").getProperties().get("firstgid");
        idLadder = (Integer) tileSets.getTileSet("ladder.gif").getProperties().get("firstgid");
        idPillar = (Integer) tileSets.getTileSet("pilier.gif").getProperties().get("firstgid");
        idTNT = (Integer) tileSets.getTileSet("tnt.png").getProperties().get("firstgid");
        idStore = (Integer) tileSets.getTileSet("magasin.gif").getProperties().get("firstgid");
        idBaseGround = (Integer) tileSets.getTileSet("solBase.png").getProperties().get("firstgid");
        idLava = (Integer) tileSets.getTileSet("lave.gif").getProperties().get("firstgid");
        idFog = (Integer) tileSets.getTileSet("fog.gif").getProperties().get("firstgid");
    }
    
    /**
     * Set the health handler
     * @param healthHandler
     */
    public void setHealthHandler(HealthHandler healthHandler) {
        this.healthHandler = healthHandler;
    }
    
    /**
     * Reload objects
     */
    public void reload() {
        layerSurface = (TiledMapTileLayer) map.getLayers().get("surface");
        layerObjets =  (TiledMapTileLayer) map.getLayers().get("objets");
        bases = new LinkedList<Base>();
    }
    
    private void playerCommands() {
        if(InputHandler.EXPLODE_TNT) {
            makeTNTexplode((int) miner.getPosition().x, (int) miner.getPosition().y);
            InputHandler.EXPLODE_TNT = false;
        }
        if(InputHandler.PUT_BASE) {
            genererBase((int) miner.getPosition().x, (int) miner.getPosition().y);
            InputHandler.PUT_BASE = false;
        }
        if(InputHandler.PUT_LADDER) {
            setLadder((int) miner.getPosition().x, (int) miner.getPosition().y);
            InputHandler.PUT_LADDER = false;
        }
        if(InputHandler.PUT_PILLAR) {
            setPilier((int) miner.getPosition().x, (int) miner.getPosition().y);
            InputHandler.PUT_PILLAR = false;
        }
        if(InputHandler.PUT_TNT) {
            setTNT((int) miner.getPosition().x, (int) miner.getPosition().y);
            InputHandler.PUT_TNT = false;
        }
    }
    
    public boolean isMineurInBase() {
        for(Base base : bases) {
            if(base.contains(miner.getPosition().x, miner.getPosition().y))
                return true;
        }
        return false;
    }

    public boolean isMineurInSurface() {
        return layerSurface.getHeight()- miner.getPosition().y <= HEIGHT_SURFACE;
    }    
    
    /**
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     * @return vrai s'il y a une échelle à cette position, non sinon
     */    
    public boolean isLadderHere(int x,int y){
        if(layerObjets.getCell(x, y) != null){
            return layerObjets.getCell(x,y).getTile().getId() == idLadder;
        }
        return false;
    }
    
   /**
     * Ajoute une échelle au coordonnée mis en paramètre
     * @param x l'entier en abscisse
     * @param y l'entier en ordonnée
     */
    public void setLadder(int x,int y){
        if(!isLadderHere(x,y) && miner.getInventory().checkInventory(Item.LADDER) > 0){
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tileSets.getTile(idLadder));
            layerObjets.setCell(x, y, cell);
            miner.getInventory().remove(Item.LADDER, 1);
        }else if(isLadderHere(x,y)){
            layerObjets.setCell(x, y, null);
            miner.getInventory().store(Item.LADDER, 1);
        }
    }
    
    public void setPilier(int x, int y){
        if(miner.getInventory().checkInventory(Item.PILLAR) > 0) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tileSets.getTile(idPillar));
            layerObjets.setCell(x, y, cell);
            miner.getInventory().remove(Item.PILLAR, 1);
        }
    }
    
    public void ramassePilier(int x, int y){
        layerObjets.setCell(x,y,null);
    }
    
    public void setTNT(int x, int y){
        if(miner.getInventory().checkInventory(Item.TNT) > 0) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tileSets.getTile(idTNT));
            if(!isCellSurfaceHere(x, y)) {
                layerObjets.setCell(x, y, cell);
                miner.getInventory().remove(Item.TNT, 1);
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
    
    private void explodeTNT(final int x, final int y){
        for(int i = -RADIUS_TNT ; i <= RADIUS_TNT; i++ ){
            for(int j = -RADIUS_TNT ; j <= RADIUS_TNT ; j++){
                if(getObject(x+i, y+j)!=idTNT ||(i == 0 && j == 0)){
                    if((int)miner.getPosition().x == (x+i) && (int)miner.getPosition().y == (y+j)) 
                        healthHandler.die();
                    layerObjets.setCell(x+i, y+j, null);
                    if(getBloc(x+i, y+j)==idDiamond) {
                        MEGame.VICTORY = true;
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
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSets.getTile(idBloc));
        // Tant qu'il y a un bloc a faire tomber
        while(getBloc(xBloc, yBloc) == idBloc){
            int yBlocCible = yBloc-1;

            // Tant qu'il y a du vide en dessous
            int posYtmp = yBloc;
            while(getBloc(xBloc, yBlocCible) == 0 && getObject(xBloc, yBlocCible) != idPillar) {
                if(getObject(xBloc, yBlocCible) != 0 && getObject(xBloc, yBlocCible) != idPillar) {
                    layerObjets.setCell(xBloc, yBlocCible, null);
                }
                //On met les blocs aux bonnes positions
                layerSurface.setCell(xBloc, posYtmp, null);
                layerSurface.setCell(xBloc, yBlocCible, cell);
                if((int)miner.getPosition().x == xBloc && (int)miner.getPosition().y == yBlocCible) {
                    healthHandler.die();
                }                
                posYtmp = yBlocCible;
                yBlocCible--;
            }
            yBloc++; // On passe au bloc supérieur
        }
    }
    
    // Check si il y a une pierre au dessus, si oui on appele pierre tombe
    private void faireTomberUnBlocObjetDeCoord(int xBloc, int yBloc, int idBloc){
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSets.getTile(idBloc));
        // Tant qu'il y a un bloc a faire tomber
        while(getObject(xBloc, yBloc) == idBloc){
            int yBlocCible = yBloc-1;

            // Tant qu'il y a du vide en dessous
            int posYtmp = yBloc;
            while(getObject(xBloc, yBlocCible) == 0 && getBloc(xBloc, yBlocCible) == 0) {
                //On met les blocs aux bonnes positions
                if(getBloc(xBloc, posYtmp+1) == idStone) faireTomberUnBlocSurfaceDeCoord(xBloc, posYtmp, idStone);
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

        final Vector2 positionLancement = miner.getPosition().cpy();
        // Faudrait lamper vers le bloc ou il va
        // Commencement du minage
        Miner.state = Miner.State.MINING;
        int dureeMinage = calculDureeMinage();
        //System.out.println("Durée minage : " + dureeMinage);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() { // Fin du minage
                Vector2 positionLorsDuCassage = miner.getPosition().cpy();
                if(isCellSurfaceHere(xBloc, yBloc) && Miner.minerOnTheGround && positionLorsDuCassage.epsilonEquals(positionLancement, 0.2f)) {
                    int idBlock = (Integer) getBloc(xBloc, yBloc);
                    if(idBlock != idStone && idBlock != idBaseGround && idBlock != idStore) {                
                        if(idBlock == idDiamond) {
                            MEGame.VICTORY = true;
                            return;
                        }
                        
                        miner.collectMoney(idBlock);
                        layerSurface.setCell(xBloc, yBloc, null);
                        
                        if (idBlock == idGlowstone) healthHandler.addLife(0.2f);
                        else healthHandler.removeLife(0.2f);
                        
                        if(isBlocAuDessus(xBloc, yBloc, idPillar)){
                            new Timer().scheduleTask(new Timer.Task(){
                                @Override
                                public void run() {
                                    faireTomberUnBlocObjetDeCoord(xBloc, yBloc+1, idPillar);
                                }
                            }, 1);
                        } else if (isBlocAuDessus(xBloc, yBloc, idStone)) {
                            new Timer().scheduleTask(new Timer.Task(){
                                @Override
                                public void run(){
                                    faireTomberUnBlocSurfaceDeCoord(xBloc, yBloc+1, idStone);
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
        int profondeur = layerSurface.getHeight() - (int) miner.getPosition().y;
        return (int) ((1 + 0.1 * (profondeur/10)) / miner.getEquipment().getSlots().get(0).getItem().getParam()); // On ajoute la vitesse de la pioche, la cast arrondit au millième -> pas génant
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
        return x >= 0 && x <= map.getProperties().get("width", Integer.class)-1 && y >= 0 && x <= map.getProperties().get("height", Integer.class)-1;
    }
    private boolean isBaseGenerable(int x, int y) {
        Base baseTmp = new Base(x, y);
        int xFin = x + (int) baseTmp.width - 1;
        int yFin = y + (int) baseTmp.height - 2;
        return layerSurface.getCell(xFin, yFin) != null;
    }
    
    // X et y = position du mineur ou il a appuyé
    public void genererBase(int x, int y) {
        if(miner.getInventory().checkInventory(Item.BASE) > 0 && isBaseGenerable(x, y)) {
            Base base = new Base(x, y);
            int x2 = x;
            y--; // pour faire genere la base une case plus bas (pour que le mineur soit à l'entrée.
            for(int i = 0 ; i < base.height ; i++) {
                for(int j = 0 ; j < base.width ; j++) {
                    if(layerSurface.getCell(x2, y) != null) {
                        if(layerSurface.getCell(x2, y).getTile().getId() == idDiamond) {
                            MEGame.VICTORY = true;
                            return;
                        }
                    }
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    TiledMapTile tile = tileSets.getTile(Base.CELLS[i][j]);
                    cell.setTile(tile);
                    if(tile != null && tile.getId() == idStore) { // Si magasin
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
            miner.getInventory().remove(Item.BASE, 1);
            bases.add(base);
            GameRenderer.setTist(bases);
        }
    }

    public void dispose() {}

    public static Vector2 getSpawnPosition() {
        return new Vector2(GameWorld.MAP_WIDTH/2 + Miner.WIDTH/2, GameWorld.MAP_HEIGHT-3);
    }

    public Base getBaseById(int idSelect) {
        return bases.get(idSelect);
    }

    public TiledMapTileLayer getLayerSurface() {
        return layerSurface;
    }

    @Override
    public void handle() {
        playerCommands();
    }

    @Override
    public void reload(Object... objects) {
        bases.clear();
        map = (TiledMap) objects[0];
        miner = (Miner) objects[1];
        tileSets = (TiledMapTileSets) objects[2];
        layerObjets = (TiledMapTileLayer) map.getLayers().get("objets");
        layerSurface = (TiledMapTileLayer) map.getLayers().get("surface");
    }
}
