/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GenerationAleatoire {
    private final String blocsSurface[], blocsObjet[];
    private final String chemin;
    private final String nomNiveau;
    private final int niveau;
    private final char TAB = (char) 9; // TABulation
    private final Date date;
    private final DateFormat dateFormat;
    private final Random random;
    private int idDiamant, idGlowstone, idPierre, idHerbe, idTerre;
    
    public GenerationAleatoire(String blocsSurface[], String blocsObjet[], String chemin, String nomNiveau, int niveau) {
        this.blocsSurface = blocsSurface;
        this.blocsObjet = blocsObjet;
        this.chemin = chemin;
        this.nomNiveau = nomNiveau;
        this.niveau = niveau;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = new Date();
        this.random = new Random();
        for(int i = 0 ; i < blocsSurface.length ; i++) {
            if (blocsSurface[i].equals("diamond_block.png")) idDiamant = i + 1;
            else if (blocsSurface[i].equals("dirt.png")) idTerre = i + 1;
            else if (blocsSurface[i].equals("glowstone.png")) idGlowstone = i + 1;
            else if (blocsSurface[i].equals("grass_side.png")) idHerbe = i + 1;
            else if (blocsSurface[i].equals("stone.png")) idPierre = i + 1;
        }
        generer();
    }

    private int genererIdentifiantBloc(int i) {
        int rand = random.nextInt(100);
        if(rand <= 30) { // de 0 à 30
            return idPierre;
        } else if (rand <= 50) { // de 31 à 50
            return idGlowstone;
        }
        return idTerre; // le reste
    }
    
    /**
     * Génération du fichier TMX (format xml)
     * Attention : les blocs objet et surface sont différencié par leur extension (gif et png respectivement)
     * Règles de génération :
     * - On laisse 3 couche vide en haut
     * - 
     */
    private void generer() {
        File fichier = new File(chemin);
        try {
            fichier.createNewFile();
            FileWriter writer = new FileWriter(fichier);
            try {
                StringBuilder input = new StringBuilder();
                int iterateur = 1;
                input.append("<!-- Carte générée aléatoirement le ").append(dateFormat.format(date)).append(" pour le niveau ").append(niveau).append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"15\" height=\"15\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");
                
                for (String blocsSurface1 : blocsSurface) {
                    input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsSurface1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../../").append(blocsSurface1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
                    iterateur++;
                }
                
                for (String blocsObjet1 : blocsObjet) {
                    input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsObjet1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../../").append(blocsObjet1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
                    iterateur++;
                }
                
                int hauteur = getProfondeurGeneration();
                int largeur = (int) Math.round(0.4 * hauteur);
                
                input.append(TAB).append("<layer name=\"surface\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");
                
                // Génération de la base de départ
                for(int i = 0 ; i < 3 ; i++ ) {
                    input.append(TAB).append(TAB);
                    for(int j = 0 ; j < largeur ; j++) {
                       input.append("0").append(",");
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
                            input.append(idDiamant).append(",");
                        else
                            input.append(genererIdentifiantBloc(i)).append(",");
                    }
                    input.append("\n");
                } 
                input.delete(input.length()-2, input.length()); // On enlève la virgule en trop
                
                input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n\n");
                input.append(TAB).append("<layer name=\"objets\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");
                
                // Ajout de la matrice objet dans le String input (vide)
                for(int i = 0 ; i < hauteur ; i++) {
                    input.append(TAB).append(TAB);
                    for(int j = 0 ; j < largeur ; j++) {
                        input.append("0,");
                    }
                    input.append("\n");
                }
                input.delete(input.length()-2, input.length()); // On enlève la virgule en trop
                
                input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n</map>");
                
                writer.write(input.toString()); 
                
            } catch(IOException io1) {
                System.out.println("Erreur lors de l'écriture dans le fichier : " + io1.getMessage());
            } finally {
                writer.close();
            }
        } catch(IOException io2) {
            System.out.println("Erreur lors de la création du fichier : " + io2.getMessage());           
        }
        String chemin2 = chemin.substring(0, chemin.length()-7);
        File fichier2 = new File(chemin2 + "Niveau " + niveau + ".name");
        try {
            fichier2.createNewFile();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }    
    }   
    
    private int getProfondeurGeneration() {
        return (int) (Math.round(1.19*niveau*niveau+13.86)*10)/10;
    }
    
}

