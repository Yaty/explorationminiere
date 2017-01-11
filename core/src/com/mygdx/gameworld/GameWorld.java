package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.minexploration.ChargementHandler;

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
     * @param cheminMap
     * @param chargement
     */
    public GameWorld(String cheminMap, boolean chargement) {
        this.cheminMap = cheminMap;
        map = new TmxMapLoader().load(cheminMap);
        if(!chargement)
            mineur = new Mineur(map);
        else {
            int id = Integer.parseInt(cheminMap.replaceAll("[\\D]", ""));
            if(Gdx.files.internal("./map/" + id + "/save.xml").exists()) {
                ChargementHandler chargeur = new ChargementHandler(id);
                mineur = new Mineur(map, chargeur.getArgent(), chargeur.getPosition(), chargeur.getInventaire(), chargeur.getEquipement(), chargeur.getHealth());
            } else {
                mineur = new Mineur(map);
            }
        }
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
