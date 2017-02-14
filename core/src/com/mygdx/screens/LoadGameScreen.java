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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Load game screen
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class LoadGameScreen implements Screen {
    private final MEGame game;
    private final Stage stage;
    private Skin skin;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final SelectBox<String> sb;
    private String gamesNames[];
    private FileHandle foldersNames[];
    
    /**
     * Constructor
     * @param game
     */
    public LoadGameScreen(MEGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        stage = new Stage();
        createSkin();
        sb = new SelectBox<String>(new Skin(Gdx.files.internal("skin/uiskin.json"))); 
        font = new BitmapFont();
        font.getData().setScale(1);
        font.setColor(Color.BROWN);
        Gdx.input.setInputProcessor(stage); // Le stage va s'occuper des E/S
        createListe("map");
        createBtn("Valider");
    }
  
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        batch.begin();
        font.draw(batch, "Sélectionnez la partie :", (Gdx.graphics.getHeight()/2)-65,  (Gdx.graphics.getWidth()/2)+50);
        batch.end();
    }

    private void createBtn(String text) {
        TextButton valider = new TextButton(text, skin); // On utilise le skin
        valider.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2-150);
        valider.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dispose();
                    game.loadGame(Integer.parseInt(foldersNames[sb.getSelectedIndex()].name()), gamesNames[sb.getSelectedIndex()]);
                };
        });
        stage.addActor(valider);
    } 
    
    private void createSkin() {
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }
    
    private void createListe(String text) {
        // https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/SelectBox.html
        // On va lister les parties (équivalente à des dossiers) dans le tableau directories
        FileHandle map = new FileHandle(text);
        foldersNames = map.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        
        gamesNames = new String[foldersNames.length];
        for(int j = 0 ; j < foldersNames.length ; j++) {
            FileHandle folder = new FileHandle(text + '/' + foldersNames[j].name());
            System.out.println("Folder : " + folder.path());
            FileHandle[] files = folder.list(new FilenameFilter() {
                @Override
                public boolean accept(File folder, String name) {
                    return name.toLowerCase().endsWith(".name");
                }
            });
            gamesNames[j] = files[0].name().substring(0, files[0].name().length()-5);
        }
        
        // Set up the SelectionBox with content
        sb.setItems(gamesNames);

        //For easier handling of Widgets
        Table table = new Table();
        table.setFillParent(true); 
        table.center();
        table.add(sb);
        stage.addActor(table);
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
    public void show() {
    }
    
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
