/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.minexploration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.inventaire.Slot;
import com.mygdx.screens.GameScreen;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SauvegardeHandler {
    private MEGame game;
    
    public SauvegardeHandler(MEGame game) {
        this.game = game;
    }
    
    /**
     * Sauvegarde du mineur dans un fichier XML
     * Fichier stocké dans le dossier de la map actuellement chargée
     * Sauvegarde la position, l'argent, la pioche et l'inventaire du mineur
     */
    public void save() {
        saveMineur();
        saveMap();
    }
    
    // LibGDX n'a pas de méthode pour enregistrer une TiledMap, il faut le faire nous même
    private void saveMap() {
        GameScreen gameScreen = (GameScreen) game.getScreen();
        int idPartie = gameScreen.getIdPartie();
        TiledMap map = gameScreen.getWorld().getMap();
        TiledMapTileLayer surface = (TiledMapTileLayer) map.getLayers().get("surface");
        TiledMapTileLayer objets = (TiledMapTileLayer) map.getLayers().get("objets");
        final char TAB = (char) 9;
        int largeur = map.getProperties().get("width", Integer.class);
        int hauteur = map.getProperties().get("height", Integer.class);
        // Date du jour pour l'écrire en commentaire
        FileHandle fichier = new FileHandle("./map/" + idPartie + "/map.tmx");
        StringBuilder input = new StringBuilder();
        input.append("<!-- Carte générée aléatoirement pour le niveau ").append(game.getLevel()).append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\" level=\"").append(game.getLevel()).append("\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");

        int iterateur = 1;
        for(TiledMapTileSet tileset : map.getTileSets()) {
            input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(tileset.getName()).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../").append(tileset.getName()).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
            iterateur++;
        }

        input.append(TAB).append("<layer name=\"surface\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Ici ajout de la matrice surface dans le String input
        // On parcour de haut en bas car le 0,0 et en bas à gauche sur la matrice
        for(int i = hauteur - 1 ; i > -1 ; i--) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur  ; j++) {
                if(surface.getCell(j, i) == null)
                    input.append("0,");
                else
                    input.append(surface.getCell(j, i).getTile().getId()).append(",");
            }
            input.append("\n");
        }
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n\n");
        input.append(TAB).append("<layer name=\"objets\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Ajout de la matrice objet dans le String input (vide)
        for(int i = hauteur - 1 ; i > -1 ; i--) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur  ; j++) {
                if(objets.getCell(j, i) == null)
                    input.append("0,");
                else
                    input.append(objets.getCell(j, i).getTile().getId()).append(",");
            }
            input.append("\n");
        }
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n</map>");               

        fichier.writeString(input.toString(), false, "UTF-8");
    }
    
    private void saveMineur() {
        // On récupère les informations dans des variables
        GameScreen gameScreen = (GameScreen) game.getScreen();
        Mineur mineur = gameScreen.getWorld().getMineur();
        String montantArgent = Integer.toString(mineur.getArgent());
        Vector2 vecteurPosition = mineur.getPosition();
        ArrayList<Slot> slots = mineur.getInventaire().getSlots();
        
        int idPartie = gameScreen.getIdPartie();
        
       try {
            StringWriter writer = new StringWriter();
            XmlWriter xml = new XmlWriter(writer);
            XmlWriter racine = xml.element("mineur");
            racine.element("argent")
                .text(montantArgent)
            .pop()
            .element("position")
                .element("x")
                    .text(vecteurPosition.x)
                .pop()
                .element("y")
                    .text(vecteurPosition.y)
                .pop()
            .pop();
                
            // Pour chaque slot de l'inventaire on sauvegarde le nom et le montant
            for(int i = 0 ; i < slots.size() ; i++) {
                if(slots.get(i).getItem() != null) {
                    racine.element("slot")
                        .element("nom")
                            .text(slots.get(i).getItem().name())
                        .pop()
                        .element("montant")
                            .text(Integer.toString(slots.get(i).getAmount()))
                        .pop()
                    .pop();
                }
            }    
            
            racine.element("pioche")
                .element("nom")
                    .text(mineur.getEquipement().getSlots().get(0).getItem().getTextureRegion())
                .pop()
            .pop();
            
            xml.pop();
            
            FileHandle file = new FileHandle("./map/" + idPartie + "/save.xml");
            file.writeString(writer.toString(), false, "UTF-8");
                           
        } catch (IOException ex) {
            Gdx.app.error("SaveHandler", Arrays.toString(ex.getStackTrace()));
        }
    }
}
