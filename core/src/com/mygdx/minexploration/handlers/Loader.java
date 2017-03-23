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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.gameobjects.minerobjects.Inventory;
import java.io.IOException;

/**
 * Class to load a map, miner's data ...
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Loader {
    private XmlReader reader;
    private Element racine;
    
    /**
     * Constructor
     * @param id map's id
     */
    public Loader(int id) {
        try {
            this.reader = new XmlReader();
            FileHandle file = Gdx.files.local("./map/" + id + "/save.xml");
            this.racine = reader.parse(file);
        } catch (IOException ex) {
            Gdx.app.error("ChargementHandler", "Parse file erreur.", ex);
            Gdx.app.exit();
        }
    }
    
    /**
     * Get the saved money
     * @return the money
     */
    public int getMoney() {
        Element argent = racine.getChildByName("argent");
        return Integer.parseInt(argent.getText());
    }
    
    /**
     * Get miner saved position
     * @return a vector which contains the position
     */
    public Vector2 getPosition() {
        Element position = racine.getChildByName("position");
        float x = Float.parseFloat(position.getChildByName("x").getText());
        float y = Float.parseFloat(position.getChildByName("y").getText());
        return new Vector2(x, y);
    }
    
    /**
     * Get the saved pickaxe
     * @return an inventory whith a pickaxe
     */
    public Inventory getEquipment() {
        Element pioche = racine.getChildByName("pioche");
        String nom = pioche.getChildByName("nom").getText();
        return new Inventory(nom);
    }
    
    /**
     * Get the saved inventory
     * @return an inventory filled
     */
    public Inventory getInventory() {
        Array<Element> slots = racine.getChildrenByName("slot");
        int nbEchelles = 0, nbPiliers = 0, nbTnt = 0, nbMagasin = 0;
        for(Element slot : slots) {
            String nom = slot.getChildByName("nom").getText();
            int value = Integer.parseInt(slot.getChildByName("montant").getText());
            if ("LADDER".equals(nom.toUpperCase()))
                nbEchelles = value;
            else if ("PILLAR".equals(nom.toUpperCase()))
                nbPiliers = value;
            else if ("BASE".equals(nom.toUpperCase()))
                nbMagasin = value;
            else if ("TNT".equals(nom.toUpperCase()))
                nbTnt = value;
        }
        return new Inventory(nbEchelles, nbPiliers, nbTnt, nbMagasin);
    }
    
    /**
     * Get saved health
     * @return the save health
     */
    public float getHealth() {
        Element vie = racine.getChildByName("vie");
        return Float.parseFloat(vie.getText());
    }
    
}
