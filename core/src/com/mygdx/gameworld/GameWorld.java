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
     *
     * @param delta
     */
    public void update(float delta) {
        mineur.update(delta);
        if(mineur.getCellsHandler().isVictory()) ShutdownHandler.shutdown();
        
        //stage.act();
    }
    
    /**
     *
     * @return
     */
    public TiledMap getMap() {
        return map;
    }
    
    /**
     *
     * @return
     */
    public Mineur getMineur() {
        return mineur;
    }
    
    
}
