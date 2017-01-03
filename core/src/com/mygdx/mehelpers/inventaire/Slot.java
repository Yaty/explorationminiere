/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.mygdx.gameobjects.Item;
import java.util.ArrayList;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Slot {
    private Item item;
    private int amount;
    private float paramSuppl; // Pour la pioche

    private ArrayList<SlotListener> slotListeners = new ArrayList<SlotListener>();

    public Slot(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Slot(Item item, int i, float j) {
        this.item = item;
        this.amount = i;
        this.paramSuppl = j;
    }
    
    public float getParamSuppl() {
        return paramSuppl;
    }

    public boolean isEmpty() {
        return item == null || amount <= 0;
    }

    public boolean add(Item item, int amount) {
        if (this.item == item || this.item == null) {
            this.item = item;
            this.amount += amount;
            notifyListeners();
            return true;
        }

        return false;
    }

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

    public void addListener(SlotListener slotListener) {
        slotListeners.add(slotListener);
    }

    public void removeListener(SlotListener slotListener) {
        slotListeners.remove(slotListener);
    }

    private void notifyListeners() {
        for (SlotListener slotListener : slotListeners) {
            slotListener.hasChanged(this);
        }
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Slot[" + item + ":" + amount + "]";
    }    
}
