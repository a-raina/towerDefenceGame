package towerDefense;

/**
 * MainMenuPresenter.java
 * By: Ben Weiss, Anmol Raina, Justin Lim
 *
 * This file is the presenter for the main menu view. It  presents all the objects we create in the mainMenu.fxml.
 *
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;

public class MainMenuPresenter {
    public MainMenuView mainMenuView;

    //Constructor for the MainMenuPresenter class
    public MainMenuPresenter() {
    }

    /**
     *The next four methods are for instantiating what happens when the corresponding button is clicked. When the
     * button is clicked the corresponding text file is open through readBox.
     * @param actionEvent
     */

    public void onHowToPlayClick(ActionEvent actionEvent) {
            mainMenuView.createReadBox("howtoplay.txt");
    }

    public void onHighScoresClick(ActionEvent actionEvent) {
            mainMenuView.createReadBox("highscoreslist.txt");
    }

    public void onGameLoreClick() {
            mainMenuView.createReadBox("lore.txt");
    }

    public void onContactUsClick(ActionEvent actionEvent) {
            mainMenuView.createReadBox("contactus.txt");
    }

    //This method creates a new scene and takes us to the actual game screen. It also loads a new fxml file
    public void switchToInGame() throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("inGame.fxml"));
        Scene newScene = new Scene(Main.root, Main.SCENE_WIDTH, Main.SCENE_LENGTH);
        Main.stage.setScene(newScene);
    }

    /**
     * This method reads in the file being passed and stores the contents inside a string.
     * @param fileName
     * @return a string containing the content of the file
     */
    public String readFile(String fileName) {

        String returnString = "";
        try {
            InputStream in = this.getClass().getResourceAsStream(fileName);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                returnString += line;
            }
            br.close();
            isr.close();
            in.close();
        } catch(Exception e) {
            // forget it. This exception should not thrown.
        }
        return returnString;
    }
}
