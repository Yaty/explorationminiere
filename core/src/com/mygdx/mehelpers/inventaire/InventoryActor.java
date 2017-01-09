/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameobjects.Inventaire;
import com.mygdx.screens.GameScreen;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InventoryActor extends Window {
    public InventoryActor(String nom, Inventaire inventaire, int width, int height, DragAndDrop dragAndDrop, Skin skin, GameScreen screen) {
        super(nom, skin);

        // basic layout
        setPosition(width, height);
        defaults().space(20);
        row().fill().expandX();
        setColor(Color.RED);
        
        // run through all slots and create SlotActors for each
        for (Slot slot : inventaire.getSlots()) {
            SlotActor slotActor = new SlotActor(skin, slot, screen);
            add(slotActor);

            // this can be ignored for now and will be explained in part III
            dragAndDrop.addSource(new SlotSource(slotActor));
            dragAndDrop.addTarget(new SlotTarget(slotActor));
        }

        pack();

        // it is hidden by default -> plus mtn
        //setVisible(false);
    }    
}
