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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GenerationAleatoire {
    private final String blocsSurface[];
    private final String blocsObjet[];
    private final String chemin;
    private final String nomNiveau;
    private final int niveau;
    private final char TAB = (char) 9; // TABulation
    private final Date date;
    private final DateFormat dateFormat;
    private final Random random;
    
    public GenerationAleatoire(String blocsSurface[], String blocsObjet[], String chemin, String nomNiveau, int niveau) {
        this.blocsSurface = blocsSurface;
        this.blocsObjet = blocsObjet;
        this.chemin = chemin;
        this.nomNiveau = nomNiveau;
        this.niveau = niveau;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = new Date();
        this.random = new Random();
        System.out.println("Début génération");
        generer();
        System.out.println("Fin génération");
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
                input.append("<!-- Carte générée aléatoirement le ").append(dateFormat.format(date)).append(" pour le niveau ").append(niveau)
                        .append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"15\" height=\"15\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");
                
                for(int i = 0 ; i < blocsSurface.length ; i++) {
                    input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsSurface[i]).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n")
                            .append(TAB).append(TAB).append("<image source=\"../../").append(blocsSurface[i]).append("\" width=\"64\" height=\"64\"/>\n")
                            .append(TAB).append("</tileset>\n\n");
                    iterateur++;
                }
                
                for(int i = 0 ; i < blocsObjet.length ; i++) {
                    input.append(TAB).append("<tileset firstgid=\"").append(iterateur).append("\" name=\"").append(blocsObjet[i]).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n")
                            .append(TAB).append(TAB).append("<image source=\"../../").append(blocsObjet[i]).append("\" width=\"64\" height=\"64\"/>\n")
                            .append(TAB).append("</tileset>\n\n");
                    iterateur++;
                }
                
                
                int hauteur = getProfondeurGeneration();
                int largeur = (int) Math.round(0.4 * hauteur);
                
                input.append(TAB).append("<layer name=\"surface\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\">\n")
                        .append(TAB).append(TAB).append("<data encoding=\"csv\">\n");
                
                // Génération de la base de départ
                for(int i = 0 ; i < 3 ; i++ ) {
                    input.append(TAB).append(TAB);
                    for(int j = 0 ; j < largeur ; j++) {
                       input.append("0").append(",");
                    }
                    input.append("\n");
                }
                
                // Ici ajout de la matrice surface dans le String input
                for(int i = 3 ; i < hauteur ; i++) {
                   input.append(TAB).append(TAB);
                   for(int j = 0 ; j < largeur ; j++) {
                        // Ajout des infos
                        input.append((random.nextInt(blocsSurface.length)) + 1).append(",");
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
        // f(x) = 37.5*niveau*niveau-72.5*niveau+87.5
        // http://pastebin.com/XHebBKHj
        return (int) (Math.round(37.5*niveau*niveau-72.5*niveau+87.5)*10)/10;
    }
    
}

