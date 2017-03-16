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
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;
import com.mygdx.minexploration.handlers.I18n;

/**
 * Pause menu
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class MenuPause extends Window {
    
    /**
     * Create a pause menu
     * @param skin the skin
     * @param game the game
     */
    public MenuPause(Skin skin, final MEGame game) {
        super("Pause", skin);
        TextButton continuer = new TextButton(I18n.GAME.getString("KeepPlaying"), skin);
        TextButton sauvegarder = new TextButton(I18n.GAME.getString("Save"), skin);
        TextButton quitterSauvegarde = new TextButton(I18n.GAME.getString("QuitSave"), skin);
        TextButton quitterSansSauvegarder = new TextButton(I18n.GAME.getString("QuitWithoutSaving"), skin);
        continuer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                game.getScreen().resume();
            }
        });
        sauvegarder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.save();
            }
        });
        quitterSauvegarde.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.quitAndSave();
            }
        });
        quitterSansSauvegarder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.shutdown();
            }
        });

        add(continuer);
        row();
        add(sauvegarder);
        row();
        add(quitterSauvegarde);
        row();
        add(quitterSansSauvegarder);
        
        // basic layout
        setBounds((Gdx.graphics.getWidth()/2)-(getWidth()/2), (Gdx.graphics.getHeight()/2)-(getHeight()/2), 200, 200);
        defaults().space(8);
        row().fill().expandX();

        pack();

        // it is hidden by default
        setVisible(false);
    }    
}
