package com.mygdx.gameworld;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.minexploration.ShutdownHandler;

/**
 *
 * @author Hugo
 */
public class GameWorld {
    
    private final TiledMap map;
    private final Mineur mineur;

    /**
     *
     */
    public GameWorld() {
        map = new TmxMapLoader().load("mapv3.tmx");
        //stage = new TiledMapStage(map);
        mineur = new Mineur(map);
        
        
    }

    /**
     * Va mettre à jour le mineur
     * @param delta le temps passé depuis la dernière frame
     */
    public void update(float delta) {
        mineur.update(delta);
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
    
    
}
