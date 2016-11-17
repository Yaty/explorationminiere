package com.mygdx.gameobjects;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import com.mygdx.mehelpers.inventaire.Slot;

/**
 * Classe qui représente l'inventaire du mineur
 * http://pixelscientists.com/wordpress/?p=15
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Inventaire {
    //private int echelles, piliers, tnt;
    private Pioche pioche;
    
    private ArrayList<Slot> slots;
    
    /**
     * Constructeur par défaut
     * @param echelles Nombre d'échelles
     * @param piliers Nombre de piliers
     * @param tnt Nombre de TNT
     * @param pioche La pioche
     * @throws java.lang.Exception
    */
    /*public Inventaire (int echelles, int piliers, int tnt, Pioche pioche) throws Exception{
        if(echelles < 0 || piliers < 0 || tnt < 0) {
            throw new Exception("Vous ne pouvez pas définir un nombre négatif d'objet dans l'inventaire.");
        } else {
            this.echelles = echelles;
            this.piliers = piliers;
            this.tnt = tnt;
        }
        if(pioche != null) 
            this.pioche = pioche;
        else
            throw new Exception("Vous ne pouvez pas définir une pioche null.");
    }*/
    
    public Inventaire() {
        /* echelles = 15;
        piliers = 5;
        tnt = 0;*/
        pioche = new Pioche("BASIQUE", 1f);
        
        slots = new ArrayList<Slot>(25);
        for (int i = 0; i < 25; i++) {
            slots.add(new Slot(null, 0));
        }

        // create some random items
        for (Slot slot : slots) {
            slot.add(Item.values()[MathUtils.random(0, Item.values().length - 1)], 1);
        }

        // create a few random empty slots
        for (int i = 0; i < 3; i++) {
            Slot randomSlot = slots.get(MathUtils.random(0, slots.size() - 1));
            randomSlot.take(randomSlot.getAmount());
        }
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
    
    public Pioche getPioche() {
        return pioche;
    }
    
    public void setPioche(Pioche pioche) throws Exception {
        if(pioche != null)
            this.pioche = pioche;
        else
            throw new Exception("Vous ne pouvez pas modifier une pioche à null.");
    }
    
    /*public void ajouterEchelle() {
        echelles++;
    }
    
    public void ajouterEchelles(int n) throws Exception {
        if(n > 0)
            echelles += n;
        else
            throw new Exception("Vous ne pouvez pas ajouter un nombre négatif d'échelle. Utilisez la méthode enleverEchelles().");
    }
    
    public void enleverEchelle() {
        echelles--;
    }
    
    public void ajouterPiliers(int n) throws Exception {
        if(n > 0)
            piliers += n;
        else
            throw new Exception("Vous ne pouvez pas ajouter un nombre négatif dde piliers. Utilisez la méthode enleverPiliers().");
    }
    
    public void enleverPilier() {
        piliers--;
    }
    
    public void ajouterPilier() {
        piliers++;
    }
    
    public void ajouterTnts(int n) throws Exception {
        if(n > 0)
            tnt += n;
        else
            throw new Exception("Vous ne pouvez pas ajouter un nombre négatif de TNT. Utilisez la méthode enleverTNT().");
    }
    
    public void ajouterTnt() {
        tnt++;
    }
    
    public void enleverTnt() {
        tnt--;
    }

    public int getEchelles() {
        return echelles;
    }

    public void setEchelles(int echelles) throws Exception {
        if(echelles >= 0)
            this.echelles = echelles;
        else
            throw new Exception("Le nombre d'échelles ne peut pas être définit négatif.");
    }

    public int getPiliers() {
        return piliers;
    }

    public void setPiliers(int piliers) throws Exception {
        if(piliers >= 0)
            this.piliers = piliers;
        else
            throw new Exception("Le nombre de piliers ne peut pas être définit négatif.");
    }

    public int getTnt() {
        return tnt;
    }

    public void setTnt(int tnt) throws Exception {
        if(tnt >= 0)
            this.tnt = tnt;
        else
            throw new Exception("Le nombre de TNT ne peut pas être définit négatif.");
    }*/
}
