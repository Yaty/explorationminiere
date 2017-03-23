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
package com.mygdx.gameobjects.minerobjects;

import com.mygdx.minexploration.handlers.I18n;

/**
 * Enum which represent Items, used by the Inventory
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public enum Item {
    TNT("tnt", I18n.GAME.getString("tnt"), I18n.GAME.getString("tnt_desc"), 0, 200),
    LADDER("echelle", I18n.GAME.getString("echelle"), I18n.GAME.getString("echelle_desc"), 0, 50),
    PILLAR("pilier", I18n.GAME.getString("pilier"), I18n.GAME.getString("pilier_desc"), 0, 100),
    BASE("base", I18n.GAME.getString("base"), I18n.GAME.getString("base_desc"), 0, 2000),
    WOODEN_PICKAXE("pioche_bois", I18n.GAME.getString("pioche_bois"), I18n.GAME.getString("pioche_bois_desc"), 1, 3000),
    STONE_PICKAXE("pioche_pierre", I18n.GAME.getString("pioche_pierre"), I18n.GAME.getString("pioche_pierre_desc"), 1.5f, 10000),
    IRON_PICKAXE("pioche_fer", I18n.GAME.getString("pioche_fer"), I18n.GAME.getString("pioche_fer_desc"), 2, 25000),
    GOLD_PICKAXE("pioche_or", I18n.GAME.getString("pioche_or"), I18n.GAME.getString("pioche_or_desc"), 3, 50000),
    DIAMOND_PICKAXE("pioche_diamant", I18n.GAME.getString("pioche_diamant"), I18n.GAME.getString("pioche_diamant_desc"), 5,  0);

    private final String textureRegion, name, description;
    private final float param;
    private final int priceToUpgrade;
    /**
     * Find an item with his name
     * @param nom the name to search
     * @return the item corresponding to that name, null if not found
     */
    public static Item getItemFromTextureName(String nom) {
        if (nom.toLowerCase().equals("pioche_bois")) return Item.WOODEN_PICKAXE;
        else if (nom.toLowerCase().equals("pioche_pierre")) return Item.STONE_PICKAXE;
        else if (nom.toLowerCase().equals("pioche_fer")) return Item.IRON_PICKAXE;
        else if (nom.toLowerCase().equals("pioce_or")) return Item.GOLD_PICKAXE;
        else if (nom.toLowerCase().equals("pioche_diamant")) return Item.DIAMOND_PICKAXE;
        else if(nom.toLowerCase().equals("tnt")) return Item.TNT;
        else if (nom.toLowerCase().equals("echelle")) return Item.LADDER;
        else if (nom.toLowerCase().equals("pilier")) return Item.PILLAR;
        else if (nom.toLowerCase().equals("base")) return Item.BASE;
        else return null;
    }

    private Item(String textureRegion, String nom, String description, float param, int prixUpgrade) {
        this.textureRegion = textureRegion;
        this.name = nom;
        this.description = description;
        this.param = param;
        this.priceToUpgrade = prixUpgrade;
    }
    
    /**
     * Get the price to update the item
     * @return the price
     */
    public int getPriceToUpgrade() {
        return priceToUpgrade;
    }
    
    /**
     * Get the param value
     * @return param value
     */
    public float getParam() {
        return param;
    }
    
    /**
     * Get the texture region name
     * @return a string which represent the texture region element
     */
    public String getTextureRegion() {
        return textureRegion;
    }
    
    /**
     * Get the name of the item
     * @return a string which is the name of the item
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the description of the item
     * @return a string which is the description of the item
     */
    public String getDescription() {
        return description;
    }
}
