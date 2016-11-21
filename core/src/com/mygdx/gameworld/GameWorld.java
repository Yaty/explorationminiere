package com.mygdx.gameworld;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.gameobjects.Mineur;

/**
 *
 * @author Hugo
 */
public class GameWorld {
    
    private TiledMap map;
    private final Mineur mineur;
    private final String cheminMap;

    /**
     *
     */
    public GameWorld(String cheminMap) {
        this.cheminMap = cheminMap;
        map = new TmxMapLoader().load(cheminMap);
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

    public void reload() {
        map = new TmxMapLoader().load(cheminMap);
        mineur.reload(map);
    }
    
    
}
