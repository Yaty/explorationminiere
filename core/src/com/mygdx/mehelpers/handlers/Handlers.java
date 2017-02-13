/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers;

import com.mygdx.mehelpers.handlers.handlers.DeplacementHandler;
import com.mygdx.mehelpers.handlers.handlers.Handler;
import com.mygdx.mehelpers.handlers.handlers.HealthHandler;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;
import com.mygdx.mehelpers.handlers.handlers.FogHandler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.gameobjects.Mineur;
import java.util.LinkedList;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class Handlers implements Handler {    
    private final LinkedList<Handler> handlers;
    
    public Handlers(Mineur mineur, TiledMap map) {       
        FogHandler fogHandler = new FogHandler((TiledMapTileLayer) map.getLayers().get("fog"), mineur.getPosition());
        MapHandler mapHandler = new MapHandler(map, mineur);
        HealthHandler healthHandler = new HealthHandler(mineur.getHealth(), mapHandler, mineur.getPosition());
        DeplacementHandler deplacementHandler = new DeplacementHandler(mapHandler, mineur.getPosition());
        
        handlers = new LinkedList<Handler>();
        handlers.add(fogHandler);
        handlers.add(mapHandler);
        handlers.add(healthHandler);
        handlers.add(deplacementHandler);
    }

    @Override
    public void handle() {
        for(Handler handler : handlers) {
            handler.handle();
        }
    }

    @Override
    public void reload(Object... objects) {

    }

    public MapHandler getMapHandler() {
        for(Handler handler : handlers) {
            if(handler instanceof MapHandler)
                return (MapHandler) handler;
        }
        return null;
    }
   
}
