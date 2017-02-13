package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;
import com.mygdx.mehelpers.handlers.Handlers;
import com.mygdx.minexploration.handlers.ChargementHandler;

/**
 *
 * @author Hugo
 */
public class GameWorld {
    public static int MAP_WIDTH, MAP_HEIGHT;
    private TiledMap map;
    private final Mineur mineur;
    private final String cheminMap;
    private final Handlers handlers;
    
    /**
     *
     * @param cheminMap
     * @param chargement
     */
    public GameWorld(String cheminMap, boolean chargement) {
        this.cheminMap = cheminMap;
        map = new TmxMapLoader().load(cheminMap);
        MAP_WIDTH = map.getProperties().get("width", Integer.class);
        MAP_HEIGHT = map.getProperties().get("height", Integer.class);
        if(!chargement)
            mineur = new Mineur(MapHandler.getSpawnPosition()); // manque la position de départ
        else {
            int id = Integer.parseInt(cheminMap.replaceAll("[\\D]", ""));
            if(Gdx.files.internal("./map/" + id + "/save.xml").exists()) {
                ChargementHandler chargeur = new ChargementHandler(id);
                mineur = new Mineur(chargeur.getArgent(), chargeur.getPosition(), chargeur.getInventaire(), chargeur.getEquipement(), chargeur.getHealth());
            } else {
                mineur = new Mineur(MapHandler.getSpawnPosition());
            }
        }
        handlers = new Handlers(mineur, map);
    }

    /**
     * Va mettre à jour le mineur
     * @param delta le temps passé depuis la dernière frame
     */
    public void update(float delta) {
        mineur.update(delta);
        handlers.handle();
    }
    
    /**
     * @return la carte
     */
    public TiledMap getMap() {
        return map;
    }
    
    /**
     * @return le mineur
     */
    public Mineur getMineur() {
        return mineur;
    }

    public void reload() {
        map = new TmxMapLoader().load(cheminMap);
        MAP_WIDTH = map.getProperties().get("width", Integer.class);
        MAP_HEIGHT = map.getProperties().get("height", Integer.class);        
        mineur.reload();
        handlers.reload();
    }

    public void dispose() {
        map.dispose();
        mineur.dispose();
        // handlers.dispose(); ??
    }

    public Handlers getHandlers() {
        return handlers;
    }
    
}
