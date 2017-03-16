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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.mygdx.gameobjects.minerobjects.Item;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SlotSource extends Source {
    private final Slot sourceSlot;

    /**
     * Constructor
     * @param actor the actor
     * @param name the actor's name
     */
    public SlotSource(SlotActor actor, String name) {
        super(actor);
        actor.setName(name);
        this.sourceSlot = actor.getSlot();
    }
    
    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sourceSlot.getAmount() == 0) {
            return null;
        }

        Payload payload = new Payload();
        Slot payloadSlot = new Slot(sourceSlot.getItem(), sourceSlot.getAmount());
        sourceSlot.take(sourceSlot.getAmount());
        payload.setObject(payloadSlot);

        TextureAtlas icons = new TextureAtlas(Gdx.files.internal("icons/inventaire/icons.atlas"));
        TextureRegion icon = icons.findRegion(payloadSlot.getItem().getTextureRegion());

        Actor dragActor = new Image(icon);
        payload.setDragActor(dragActor);

        Actor validDragActor = new Image(icon);
        payload.setValidDragActor(validDragActor);

        Actor invalidDragActor = new Image(icon);
        payload.setInvalidDragActor(invalidDragActor);

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        Slot payloadSlot = (Slot) payload.getObject();
        // the payload was dropped over a valid target
        if (target != null) {
            Slot targetSlot = ((SlotActor) target.getActor()).getSlot();
            // if the item is the same, stack it
            if (targetSlot.getItem() == payloadSlot.getItem() || targetSlot.getItem() == null) {
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
            } else {
                // the item is not the same, thus switch the items
                Item targetType = targetSlot.getItem();
                int targetAmount = targetSlot.getAmount();
                targetSlot.take(targetAmount);
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
                sourceSlot.add(targetType, targetAmount);
            }
        } else {
            // the payload was not dropped over a target, thus put it back to where it came from
            sourceSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
        }
    }    
}
