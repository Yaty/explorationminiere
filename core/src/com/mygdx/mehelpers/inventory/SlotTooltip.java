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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SlotTooltip extends Window implements SlotListener {
    private final Skin skin;

    private final Slot slot;

    /**
     * Constructor
     * @param slot
     * @param skin
     */
    public SlotTooltip(Slot slot, Skin skin) {
        super("Informations", skin);
        this.slot = slot;
        this.skin = skin;
        hasChanged(slot);
        slot.addListener(this);
        setVisible(false);
    }

    @Override
    public void hasChanged(Slot slot) {
        if (slot.isEmpty()) {
            setVisible(false);
            return;
        }
        clear();
        this.getTitleLabel().setText(slot.getAmount() + " : " + slot.getItem().getName());
        Label label = new Label(slot.getItem().getDescription(), skin);
        add(label);
        if(slot.getItem().getName().toLowerCase().startsWith("pioche") && slot.getItem().getPriceToUpgrade() != 0) {
            this.row();
            Label label2 = new Label("Argent nécessaire pour améliorer l'objet : " + slot.getItem().getPriceToUpgrade(), skin);
            add(label2);
        }
        pack();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        // the listener sets this to true in case the slot is hovered
        // however, we don't want that in case the slot is empty
        if (slot.isEmpty()) {
            super.setVisible(false);
        }
    }    
}
