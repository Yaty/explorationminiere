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
package com.mygdx.mehelpers.inventory;

import com.mygdx.gameobjects.minerobjects.Item;
import java.util.ArrayList;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Slot {
    private Item item;
    private int amount;

    private final ArrayList<SlotListener> slotListeners;

    /**
     * Construct a slot
     * @param item an item which will be in the slot
     * @param amount the numbers of time it will be in the slot
     */
    public Slot(Item item, int amount) {
        this.slotListeners = new ArrayList<SlotListener>();
        this.item = item;
        this.amount = amount;
    }

    /**
     * Check if the slot is empty
     * @return true if it is empty, false otherwise
     */
    public boolean isEmpty() {
        return item == null || amount <= 0;
    }

    /**
     * Add an item in the slot, an amount of times
     * @param item the item to add
     * @param amount the numbers of times to add the item
     * @return true if it was add, false otherwise
     */
    public boolean add(Item item, int amount) {
        if (this.item == item || this.item == null) {
            this.item = item;
            this.amount += amount;
            notifyListeners();
            return true;
        }

        return false;
    }
    
    /**
     * Clear the slot
     */
    public void clearSlot() {
        item = null;
        amount = 0;
    }

    /**
     * Remove an numbers of item in the slot
     * @param amount ther amount to remove
     * @return true if it was removed, false otherwise
     */
    public boolean take(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
            if (this.amount == 0) {
                item = null;
            }
            notifyListeners();
            return true;
        }

        return false;
    }

    /**
     *
     * @param slotListener
     */
    public void addListener(SlotListener slotListener) {
        slotListeners.add(slotListener);
    }

    /**
     * Remove a listener
     * @param slotListener
     */
    public void removeListener(SlotListener slotListener) {
        slotListeners.remove(slotListener);
    }

    private void notifyListeners() {
        for (SlotListener slotListener : slotListeners) {
            slotListener.hasChanged(this);
        }
    }

    /**
     * Get the item
     * @return the item in the slot
     */
    public Item getItem() {
        return item;
    }

    /**
     * Return the amount of items in the slot
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Slot[" + item + ":" + amount + "]";
    }    
}
