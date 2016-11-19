/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.minexploration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Inventaire;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.screens.GameScreen;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    
    public void save() {
        System.out.println("Je save !");
        GameScreen gameScreen = (GameScreen) game.getScreen();
        Mineur mineur = gameScreen.getWorld().getMineur();
        String montantArgent = Integer.toString(mineur.getArgent());
        Vector2 vecteurPosition = mineur.getPosition();
        Inventaire inventaire = mineur.getInventaire();
        int niveau = game.getLevel();
        String nomPartie = game.getNomGame();
        int idPartie = gameScreen.getIdPartie();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            System.out.println("Je save 2 !");
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document= builder.newDocument();
            
            final Comment commentaire = document.createComment("Sauvegarde du niveau " + niveau + " de la partie " + nomPartie + " le " + dateFormat.format(date) + ".");
            document.appendChild(commentaire);
            
            final Element racine = document.createElement("mineur");
            document.appendChild(racine);
            
            final Element argent = document.createElement("argent");
            argent.appendChild(document.createTextNode(montantArgent));
            racine.appendChild(argent);

            final Element position = document.createElement("position");
            final Element positionX = document.createElement("x");
            positionX.appendChild(document.createTextNode(Float.toString(vecteurPosition.x)));
            final Element positionY = document.createElement("y");
            positionY.appendChild(document.createTextNode(Float.toString(vecteurPosition.y)));
            position.appendChild(positionX);
            position.appendChild(positionY);
            racine.appendChild(position);            
                
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StreamResult sortie = new StreamResult(new File("./map/" + idPartie + "/save.xml"));
            
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
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
