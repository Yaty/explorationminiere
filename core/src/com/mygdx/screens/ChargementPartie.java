/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.minexploration.MEGame;
import java.io.File;
import java.io.FilenameFilter;
import com.mydgx.screens.helpers.PagedScrollPane;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class ChargementPartie implements Screen {
    private final MEGame game;
    private Stage stage;
    private Skin skin;
    private Table container;
    
    public ChargementPartie(MEGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Le stage va s'occuper des E/S
        createListe();
    }
    
    
    private void createListe() {
        
        // https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/SelectBox.html
        
        // On va lister les parties (équivalente à des dossiers) dans le tableau directories
        File file = new File("./map/");
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        
        String names[] = new String[directories.length];
        for(int j = 0 ; j < directories.length ; j++) {
            File folder = new File("./map/" + directories[j]);
            File[] files = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File folder, String name) {
                    return name.toLowerCase().endsWith(".name");
                }
            });
            names[j] = files[0].getName().substring(0, files[0].getName().length()-5);
        }
        
        // Set up the SelectionBox with content
        final SelectBox<String> sb = new SelectBox<String>(new Skin(Gdx.files.internal("skin/uiskin.json"))); 
        sb.setItems(names); 


        //For easier handling of Widgets
        Table table = new Table();
        table.setFillParent(true); 
        table.center();

        table.add(sb);
        stage.addActor(table);
       
    }

    @Override
    public void show() {
    }
    
/**
 * Cette methode sert pour l'affichage dans l'ecrans du menu
 * 
 */
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
    
    
    
}
