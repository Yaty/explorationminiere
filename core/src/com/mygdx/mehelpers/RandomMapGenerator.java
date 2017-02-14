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
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class to generate a tiled map randomly
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class RandomMapGenerator {
    private final String blocksSurface[], blocksObjects[];
    private Grid caves, liquids;
    private final String pathToMap;
    private final int level;
    private final char TAB = (char) 9; // TABulation
    private final Random random;
    private int idDiamond, idGlowstone, idStone, idGrass, idDirt, idCoal, idEmerald, idGold, idIron, idLapis, idLava, idFog;
    private final LinkedList<Integer> idFlowers;
    
    /**
     * Create a random map generator, and create a map
     * @param blocksSurface array of blocks name in strings, those blocks are going to be in the surface layer
     * @param blocksObjects array of blocks name in strngs, those blocks are goind to be in the "objets" layer
     * @param pathToMap path to the .tmx which will contain the map
     * @param level the level to generate
     */
    public RandomMapGenerator(String blocksSurface[], String blocksObjects[], String pathToMap, int level) {
        this.blocksSurface = blocksSurface;
        this.blocksObjects = blocksObjects;
        this.idFlowers = new LinkedList();
        this.pathToMap = pathToMap;
        this.level = level;
        this.random = new Random();
        int i;
        for(i = 0 ; i < blocksSurface.length ; i++) {
            if (blocksSurface[i].equals("diamond_block.png")) idDiamond = i + 1;
            else if (blocksSurface[i].equals("dirt.png")) idDirt = i + 1;
            else if (blocksSurface[i].equals("glowstone.png")) idGlowstone = i + 1;
            else if (blocksSurface[i].equals("grass_side.png")) idGrass = i + 1;
            else if (blocksSurface[i].equals("stone.png")) idStone = i + 1;
            else if (blocksSurface[i].equals("coal_ore.png")) idCoal = i + 1;
            else if (blocksSurface[i].equals("emerald_ore.png")) idEmerald = i + 1;
            else if (blocksSurface[i].equals("gold_ore.png")) idGold = i + 1;
            else if (blocksSurface[i].equals("iron_ore.png")) idIron = i + 1;
            else if (blocksSurface[i].equals("lapis_ore.png")) idLapis = i + 1;
        }
        
        for(int j = 0 ; j < blocksObjects.length ; j++) {
            i++;
            if(blocksObjects[j].equals("lave.gif")) idLava = i;
            else if (blocksObjects[j].equals("fog.gif")) idFog = i;
            else if (blocksObjects[j].startsWith("flower")) idFlowers.add(i);
        }

        generer();
    }
    
    // Hard coded ...
    private int generateBlockByDepth(int depth) {
        int rand = random.nextInt(200);              // 201 valeurs possibles - 0 à 200
        if(depth < 20) {
            if(rand <= 19) return idStone;             // de 0 à 19
            else if (rand <= 24) return idGlowstone;    // de 20 à 24
            else if (rand <= 34) return idCoal;      // de 25 à 34
            else if (rand <= 39) return idIron;          // de 35 à 39
            else if (rand <= 44) return idGold;           // de 40 à 44
            else if (rand <= 47) return idLapis;        // de 45 à 47
            else if (rand <= 49) return idEmerald;     // de 48 à 49
            return idDirt;                             // le reste -> 75%
        } else if (depth < 50) {
            if(rand <= 19) return idStone;             // de 0 à 19 - 20%
            else if (rand <= 23) return idGlowstone;    // de 20 à 23 - 4%
            else if (rand <= 31) return idCoal;      // de 24 à 31 - 8%
            else if (rand <= 35) return idIron;          // de 32 à 35 - 4%
            else if (rand <= 39) return idGold;           // de 36 à 39 - 4%
            else if (rand <= 42) return idLapis;        // de 40 à 42 - 3%
            else if (rand <= 44) return idEmerald;     // de 43 à 44 - 2%
            return idDirt;                             // le reste -> + 50%           
        } else if (depth < 100) {
            if(rand <= 19) return idStone;             // de 0 à 19 - 20%
            else if (rand <= 22) return idGlowstone;    // de 20 à 22 - 3%
            else if (rand <= 28) return idCoal;      // de 23 à 28 - 6%
            else if (rand <= 31) return idIron;          // de 29 à 31 - 3%
            else if (rand <= 34) return idGold;           // de 32 à 34 - 3%
            else if (rand <= 36) return idLapis;        // de 35 à 36 - 2%
            else if (rand <= 38) return idEmerald;     // de 37 à 38 - 2%
            return idDirt;                             // le reste -> + 50%                      
        } else if (depth < 500) {
            if(rand <= 19) return idStone;             // de 0 à 19 - 20%
            else if (rand <= 21) return idGlowstone;    // de 20 à 21 - 2%
            else if (rand <= 24) return idCoal;      // de 22 à 24 - 4%
            else if (rand <= 27) return idIron;          // de 25 à 27 - 3%
            else if (rand <= 30) return idGold;           // de 28 à 30 - 3%
            else if (rand <= 32) return idLapis;        // de 31 à 32 - 2%
            else if (rand <= 34) return idEmerald;     // de 33 à 34 - 2%
            return idDirt;                             // le reste -> + 50%                
        } else { // >= 500
            if(rand <= 19) return idStone;             // de 0 à 19 - 20%
            else if (rand <= 20) return idGlowstone;    // de 20 à 20 - 1%
            else if (rand <= 23) return idCoal;      // de 21 à 23 - 4%
            else if (rand <= 26) return idIron;          // de 24 à 26 - 3%
            else if (rand <= 29) return idGold;           // de 27 à 29 - 3%
            else if (rand <= 31) return idLapis;        // de 30 à 31 - 2%
            else if (rand <= 32) return idEmerald;     // de 32 à 32 - 1%
            return idDirt;                             // le reste -> + 50%                 
        }
    }
    
    /**
     * Thid method generate a .tmx which contains a map
     * Surfaces blocks are in png, objets blocks are in gif format
     * Rules :
     * - 3 empty lines
     * - 
     */
    private void generer() {
        FileHandle fichier = Gdx.files.local(pathToMap);
        final int mapHeight = getProfondeurGeneration();
        final int mapWidth = (int) Math.round(0.7 * mapHeight);
        
        caves = genererGrid(mapHeight, mapWidth, 0.515f, 2, 13, 12, 15); // https://github.com/czyzby/noise4j
        liquids = genererGrid(mapHeight, mapWidth, 0.65f, 2, 13, 9, 6);
        
        StringBuilder input = new StringBuilder();
        int iterator = 1;
        input.append("<!-- Carte générée aléatoirement pour le niveau ").append(level).append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"").append(mapWidth).append("\" height=\"").append(mapHeight).append("\" level=\"").append(level).append("\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");

        for (String blocsSurface1 : blocksSurface) {
            input.append(TAB).append("<tileset firstgid=\"").append(iterator).append("\" name=\"").append(blocsSurface1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../").append(blocsSurface1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
            iterator++;
        }

        for (String blocsObjet1 : blocksObjects) {
            input.append(TAB).append("<tileset firstgid=\"").append(iterator).append("\" name=\"").append(blocsObjet1).append("\" tilewidth=\"64\" tileheight=\"64\" tilecount=\"1\" columns=\"1\">\n").append(TAB).append(TAB).append("<image source=\"../").append(blocsObjet1).append("\" width=\"64\" height=\"64\"/>\n").append(TAB).append("</tileset>\n\n");
            iterator++;
        }


        input.append(TAB).append("<layer name=\"surface\" width=\"").append(mapWidth).append("\" height=\"").append(mapHeight).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Génération de la base de départ
        for(int i = 0 ; i < 3 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
               input.append("0,");
            }
            input.append("\n");
        }

        input.append(TAB).append(TAB);
        for(int i = 0 ; i < mapWidth ; i++) {
            input.append(idGrass).append(",");
        }
        input.append("\n");    

        int diamondPositionX = random.nextInt(mapWidth-1);
        int posDiamMaxY = mapHeight-1;
        int posDiamMinY = (int) (mapHeight*0.8f);
        int diamondPositionY = random.nextInt(posDiamMaxY-posDiamMinY)+posDiamMinY;

        // Ici ajout de la matrice surface dans le String input
        for(int i = 4 ; i < mapHeight ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
                if(diamondPositionX == j && diamondPositionY == i)
                    input.append(idDiamond).append(',');
                else {
                    if(caves.get(j, i) == 1f) {
                        input.append(0).append(',');
                    } else if (liquids.get(j, i) == 1f) {
                        input.append(0).append(',');
                    } else // Si on est pas une cave ou pas dans une zone de liquide = c'est un bloc
                        input.append(generateBlockByDepth(i)).append(',');
                }
            }
            input.append("\n");
        } 
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n\n");
        input.append(TAB).append("<layer name=\"objets\" width=\"").append(mapWidth).append("\" height=\"").append(mapHeight).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");

        // Ajout de la matrice objet dans le String input (vide)

        for(int i = 0 ; i < 2 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
                input.append("0,");
            }
            input.append("\n");
        }

        input.append(TAB).append(TAB);
        for(int j = 0 ; j < mapWidth ; j++) {
           input.append(getAFlower()).append(",");
        }
        input.append("\n");

        for(int i = 3 ; i < mapHeight ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
                if(liquids.get(j, i) == 1f)
                    input.append(idLava).append(',');
                else
                    input.append("0,");
            }
            input.append("\n");
        }
        input.delete(input.length()-2, input.length()); // On enlève la virgule en trop        

        input.append("\n").append(TAB).append(TAB).append("</data>\n").append(TAB).append("</layer>\n");
        
        // AJOUT DE LA MATRICE FOG
        input.append("\n").append(TAB).append("<layer name=\"fog\" width=\"").append(mapWidth).append("\" height=\"").append(mapHeight).append("\">\n").append(TAB).append(TAB).append("<data encoding=\"csv\">\n");
        for(int i = 0 ; i < 3 ; i++ ) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
               input.append("0,");
            }
            input.append("\n");
        }
        
        for(int i = 3 ; i < mapHeight ; i++) {
            input.append(TAB).append(TAB);
            for(int j = 0 ; j < mapWidth ; j++) {
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
        return (int) (Math.round(1.19*level*level+13.86)*10)/10;
    }

    private String getAFlower() {
        Random rand = new Random();
        int nb = rand.nextInt(99);
        if(nb < 10) return String.valueOf(idFlowers.get(rand.nextInt(idFlowers.size()-1))); // 10% de fleurs
        else return "0";
    }
    
    /**
     * See https://github.com/czyzby/noise4j
     * @param height
     * @param width
     * @param f
     * @param i
     * @param i0
     * @param i1
     * @param i2
     * @return a grid fiiled with 0 and 1
     */
    private Grid genererGrid(int height, int width, float f, int i, int i0, int i1, int i2) {
        final Grid grid = new Grid(width, height);
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