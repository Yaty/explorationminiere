/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameobjects.HidingClickListener;
import com.mygdx.gameobjects.Inventaire;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InventoryActor extends Window {
    public InventoryActor(Inventaire inventaire, DragAndDrop dragAndDrop, Skin skin) {
        super("Inventaire", skin);

        // add an "X" button to the top right of the window, and make it hide the inventory
        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new HidingClickListener(this));
        //bug//getButtonTable().add(closeButton).height(getPadTop());

        // basic layout
        setPosition(400, 100);
        defaults().space(8);
        row().fill().expandX();

        // run through all slots and create SlotActors for each
        int i = 0;
        for (Slot slot : inventaire.getSlots()) {
            SlotActor slotActor = new SlotActor(skin, slot);
            add(slotActor);

            // this can be ignored for now and will be explained in part III
            dragAndDrop.addSource(new SlotSource(slotActor));
            dragAndDrop.addTarget(new SlotTarget(slotActor));

            i++;
            // every 5 cells, we are going to jump to a new row
            if (i % 5 == 0) {
                row();
            }
        }

        pack();

        // it is hidden by default
        setVisible(false);
    }    
}
