package com.mygdx.gameobjects;

import java.util.ArrayList;
import com.mygdx.mehelpers.inventaire.Slot;

/**
 * Classe qui représente l'inventaire du mineur
 * http://pixelscientists.com/wordpress/?p=15
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Inventaire {   
    private ArrayList<Slot> slots;
    
    public Inventaire() {
        slots = new ArrayList();
        slots.add(new Slot(Item.ECHELLE, 15));
        slots.add(new Slot(Item.PILIER, 10));
        slots.add(new Slot(Item.MAGASIN, 1));
        slots.add(new Slot(Item.TNT, 5));
    }
    
    public int checkInventory(Item item) {
        int amount = 0;

        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                amount += slot.getAmount();
            }
        }

        return amount;
    }

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

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public Slot firstSlotWithItem(Item item) {
        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                return slot;
            }
        }

        return null;
    }
}
