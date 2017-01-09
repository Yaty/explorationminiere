/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.minexploration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.inventaire.Slot;
import com.mygdx.screens.GameScreen;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        File fichier = new File("./map/" + idPartie + "/map.tmx");
        try {
            fichier.createNewFile();
            FileWriter writer = new FileWriter(fichier, false);
            try {
                StringBuilder input = new StringBuilder();
                input.append("<!-- Carte générée aléatoirement le ").append(dateFormat.format(date)).append(" pour le niveau ").append(game.getLevel()).append(" -->\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"left-up\" width=\"").append(largeur).append("\" height=\"").append(hauteur).append("\" level=\"").append(game.getLevel()).append("\" tilewidth=\"64\" tileheight=\"64\" nextobjectid=\"2\">\n");
                
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
                
                writer.write(input.toString());
            } catch(IOException io1) {
                System.out.println("Erreur lors de l'écriture dans le fichier map.tmx : " + io1.getMessage());
            } finally {
                writer.close();
            }
        } catch(IOException io2) {
            System.out.println("Erreur lors de la création du fichier map.tmx : " + io2.getMessage());           
        }        
    }
    
    private void saveMineur() {
        // On récupère les informations dans des variables
        GameScreen gameScreen = (GameScreen) game.getScreen();
        Mineur mineur = gameScreen.getWorld().getMineur();
        String montantArgent = Integer.toString(mineur.getArgent());
        Vector2 vecteurPosition = mineur.getPosition();
        ArrayList<Slot> slots = mineur.getInventaire().getSlots();
        
        int niveau = game.getLevel();
        String nomPartie = game.getNomGame();
        int idPartie = gameScreen.getIdPartie();
        
        // Date du jour pour l'écrire en commentaire
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document= builder.newDocument();
            
            // <mineur> </mineur>
            final Element racine = document.createElement("mineur");
            document.appendChild(racine);
            
            // Ajout d'un commentaire
            final Comment commentaire = document.createComment("Sauvegarde du niveau " + niveau + " de la partie " + nomPartie + " le " + dateFormat.format(date) + ".");
            document.appendChild(commentaire);
            
            // <argent> X </argent>
            final Element argent = document.createElement("argent");
            argent.appendChild(document.createTextNode(montantArgent));
            racine.appendChild(argent);

            // <position><x>X</x><y>Y</y></position>
            final Element position = document.createElement("position");
            final Element positionX = document.createElement("x");
            positionX.appendChild(document.createTextNode(Float.toString(vecteurPosition.x)));
            final Element positionY = document.createElement("y");
            positionY.appendChild(document.createTextNode(Float.toString(vecteurPosition.y)));
            position.appendChild(positionX);
            position.appendChild(positionY);
            racine.appendChild(position);
            
            // Pour chaque slot de l'inventaire on sauvegarde le nom et le montant
            // <slot><nom>Bla</nom><montant>X</montant></slot>
            for(int i = 0 ; i < slots.size() ; i++) {
                if(slots.get(i).getItem() != null) { // Un slot vide possède un item null, on ne veut pas l'enregistrer
                    //System.out.println("DEBUG : " + slots.get(i).getItem() + " " + slots.get(i).getAmount());
                    Element slot = document.createElement("slot");
                    Element nomObjet = document.createElement("nom");
                    nomObjet.appendChild(document.createTextNode(slots.get(i).getItem().name()));
                    Element montantObjet = document.createElement("montant");
                    montantObjet.appendChild(document.createTextNode(Integer.toString(slots.get(i).getAmount())));
                    slot.appendChild(nomObjet);
                    slot.appendChild(montantObjet);
                    racine.appendChild(slot);
                }
            }
            
            // C'est la pioche que le mineur utilise actuellement
            // <pioche><nom>Bla</nom><vitesse>X</vitesse></pioche>
            final Element pioche = document.createElement("pioche");
            final Element nomPioche = document.createElement("nom");
            nomPioche.appendChild(document.createTextNode(mineur.getEquipement().getSlots().get(0).getItem().getTextureRegion()));
            pioche.appendChild(nomPioche);
            racine.appendChild(pioche);            
                
            // On prépare l'écriture
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StreamResult sortie = new StreamResult(new File("./map/" + idPartie + "/save.xml"));
            
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            // Ecriture
            transformer.transform(source, sortie);

        } catch (ParserConfigurationException ex) {
            Gdx.app.log("SauvegardeHandler", "Erreur lors de l'affectation du builder : " + ex);
        } catch (TransformerConfigurationException ex) {
            Gdx.app.log("SauvegardeHandler", "Erreur lors de l'affectation du transformer : " + ex);
        } catch (TransformerException ex) {
            Gdx.app.log("SauvegardeHandler", "Erreur lors de la transformation en XML : " + ex);
        }
    }
}
