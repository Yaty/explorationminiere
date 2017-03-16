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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.gameobjects.minerobjects.Item;
import com.mygdx.minexploration.handlers.I18n;
import com.mygdx.screens.GameScreen;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SlotActor extends ImageButton implements SlotListener {

    private final Slot slot;

    private final Skin skin;
    
    /**
     * Constructor
     * @param skin the skin
     * @param slot the slot
     * @param screen the game screen
     */
    public SlotActor(Skin skin, final Slot slot, final GameScreen screen) {
        super(createStyle(skin, slot));
        this.slot = slot;
        this.skin = skin;

        // this actor has to be notified when the slot itself changes
        slot.addListener(this);

        // ignore this for now, it will be explained in part IV
        SlotTooltip tooltip = new SlotTooltip(slot, skin);
        GameScreen.stage.addActor(tooltip);
        addListener(new TooltipListener(tooltip, true));
        addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(slot != null && slot.getItem() != null && slot.getItem().getTextureRegion().toLowerCase().startsWith("pioche") && !slot.getItem().getTextureRegion().equals("pioche_diamant")) {
                    // Slot équipement
                    Item pioche = slot.getItem();
                    int prixUpgrade = pioche.getPriceToUpgrade();
                    if(screen.getWorld().getMiner().getMoney() >= prixUpgrade) {
                        switch (pioche) {
                            case WOODEN_PICKAXE:
                                slot.clearSlot();
                                slot.add(Item.STONE_PICKAXE, 1);
                                screen.getWorld().getMiner().withdrawMoney(pioche.getPriceToUpgrade());
                                break;
                            case STONE_PICKAXE:
                                slot.clearSlot();
                                slot.add(Item.IRON_PICKAXE, 1);
                                screen.getWorld().getMiner().withdrawMoney(pioche.getPriceToUpgrade());
                                break;
                            case IRON_PICKAXE:
                                slot.clearSlot();
                                slot.add(Item.GOLD_PICKAXE, 1);
                                screen.getWorld().getMiner().withdrawMoney(pioche.getPriceToUpgrade());
                                break;
                            case GOLD_PICKAXE:
                                slot.clearSlot();
                                slot.add(Item.DIAMOND_PICKAXE, 1);
                                screen.getWorld().getMiner().withdrawMoney(pioche.getPriceToUpgrade());
                                break;
                            default:
                                break;
                        }
                    }
                } else if (slot.getItem() != null && !slot.getItem().name().startsWith("pioche") && getName().equals(I18n.GAME.getString("Store"))) {
                    if(screen.getWorld().getMiner().getMoney() >= slot.getItem().getPriceToUpgrade()) {
                        screen.getWorld().getMiner().withdrawMoney(slot.getItem().getPriceToUpgrade());
                        screen.getWorld().getMiner().getInventory().store(slot.getItem(), 1);
                    }                    
                }
            }
        });
    }

    /**
     * This will create a new style for our image button, with the correct image for the item type.
     */
    private static ImageButtonStyle createStyle(Skin skin, Slot slot) {
        TextureAtlas icons = new TextureAtlas(Gdx.files.internal("icons/inventaire/icons.atlas"));
        TextureRegion image;
        if (slot.getItem() != null) {
            image = icons.findRegion(slot.getItem().getTextureRegion());
        } else {
            // we have a special "empty" region in our atlas file, which is just black
            image = icons.findRegion("nothing");
        }
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(image);
        style.imageDown = new TextureRegionDrawable(image);
        return style;
    }

    @Override
    public void hasChanged(Slot slot) {
        // when the slot changes, we switch the icon via a new style
        setStyle(createStyle(skin, slot));
    }

    /**
     * Get the slot
     * @return the slot
     */
    public Slot getSlot() {
        return slot;
    }
    
}
