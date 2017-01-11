/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.minexploration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.gameobjects.Inventaire;
import java.io.IOException;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class ChargementHandler {
    private XmlReader reader;
    private Element racine;
    
    public ChargementHandler(int id) {
        try {
            this.reader = new XmlReader();
            FileHandle file = Gdx.files.internal("./map/" + id + "/save.xml");
            this.racine = reader.parse(file);
        } catch (IOException ex) {
            Gdx.app.error("ChargementHandler", "Parse file erreur.", ex);
            Gdx.app.exit();
        }
    }
    
    public int getArgent() {
        Element argent = racine.getChildByName("argent");
        return Integer.parseInt(argent.getText());
    }
    
    public Vector2 getPosition() {
        Element position = racine.getChildByName("position");
        float x = Float.parseFloat(position.getChildByName("x").getText());
        float y = Float.parseFloat(position.getChildByName("y").getText());
        return new Vector2(x, y);
    }
    
    public Inventaire getEquipement() {
        Element pioche = racine.getChildByName("pioche");
        String nom = pioche.getChildByName("nom").getText();
        return new Inventaire(nom);
    }
    
    public Inventaire getInventaire() {
        Array<Element> slots = racine.getChildrenByName("slot");
        int nbEchelles = 0, nbPiliers = 0, nbTnt = 0, nbMagasin = 0;
        for(Element slot : slots) {
            String nom = slot.getChildByName("nom").getText();
            int value = Integer.parseInt(slot.getChildByName("montant").getText());
            if ("ECHELLE".equals(nom.toUpperCase()))
                nbEchelles = value;
            else if ("PILIER".equals(nom.toUpperCase()))
                nbPiliers = value;
            else if ("MAGASIN".equals(nom.toUpperCase()))
                nbMagasin = value;
            else if ("TNT".equals(nom.toUpperCase()))
                nbTnt = value;
        }
        return new Inventaire(nbEchelles, nbPiliers, nbTnt, nbMagasin);
    }
    
    public float getHealth() {
        Element vie = racine.getChildByName("vie");
        return Float.parseFloat(vie.getText());
    }
    
}
