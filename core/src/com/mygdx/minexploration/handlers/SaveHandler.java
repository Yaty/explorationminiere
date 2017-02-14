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
package com.mygdx.minexploration.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;
import com.mygdx.gameobjects.Miner;
import com.mygdx.mehelpers.inventory.Slot;
import com.mygdx.minexploration.MEGame;
import com.mygdx.screens.GameScreen;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Class to save miner's data, tiled map ...
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SaveHandler {
    private final MEGame game;
    private GameScreen gameScreen;
    private int idPartie;
    private final int TILEWIDTH = 64, TILEHEIGHT = 64;
    
    /**
     * Constructor
     * @param game the game
     */
    public SaveHandler(MEGame game) {
        this.game = game;
    }
    
    private void updateRef() {
        gameScreen = (GameScreen) game.getScreen();
        idPartie = gameScreen.getGameId();
    }
    
    /**
     * Save miner's data
     * Save map
     */
    public void save() {
        updateRef();
        saveMiner();
        saveMap();
    }
    
    private void saveMap() {
        TiledMap map = gameScreen.getWorld().getMap();
        TiledMapTileLayer surface = (TiledMapTileLayer) map.getLayers().get("surface");
        TiledMapTileLayer objets = (TiledMapTileLayer) map.getLayers().get("objets");
        TiledMapTileLayer fog = (TiledMapTileLayer) map.getLayers().get("fog");
        int largeur = map.getProperties().get("width", Integer.class);
        int hauteur = map.getProperties().get("height", Integer.class);
        
        try {
            StringWriter writer = new StringWriter();
            XmlWriter xml = new XmlWriter(writer);
            XmlWriter racine = xml.element("map");
            racine.attribute("version", "1.0").attribute("orientation", "orthogonal").attribute("renderorder", "left-up").attribute("width", largeur).attribute("height", hauteur).attribute("level", game.getLevel()).attribute("tilewidth", TILEWIDTH).attribute("tileheight", TILEHEIGHT).attribute("nextobjectid", "2");
            
            int compteurDeBloc = 1;
            for(TiledMapTileSet tileset : map.getTileSets()) {
                racine
                .element("tileset").attribute("firstgid", compteurDeBloc++).attribute("name", tileset.getName()).attribute("tilewidth", TILEWIDTH).attribute("tileheight", TILEHEIGHT).attribute("tilecount", "1").attribute("columns", "1")
                    .element("image").attribute("source", "../" + tileset.getName()).attribute("width", TILEWIDTH).attribute("height", TILEHEIGHT)
                    .pop()
                .pop();
            }
            
            final String matriceSurfaces = tileLayerToString(surface), matriceObjets = tileLayerToString(objets), matriceFog = tileLayerToString(fog);
            
            racine
            .element("layer").attribute("name", "surface").attribute("width", largeur).attribute("height", hauteur)
                .element("data").attribute("encoding", "csv")
                    .text(matriceSurfaces)
                .pop()
            .pop()
            .element("layer").attribute("name", "objets").attribute("width", largeur).attribute("height", hauteur)
                .element("data").attribute("encoding", "csv")
                    .text(matriceObjets)
                .pop()
            .pop()
            .element("layer").attribute("name", "fog").attribute("width", largeur).attribute("height", hauteur)
                .element("data").attribute("encoding", "csv")
                    .text(matriceFog)
                .pop()
            .pop();
            
            racine.pop(); // Fermeture balise map
            
            Gdx.files.local("map/" + idPartie + "/map.tmx").writeString(writer.toString(), false, "UTF-8"); // Ecriture
        } catch (IOException ex) {
            Gdx.app.error("SauvegardeHandler", "Erreur lors de la sauvegarde de la carte.", ex);
            Gdx.app.exit();
        }
    }
    
    private String tileLayerToString(TiledMapTileLayer layer) {
        StringBuilder str = new StringBuilder();
        final int hauteurLayer = layer.getHeight();
        final int largeurLayer = layer.getWidth();
        
        for(int i = hauteurLayer - 1 ; i > -1 ; i--) {
            for(int j = 0 ; j < largeurLayer  ; j++) {
                if(layer.getCell(j, i) == null)
                    str.append("0,");
                else {
                    TiledMapTile tile = layer.getCell(j, i).getTile();
                    if(tile != null)
                        str.append(tile.getId()).append(",");
                }
            }
        }
        str.delete(str.length()-1, str.length()); // Suppression \n et ,
        
        return str.toString();        
    }
    
    private void saveMiner() {
        // On récupère les informations dans des variables
        Miner mineur = gameScreen.getWorld().getMiner();
        String montantArgent = Integer.toString(mineur.getMoney());
        Vector2 vecteurPosition = mineur.getPosition();
        ArrayList<Slot> slots = mineur.getInventory().getSlots();
        
        try {
            StringWriter writer = new StringWriter();
            XmlWriter xml = new XmlWriter(writer);
            XmlWriter racine = xml.element("mineur");
            racine.element("argent")
                .text(montantArgent)
            .pop()
            .element("position")
                .element("x")
                    .text(vecteurPosition.x)
                .pop()
                .element("y")
                    .text(vecteurPosition.y)
                .pop()
            .pop()
            .element("vie")
                .text(mineur.getHealth())
            .pop();
                
            // Pour chaque slot de l'inventaire on sauvegarde le nom et le montant
            for(int i = 0 ; i < slots.size() ; i++) {
                if(slots.get(i).getItem() != null) {
                    racine.element("slot")
                        .element("nom")
                            .text(slots.get(i).getItem().name())
                        .pop()
                        .element("montant")
                            .text(Integer.toString(slots.get(i).getAmount()))
                        .pop()
                    .pop();
                }
            }    
            
            racine.element("pioche")
                .element("nom")
                    .text(mineur.getEquipment().getSlots().get(0).getItem().getTextureRegion())
                .pop()
            .pop();
            
            xml.pop();
            
            Gdx.files.local("map/" + idPartie + "/save.xml").writeString(writer.toString(), false, "UTF-8");
                           
        } catch (IOException ex) {
            Gdx.app.error("SaveHandler", "Erreur Xml", ex);
        }
    }
}
