package com.mygdx.gameobjects;

import java.util.ArrayList;
import com.mygdx.mehelpers.inventaire.Slot;

/**
 * Classe qui représente l'inventaire du mineur
 * http://pixelscientists.com/wordpress/?p=15
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Inventaire {   
    private final ArrayList<Slot> slots;
    
    public Inventaire() {
        slots = new ArrayList<Slot>();
        slots.add(new Slot(Item.ECHELLE, 15));
        slots.add(new Slot(Item.PILIER, 10));
        slots.add(new Slot(Item.BASE, 1));
        slots.add(new Slot(Item.TNT, 5));
    }

    public Inventaire(int nbEchelles, int nbPiliers, int nbTnt, int nbBase) {
        slots = new ArrayList<Slot>();
        slots.add(new Slot(Item.ECHELLE, nbEchelles));
        slots.add(new Slot(Item.PILIER, nbPiliers));
        slots.add(new Slot(Item.BASE, nbBase));
        slots.add(new Slot(Item.TNT, nbTnt));
    }
    
    public Inventaire(String nom) {
        slots = new ArrayList<Slot>();
        Item pioche = Item.getItemFromTextureName(nom);
        slots.add(new Slot(pioche, 1));
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
