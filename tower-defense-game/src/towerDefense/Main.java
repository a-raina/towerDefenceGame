package towerDefense;

/**
 * towerDefense/Main.java
 * By: Anmol Raina, Justin Lim, Ben Weiss
 *
 *Sets up the program to run and loads the main menu fxml file. Creates a stage to load our screen on and a root node
 * which sprouts all other nodes.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    public static Stage stage;
    public static Scene scene;
    public static Parent root;
    public final static int SCENE_WIDTH = 750;
    public final static int SCENE_LENGTH = 500;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        primaryStage.setTitle("TOWER DEFENSE");
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_LENGTH);
        primaryStage.setScene(this.scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        stage = primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
