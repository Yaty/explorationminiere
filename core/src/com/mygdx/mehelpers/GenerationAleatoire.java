/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GenerationAleatoire {
    private final String blocsSurface[], blocsObjet[];
    private Grid caves, liquides;
    private final String chemin;
    private final int niveau;
    private final char TAB = (char) 9; // TABulation
    private final Random random;
    private int idDiamant, idGlowstone, idPierre, idHerbe, idTerre, idCharbon, idEmeraude, idOr, idFer, idLapis, idLave, idFog;
    private final LinkedList<Integer> idFleurs;
    
    public GenerationAleatoire(String blocsSurface[], String blocsObjet[], String chemin, int niveau) {
        this.blocsSurface = blocsSurface;
        this.blocsObjet = blocsObjet;
        this.idFleurs = new LinkedList();
        this.chemin = chemin;
        this.niveau = niveau;
        this.random = new Random();
        int i;
        for(i = 0 ; i < blocsSurface.length ; i++) {
            if (blocsSurface[i].equals("diamond_block.png")) idDiamant = i + 1;
            else if (blocsSurface[i].equals("dirt.png")) idTerre = i + 1;
            else if (blocsSurface[i].equals("glowstone.png")) idGlowstone = i + 1;
            else if (blocsSurface[i].equals("grass_side.png")) idHerbe = i + 1;
            else if (blocsSurface[i].equals("stone.png")) idPierre = i + 1;
            else if (blocsSurface[i].equals("coal_ore.png")) idCharbon = i + 1;
            else if (blocsSurface[i].equals("emerald_ore.png")) idEmeraude = i + 1;
            else if (blocsSurface[i].equals("gold_ore.png")) idOr = i + 1;
            else if (blocsSurface[i].equals("iron_ore.png")) idFer = i + 1;
            else if (blocsSurface[i].equals("lapis_ore.png")) idLapis = i + 1;
        }
        
        for(int j = 0 ; j < blocsObjet.length ; j++) {
            i++;
            if(blocsObjet[j].equals("lave.gif")) idLave = i;
            else if (blocsObjet[j].equals("fog.gif")) idFog = i;
            else if (blocsObjet[j].startsWith("flower")) idFleurs.add(i);
        }

        generer();
    }
    
    // On peut faire ça mieux avec un tableau par zone de profondeur (100 à 200 par ex) contenant les probas
    // et un tableau avec nos blocs, on parcourt et on return
    private int genererIdentifiantBloc(int profondeur) {
        int rand = random.nextInt(200);              // 201 valeurs possibles - 0 à 200
        if(profondeur < 20) {
            if(rand <= 19) return idPierre;             // de 0 à 19
            else if (rand <= 24) return idGlowstone;    // de 20 à 24
            else if (rand <= 34) return idCharbon;      // de 25 à 34
            else if (rand <= 39) return idFer;          // de 35 à 39
            else if (rand <= 44) return idOr;           // de 40 à 44
            else if (rand <= 47) return idLapis;        // de 45 à 47
            else if (rand <= 49) return idEmeraude;     // de 48 à 49
            return idTerre;                             // le reste -> 75%
        } else if (profondeur < 50) {
            if(rand <= 19) return idPierre;             // de 0 à 19 - 20%
            else if (rand <= 23) return idGlowstone;    // de 20 à 23 - 4%
            else if (rand <= 31) return idCharbon;      // de 24 à 31 - 8%
            else if (rand <= 35) return idFer;          // de 32 à 35 - 4%
            else if (rand <= 39) return idOr;           // de 36 à 39 - 4%
            else if (rand <= 42) return idLapis;        // de 40 à 42 - 3%
            else if (rand <= 44) return idEmeraude;     // de 43 à 44 - 2%
            return idTerre;                             // le reste -> + 50%           
        } else if (profondeur < 100) {
            if(rand <= 19) return idPierre;             // de 0 à 19 - 20%
            else if (rand <= 22) return idGlowstone;    // de 20 à 22 - 3%
            else if (rand <= 28) return idCharbon;      // de 23 à 28 - 6%
            else if (rand <= 31) return idFer;          // de 29 à 31 - 3%
            else if (rand <= 34) return idOr;           // de 32 à 34 - 3%
            else if (rand <= 36) return idLapis;        // de 35 à 36 - 2%
            else if (rand <= 38) return idEmeraude;     // de 37 à 38 - 2%
            return idTerre;                             // le reste -> + 50%                      
        } else if (profondeur < 500) {
            if(rand <= 19) return idPierre;             // de 0 à 19 - 20%
            else if (rand <= 21) return idGlowstone;    // de 20 à 21 - 2%
            else if (rand <= 24) return idCharbon;      // de 22 à 24 - 4%
            else if (rand <= 27) return idFer;          // de 25 à 27 - 3%
            else if (rand <= 30) return idOr;           // de 28 à 30 - 3%
            else if (rand <= 32) return idLapis;        // de 31 à 32 - 2%
            else if (rand <= 34) return idEmeraude;     // de 33 à 34 - 2%
            return idTerre;                             // le reste -> + 50%                
        } else { // >= 500
            if(rand <= 19) return idPierre;             // de 0 à 19 - 20%
            else if (rand <= 20) return idGlowstone;    // de 20 à 20 - 1%
            else if (rand <= 23) return idCharbon;      // de 21 à 23 - 4%
            else if (rand <= 26) return idFer;          // de 24 à 26 - 3%
            else if (rand <= 29) return idOr;           // de 27 à 29 - 3%
            else if (rand <= 31) return idLapis;        // de 30 à 31 - 2%
            else if (rand <= 32) return idEmeraude;     // de 32 à 32 - 1%
            return idTerre;                             // le reste -> + 50%                 
        }
    }
    
    /**
     * Génération du fichier TMX (format xml)
     * Attention : les blocs objet et surface sont différencié par leur extension (gif et png respectivement)
     * Règles de génération :
     * - On laisse 3 couche vide en haut
     * - 
     */
    private void generer() {
        FileHandle fichier = Gdx.files.local(chemin);
        int hauteur = 200;//getProfondeurGeneration();
        int largeur = 200;//(int) Math.round(0.4 * hauteur);
        
        caves = genererGrid(hauteur, largeur, 0.515f, 2, 13, 12, 15);
        liquides = genererGrid(hauteur, largeur, 0.65f, 2, 13, 9, 6);
        
        StringBuilder input = new StringBuilder();
        int iterateur = 1;
        input.append("<!-- Carte générée aléatoirement pour le niveau ").append(niveau).append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\" level=\"").append(niveau).append("\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");

        for (String blocsSurface1 : blocsSurface) {
            input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsSurface1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../").append(blocsSurface1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
            iterateur++;
        }

        for (String blocsObjet1 : blocsObjet) {
            input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsObjet1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../").append(blocsObjet1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
            iterateur++;
        }


        input.append(TAB).append("<layer name=\"surface\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Génération de la base de départ
        for(int i = 0 ; i < 3 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
               input.append("0,");
            }
            input.append("\n");
        }

        input.append(TAB).append(TAB);
        for(int i = 0 ; i < largeur ; i++) {
            input.append(idHerbe).append(",");
        }
        input.append("\n");    

        int positionDiamantX = random.nextInt(largeur-1);
        int posDiamMaxY = hauteur-1;
        int posDiamMinY = (int) (hauteur*0.8f);
        int positionDiamantY = random.nextInt(posDiamMaxY-posDiamMinY)+posDiamMinY;

        // Ici ajout de la matrice surface dans le String input
        for(int i = 4 ; i < hauteur ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
                if(positionDiamantX == j && positionDiamantY == i)
                    input.append(idDiamant).append(',');
                else {
                    if(caves.get(j, i) == 1f) {
                        input.append(0).append(',');
                    } else if (liquides.get(j, i) == 1f) {
                        input.append(0).append(',');
                    } else // Si on est pas une cave ou pas dans une zone de liquide = c'est un bloc
                        input.append(genererIdentifiantBloc(i)).append(',');
                }
            }
            input.append("\n");
        } 
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n\n");
        input.append(TAB).append("<layer name=\"objets\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Ajout de la matrice objet dans le String input (vide)

        for(int i = 0 ; i < 2 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
                input.append("0,");
            }
            input.append("\n");
        }

        input.append(TAB).append(TAB);
        for(int j = 0 ; j < largeur ; j++) {
           input.append(getIdFleur()).append(",");
        }
        input.append("\n");

        for(int i = 3 ; i < hauteur ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
                if(liquides.get(j, i) == 1f)
                    input.append(idLave).append(',');
                else
                    input.append("0,");
            }
            input.append("\n");
        }
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop        

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n");
        
        // AJOUT DE LA MATRICE FOG
        input.append("\n").append(TAB).append("<layer name=\"fog\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");
        for(int i = 0 ; i < 3 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
               input.append("0,");
            }
            input.append("\n");
        }
        
        for(int i = 3 ; i < hauteur ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < largeur ; j++) {
                input.append(idFog).append(',');
            }
            input.append("\n");
        }
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop        
        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n");

        
        input.append("</map>");
        fichier.writeString(input.toString(), false, "UTF-8");
    }   
    
    private int getProfondeurGeneration() {
        return (int) (Math.round(1.19*niveau*niveau+13.86)*10)/10;
    }

    private String getIdFleur() {
        Random rand = new Random();
        int nb = rand.nextInt(99);
        if(nb < 10) return String.valueOf(idFleurs.get(rand.nextInt(idFleurs.size()-1))); // 10% de fleurs
        else return "0";
    }

    private Grid genererGrid(int hauteur, int largeur, float f, int i, int i0, int i1, int i2) {
        final Grid grid = new Grid(largeur, hauteur);
        final CellularAutomataGenerator cellularGenerator = new CellularAutomataGenerator();
        cellularGenerator.setAliveChance(f);
        cellularGenerator.setRadius(i);
        cellularGenerator.setBirthLimit(i0);
        cellularGenerator.setDeathLimit(i1);
        cellularGenerator.setIterationsAmount(i2);
        cellularGenerator.generate(grid);
        return grid;
    }
    
}