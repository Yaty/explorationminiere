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
package com.mygdx.mehelpers.handlers;

import com.mygdx.mehelpers.handlers.handlers.MoveHandler;
import com.mygdx.mehelpers.handlers.handlers.Handler;
import com.mygdx.mehelpers.handlers.handlers.HealthHandler;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;
import com.mygdx.mehelpers.handlers.handlers.FogHandler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.gameobjects.Miner;
import java.util.LinkedList;

/**
 * Class which wrap handlers
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Handlers implements Handler {    
    private final LinkedList<Handler> handlers;
    
    /**
     * Constructor
     * @param miner the miner
     * @param map tiled map
     */
    public Handlers(Miner miner, TiledMap map) {       
        FogHandler fogHandler = new FogHandler((TiledMapTileLayer) map.getLayers().get("fog"), miner.getPosition());
        MapHandler mapHandler = new MapHandler(map, miner);
        HealthHandler healthHandler = new HealthHandler(miner.getHealthObj(), mapHandler, miner.getPosition());
        mapHandler.setHealthHandler(healthHandler);
        MoveHandler deplacementHandler = new MoveHandler(mapHandler, miner.getPosition());
        
        handlers = new LinkedList<Handler>();
        handlers.add(fogHandler);
        handlers.add(mapHandler);
        handlers.add(healthHandler);
        handlers.add(deplacementHandler);
    }

    /**
     * Launch handle on handlers
     */
    @Override
    public void handle() {
        for(Handler handler : handlers) {
            handler.handle();
        }
    }

    @Override
    public void reload(Object... objects) {
        Miner miner = (Miner) objects[0];
        TiledMap map = (TiledMap) objects[1];
        
        FogHandler fogHandler = new FogHandler((TiledMapTileLayer) map.getLayers().get("fog"), miner.getPosition());
        MapHandler mapHandler = new MapHandler(map, miner);
        HealthHandler healthHandler = new HealthHandler(miner.getHealthObj(), mapHandler, miner.getPosition());
        mapHandler.setHealthHandler(healthHandler);
        MoveHandler move = new MoveHandler(mapHandler, miner.getPosition());
        
        handlers.clear();
        handlers.add(fogHandler);
        handlers.add(mapHandler);
        handlers.add(healthHandler);
        handlers.add(move);
    }

    /**
     * Get the map handler
     * @return the map handler
     */
    public MapHandler getMapHandler() {
        for(Handler handler : handlers) {
            if(handler instanceof MapHandler)
                return (MapHandler) handler;
        }
        return null;
    }
   
}
