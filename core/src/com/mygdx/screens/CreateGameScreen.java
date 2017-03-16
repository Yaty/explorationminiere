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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;
import com.mygdx.minexploration.handlers.I18n;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Create a game screen
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class CreateGameScreen implements Screen {
    private final MEGame game;
    private TextField nomPartie;
    private final Stage stage;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Skin skin;
    private FileHandle[] directories;
    
    /**
     * Constructor
     * @param game the Game
     */
    public CreateGameScreen(MEGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.font = new BitmapFont();
        this.font.getData().setScale(1);
        this.font.setColor(Color.BROWN);
        Gdx.input.setInputProcessor(stage); // Le stage va s'occuper des E/S
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        createButtons(I18n.MENU.getString("Submit"));
        createTextField();
    }
    
    private void createTextField() {
        nomPartie = new TextField("", skin);
        nomPartie.setWidth(200);
        nomPartie.setPosition((Gdx.graphics.getHeight()/2)-70,  (Gdx.graphics.getWidth()/2)+40);
        stage.addActor(nomPartie);
    }
    
    private int getGameId() {
        FileHandle file = Gdx.files.local("map");
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        int maxi = 1;
        for(int i = 0 ; i < directories.length ; i++) {
            if(Integer.parseInt(directories[i].name()) > maxi)
                maxi = Integer.parseInt(directories[i].name());
        }
        return maxi+1;
    }
    
    public final void createButtons(String text) {
        TextButton valider = new TextButton(text, skin); // On utilise le skin
        valider.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        valider.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dispose();
                    if(!nomPartie.getText().isEmpty())
                        game.newFirstLevel(getGameId(), nomPartie.getText());
                };
        });
        stage.addActor(valider);
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
        font.draw(batch, I18n.MENU.getString("NameGame") + " :", (Gdx.graphics.getHeight()/2)-65,  (Gdx.graphics.getWidth()/2)+100);
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
        batch.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
    
}
