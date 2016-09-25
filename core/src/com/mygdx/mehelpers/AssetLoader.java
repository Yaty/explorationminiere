package com.mygdx.mehelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// On devrait charger la map en tmx ici ! ?
// Tout en static car on ne va pas créer plusieurs instances de cette classe

/**
 *
 * @author Hugo
 */

public class AssetLoader {
    public static Texture mineurTexture, backgroundTexture;
    public static Animation debout, marcher, sauter;
    public static TextureRegion[] regions;
    // private static Sprite background;
    
    /**
     *
     */
    public static void load() {
        // Chargement texture + animations
        mineurTexture = new Texture(Gdx.files.internal("mineur.png"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        
        // A modif selon l'image du mineur
        regions = TextureRegion.split(mineurTexture, 36, 52)[0];
        debout = new Animation(0, regions[0]);
        sauter = new Animation(0, regions[1]);
        marcher = new Animation(0.15f, regions[2], regions[3], regions[4]);
        marcher.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }
    
    
    /**
     *
     */
    public static void dispose() {
        mineurTexture.dispose();
        backgroundTexture.dispose();
    }   
}
