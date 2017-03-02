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
package com.mygdx.mehelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class which contains loaded assets
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class AssetLoader {
    public static Texture minerTexture, backgroundTexture, healthBarTexture,healthbarContainerTexture;
    public static Animation standing, walking, jumping;
    public static TextureRegion[] regions;
    public static Music tnt_sound, run_sound, dig_sound,break_sound, fall_stone_sound,prefall_stone_sound;
    
    /**
     * Load textures and animations
     */
    public static void load() {
        // Chargement texture + animations
        minerTexture = new Texture(Gdx.files.internal("mineur.png"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        healthBarTexture = new Texture(Gdx.files.internal("gui_healthbar.png"));
        healthbarContainerTexture = new Texture(Gdx.files.internal("gui_healthbar_container.png"));
        
        // A modif selon l'image du mineur
        //healthBar = TextureRegion.split(healthBarTexture, 30, 30)[0];
        regions = TextureRegion.split(minerTexture, 36, 52)[0];
        standing = new Animation(0, regions[0]);
        jumping = new Animation(0, regions[1]);
        walking = new Animation(0.15f, regions[2], regions[3], regions[4]);
        walking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        
        //Sons
        tnt_sound = Gdx.audio.newMusic(Gdx.files.internal("tnt_sound.mp3"));
        run_sound = Gdx.audio.newMusic(Gdx.files.internal("run_sound.mp3"));
        run_sound.setVolume(0.15f);
        dig_sound = Gdx.audio.newMusic(Gdx.files.internal("dig_sound.mp3"));
        dig_sound.setVolume(0.05f);
        break_sound = Gdx.audio.newMusic(Gdx.files.internal("break_sound.mp3"));
        break_sound.setVolume(0.3f);
        fall_stone_sound = Gdx.audio.newMusic(Gdx.files.internal("fall_stone_sound.mp3"));
        prefall_stone_sound = Gdx.audio.newMusic(Gdx.files.internal("prefall_stone_sound.mp3"));
        prefall_stone_sound.setLooping(true);
    }
    
    
    /**
     * Dipose the textures. Called when the game is exiting.
     */
    public static void dispose() {
        minerTexture.dispose();
        backgroundTexture.dispose();
        healthBarTexture.dispose();
        healthbarContainerTexture.dispose();
        tnt_sound.dispose();
        run_sound.dispose();
        dig_sound.dispose();
        break_sound.dispose();
        fall_stone_sound.dispose();
        prefall_stone_sound.dispose();
    }       

}
