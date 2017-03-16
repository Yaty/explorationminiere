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

import java.util.ArrayList;
import com.mygdx.mehelpers.inventory.Slot;

/**
 * Class which represent an inventory, used by the miner
 * Mostly coded by : http://pixelscientists.com/wordpress/?p=15
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Inventory {   
    private final ArrayList<Slot> slots;
    
    /**
     * Constructor called to instanciate this class
     * Set up a basic inventory
     * @param size size of the inventory
     */
    public Inventory(int size) {
        slots = new ArrayList<Slot>();
        for(int i = 0 ; i < size ; i++)
            slots.add(new Slot(null, 0));
    }

    /**
     * Constructor called to instanciate this class with a certain numbers
     * of items
     * @param nbLadders the number of ladders to add
     * @param nbPillars the number of pillars to add
     * @param nbTnt the number of Tnt to add
     * @param nbBases the number of base to add
     */
    public Inventory(int nbLadders, int nbPillars, int nbTnt, int nbBases) {
        slots = new ArrayList<Slot>();
        slots.add(new Slot(Item.LADDER, nbLadders));
        slots.add(new Slot(Item.PILLAR, nbPillars));
        slots.add(new Slot(Item.BASE, nbBases));
        slots.add(new Slot(Item.TNT, nbTnt));
    }
    
    /**
     * Constructor called to instanciate this class with a certain item
     * Usefull for the pickaxe
     * @param nom the name of the item to add
     */
    public Inventory(String nom) {
        slots = new ArrayList<Slot>();
        Item pioche = Item.getItemFromTextureName(nom);
        slots.add(new Slot(pioche, 1));
    }
    
    /**
     * Get how much the item is in the inventory
     * @param item the item to search
     * @return the number of this item in the inventory
     */
    public int checkInventory(Item item) {
        int amount = 0;
        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                amount += slot.getAmount();
            }
        }
        return amount;
    }

    /**
     * Add an item in the inventory with an amount
     * @param item the item to add
     * @param amount the number of this item to add
     * @return true if it was added, false otherwise
     */
    public boolean store(Item item, int amount) {
        // first check for a slot with the same item type
        Slot itemSlot = firstSlotWithItem(item);
        if (itemSlot != null) {
            itemSlot.add(item, amount);
            return true;
        } else {
            // now check for an available empty slot
            Slot emptySlot = firstSlotWithItem(null);
            if (emptySlot != null) {
                emptySlot.add(item, amount);
                return true;
            }
        }

        // no slot to add
        return false;
    }
    
    /**
     * Remove an item in the inventory with an amount
     * @param item the item to remove
     * @param amount the number of this item to remove
     * @return true if it was deleted, false otherwise
     */
    public boolean remove(Item item, int amount) {
        // first check for a slot with the same item type
        Slot itemSlot = firstSlotWithItem(item);
        if (itemSlot != null) { // Si on a déjà l'item
            itemSlot.take(amount);
            return true;
        }
        // rien a supprimé
        return false;        
    }

    /**
     * Get the slots of the inventory
     * @return the slots
     */
    public ArrayList<Slot> getSlots() {
        return slots;
    }

    /**
     * Get the first slot filled with an item
     * @param item the item we search
     * @return the slot where the item is stored
     */
    public Slot firstSlotWithItem(Item item) {
        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                return slot;
            }
        }

        return null;
    }

    /**
     * Soon
     */
    public void dispose() {}
}
