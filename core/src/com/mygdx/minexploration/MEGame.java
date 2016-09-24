package com.mygdx.minexploration;

import com.badlogic.gdx.Game; // On importe Game qui implémente ApplicationListener
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.MainMenuScreen;
import com.mygdx.screens.MenuFin;

/**
 *
 * @author Hugo
 */
public class MEGame extends Game {

    
    /**
     *
     */
    @Override
    public void create() {
        // Chargement des textures, assignation du screen au menu principal
        AssetLoader.load();
        setScreen(new MainMenuScreen(this));
    }
    
    public void backToMainMenuScreen(){
        setScreen(new MainMenuScreen(this));
    }
    
    public void createMenuFin (){
        setScreen(new MenuFin(this));
    }    
    /**
     *
     */    
    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
    

}
