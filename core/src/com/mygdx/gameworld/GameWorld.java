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
package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.gameobjects.Miner;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;
import com.mygdx.mehelpers.handlers.Handlers;
import com.mygdx.minexploration.handlers.Loader;

/**
 * The game world !
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GameWorld {
    public static int MAP_WIDTH, MAP_HEIGHT;
    private final float UNITY = 1/64f;
    private TiledMap map;
    private final Miner miner;
    private final String pathToMap;
    private final Handlers handlers;
    
    /**
     * Construct a new game world
     * @param pathToMap the path to the TMX file
     * @param loading true if we are loading a game, alse if it's a new one
     */
    public GameWorld(String pathToMap, boolean loading) {
        this.pathToMap = pathToMap;
        map = new TmxMapLoader().load(pathToMap);
        MAP_WIDTH = map.getProperties().get("width", Integer.class);
        MAP_HEIGHT = map.getProperties().get("height", Integer.class);
        Miner.WIDTH = UNITY * AssetLoader.regions[0].getRegionWidth();
        Miner.HEIGHT = UNITY * AssetLoader.regions[0].getRegionHeight();
        if(!loading)
            miner = new Miner(MapHandler.getSpawnPosition()); // manque la position de départ
        else {
            int id = Integer.parseInt(pathToMap.replaceAll("[\\D]", ""));
            if(Gdx.files.internal("./map/" + id + "/save.xml").exists()) {
                Loader chargeur = new Loader(id);
                miner = new Miner(chargeur.getMoney(), chargeur.getPosition(), chargeur.getInventory(), chargeur.getEquipment(), chargeur.getHealth());
            } else {
                miner = new Miner(MapHandler.getSpawnPosition());
            }
        }
        handlers = new Handlers(miner, map);
    }

    /**
     * Update the world
     * @param delta time passed during the last frame
     */
    public void update(float delta) {
        miner.update(delta);
        handlers.handle();
    }
    
    /**
     * @return the map
     */
    public TiledMap getMap() {
        return map;
    }
    
    /**
     * @return the miner
     */
    public Miner getMiner() {
        return miner;
    }

    /**
     * Reload the world
     */
    public void reload() {
        map = new TmxMapLoader().load(pathToMap);
        MAP_WIDTH = map.getProperties().get("width", Integer.class);
        MAP_HEIGHT = map.getProperties().get("height", Integer.class);        
        miner.reload();
        handlers.reload(miner, map);
        miner.respawn(MapHandler.getSpawnPosition());
    }

    /**
     * Dispose the world
     */
    public void dispose() {
        map.dispose();
        miner.dispose();
        // handlers.dispose(); ??
    }

    /**
     * Get the list of handlers
     * @return the handlers
     */
    public Handlers getHandlers() {
        return handlers;
    }
    
}
