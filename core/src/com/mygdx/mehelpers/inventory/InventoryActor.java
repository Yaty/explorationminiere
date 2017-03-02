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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameobjects.minerobjects.Inventory;
import com.mygdx.screens.GameScreen;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InventoryActor extends Window {

    /**
     * Create an Inventory Actor
     * @param windowName The window's name
     * @param inventory An inventory to display
     * @param width The window's width
     * @param height The window's height
     * @param dragAndDrop drag&drop object
     * @param skin a skin
     * @param screen the game screen
     */
    public InventoryActor(String windowName, Inventory inventory, int width, int height, DragAndDrop dragAndDrop, Skin skin, GameScreen screen) {
        super(windowName, skin);

        // basic layout
        setPosition(width, height);
        defaults().space(20);
        row().fill().expandX();
        setColor(Color.RED);
        
        // run through all slots and create SlotActors for each
        for (Slot slot : inventory.getSlots()) {
            SlotActor slotActor = new SlotActor(skin, slot, screen);
            add(slotActor);

            // this can be ignored for now and will be explained in part III
            dragAndDrop.addSource(new SlotSource(slotActor, windowName));
            dragAndDrop.addTarget(new SlotTarget(slotActor, windowName));
        }

        pack();
    }    
}
