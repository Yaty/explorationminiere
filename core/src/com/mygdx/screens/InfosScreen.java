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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.minexploration.MEGame;
import com.mygdx.minexploration.handlers.I18n;

/**
 * Infos screen, not implemented yet
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InfosScreen implements Screen {
    private final MEGame game;
    private final BitmapFont title, content;
    private final TextButton back;
    private final Stage stage;
    private final SpriteBatch batch;
    private final String titleStr, contentStr;
    
    public InfosScreen(final MEGame game) {
        this.game = game;
        this.stage = new Stage();
        this.batch = new SpriteBatch();
        title = new BitmapFont();
        title.setColor(Color.BLACK);
        content = new BitmapFont();
        content.setColor(Color.BLACK);
        back = new TextButton(I18n.MENU.getString("Back"), new Skin(Gdx.files.internal("skin/uiskin.json")));
        back.setSize(200, 100);
        back.setPosition(Gdx.graphics.getWidth()/2 - back.getWidth()/2, 450);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.backToMainMenuScreen();
            }
        });
        titleStr = I18n.MENU.getString("TitleInfos");
        contentStr = I18n.MENU.getString("ContentInfos");
        stage.addActor(back);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
        stage.act();
        stage.draw();
        
        batch.begin();
        title.draw(batch, titleStr, Gdx.graphics.getWidth()/2 - 30, Gdx.graphics.getHeight() - 25);
        content.draw(batch, contentStr, 100, Gdx.graphics.getHeight() - 100, Gdx.graphics.getWidth() - 200, 1, true);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        content.dispose();
        title.dispose();
    }
    
}
