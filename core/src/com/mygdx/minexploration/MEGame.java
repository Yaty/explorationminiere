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
        // Création du jeu
        setScreen(new MainMenuScreen(this));
    }
    
    public void createGame(){
        AssetLoader.load();
        setScreen(new GameScreen(this));
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
