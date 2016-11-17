/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.screens.GameScreen;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SlotActor extends ImageButton implements SlotListener {

    private Slot slot;

    private Skin skin;

    public SlotActor(Skin skin, Slot slot) {
        super(createStyle(skin, slot));
        this.slot = slot;
        this.skin = skin;

        // this actor has to be notified when the slot itself changes
        slot.addListener(this);

        // ignore this for now, it will be explained in part IV
        SlotTooltip tooltip = new SlotTooltip(slot, skin);
        GameScreen.stage.addActor(tooltip);
        addListener(new TooltipListener(tooltip, true));
    }

    /**
     * This will create a new style for our image button, with the correct image for the item type.
     */
    private static ImageButtonStyle createStyle(Skin skin, Slot slot) {
        TextureAtlas icons = new TextureAtlas(Gdx.files.internal("icons/inventaire/icons.atlas"));
        TextureRegion image;
        if (slot.getItem() != null) {
            System.out.println(slot.getItem());
            image = icons.findRegion(slot.getItem().getTextureRegion());
        } else {
            // we have a special "empty" region in our atlas file, which is just black
            image = icons.findRegion("nothing");
        }
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        System.out.println(image);
        style.imageUp = new TextureRegionDrawable(image);
        style.imageDown = new TextureRegionDrawable(image);

        return style;
    }

    @Override
    public void hasChanged(Slot slot) {
        // when the slot changes, we switch the icon via a new style
        setStyle(createStyle(skin, slot));
    }

    public Slot getSlot() {
        return slot;
    }
    
}
