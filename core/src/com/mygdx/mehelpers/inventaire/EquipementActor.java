/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameobjects.Equipement;
import com.mygdx.gameobjects.HidingClickListener;
import com.mygdx.gameobjects.Inventaire;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class EquipementActor extends Window {
    public EquipementActor(Equipement equipement, DragAndDrop dragAndDrop, Skin skin) {
        super("Equipement", skin);

        // add an "X" button to the top right of the window, and make it hide the inventory
        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new HidingClickListener(this));
        //bug//getButtonTable().add(closeButton).height(getPadTop());

        // basic layout
        setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        defaults().space(20);
        row().fill().expandX();
        setColor(Color.GREEN);

        SlotActor slotActor = new SlotActor(skin, equipement.getPioche());
        add(slotActor);
        dragAndDrop.addSource(new SlotSource(slotActor));
        dragAndDrop.addTarget(new SlotTarget(slotActor));

        pack();

        // it is hidden by default -> plus mtn
        //setVisible(false);
    }    
}
