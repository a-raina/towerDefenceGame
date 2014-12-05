package towerDefense;

/**
 * InGamePresenter.java
 * By: Ben Weiss, Anmol Rainaa, Justin Lim
 *
 * This class is our controller class for our project. It includes the all-important
 * update method which updates the view and the model.
 */


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class InGamePresenter {

    final private double FRAMES_PER_SECOND = 20.0;

    // Ratechecker is the way we get our towers to fire only sporadically
    private double rateChecker;
    private int levelChecker;
    public boolean hasWaveStarted = false;

    public Model model;

    // Store labels
    public Label waveLabel;
    public Label scoreLabel;
    public Label moneyLabel;
    public Label lifePointsLabel;

    // We need to store all the nodes in groups in order to know
    // which node is which. We use our inGameView class to do that,
    // It's not great code/construction
    public InGameView inGameView;
    public InGameView backgroundTiles;
    public InGameView pathTiles;
    public InGameView towerTiles;
    public InGameView studentsGroup;
    public InGameView base;
    public InGameView laserGroup;
    public boolean hasTowerBeenBuilt;
    public Timer timer;

    public InGamePresenter() {
    }
    /**
     * This method initializes our first wave as well as makes the many different
     * tiles that appear in our game.
     */
    public void initialize() {
        // Create timer and instantiate model
        this.model = new Model();

        // Set up background tiles for entire game
        for (int x = 250; x < Main.SCENE_WIDTH; x += Model.TILE_SIZE) {
            for (int y = 0; y < Main.SCENE_LENGTH; y += Model.TILE_SIZE) {
                this.model.addBackgroundData(x, y);
                backgroundTiles.createBackgroundTile(x, y);
            }
        }
        // Set up path
        model.createPath();
        ArrayList<Model.PathData> pathDataList = model.getPathDataList();
        for (Model.PathData path: pathDataList) {
            Point2D point = path.getLocation();
            pathTiles.createPathTile(point.getX(), point.getY());
        }
        // Create first wave of studentsGroup
        createWave();

        // Make base
        Point2D baseLocation = pathDataList.get((pathDataList.size() -1)).getLocation();
        this.model.setBase(baseLocation);
        this.base.createBase(baseLocation.getX()+ 6,baseLocation.getY());
    }

    /**
     * Creates the next wave of students and updates the model and the view.
     */
    public void createWave() {
        Random studentMultiplier = new Random();
        int NumberOfStudents = 5 * (studentMultiplier.nextInt(2) + 1) * (model.getCurrentLevel()+1);
        for (int i = 0; i < NumberOfStudents ; i++) {
            this.model.addStudentData(375, -50 * (i + 1));
            this.studentsGroup.createStudent(375, -50 * (i + 1));
        }
    }

    /**
     * Checks to see if you are DEAD
     * @return a boolean where false is you're not dead
     */
    public boolean checkLifePoints() {
        if (model.getLifePoints() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This sets up our animation timer to create our animations.
     */
    private void setUpAnimationTimer() {

        this.rateChecker = 0;
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {

                        // If you're still alive...
                        if (checkLifePoints()) {
                            if (model.getStudentDataList().isEmpty() && model.getLifePoints() > 0) {
                                timer.cancel();
                                hasWaveStarted = false;
                                model.updateCurrentLevel();
                                levelChecker = model.getCurrentLevel();
                                waveLabel.setText("Wave: " + Integer.toString(model.getCurrentLevel()));
                                createWave();
                            }
                            updateAnimation();
                            rateChecker++;
                        }
                        // If you're dead, quit and save the score
                        else {
                            inGameView.createLoseLabel(20, 300);
                            model.saveHighScoreList(model.getCurrentScore());
                        }
                    }
                });
            }
        };
        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer = new java.util.Timer();
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    /**
     * This is our behomoth of an update method. It updates the model and the view at the same time.
     */
    private void updateAnimation() {

        // Get list data needed for method
        ArrayList<Model.StudentData> studentDataList = this.model.getStudentDataList();
        ArrayList<Model.TowerData> towerDataList = this.model.getTowerDataList();
        ArrayList<Integer> removeIndices = new ArrayList<Integer>();

        //Get base info
        Point2D baseLocation = this.model.getBase();
        double baseX = baseLocation.getX();
        double baseY = baseLocation.getY();

        // Get all nodes in studentGroup
        ObservableList<Node> nodeList = this.studentsGroup.getChildren();
        boolean checkToRemove = false;
        int removeIndex = 0;
        for (int counter = 0; counter < studentDataList.size(); counter++) {

            // Update students in model
            studentDataList.get(counter).step();
            Point2D studentPoint = studentDataList.get(counter).getLocation();
            double studentX = studentDataList.get(counter).getLocation().getX();
            double studentY = studentDataList.get(counter).getLocation().getY();

            // Update students in view
            InGameView.Student student = (InGameView.Student) nodeList.get(counter);
            student.moveStudent(studentX, studentY);
            removeIndex = counter;

            // Now cycle through towers in order to shoot at students
            if (!towerDataList.isEmpty()) {
                for (int i = 0; i < this.model.getTowerDataList().size(); i++) {
                    if (towerDataList.get(i).isInRange(studentPoint) && (this.rateChecker % 20 == 0)) {
                        checkToRemove = true;
                        this.model.setMoney(this.model.getMoney() + 10);
                        this.model.setCurrentScore(this.model.getCurrentScore() + 50);
                        this.scoreLabel.setText("Score: " + Integer.toString(this.model.getCurrentScore()));
                        this.moneyLabel.setText("Money: " + Integer.toString(this.model.getMoney()));

                        break;
                    }
                }
            }

            // Check to see if student hits base
            if (studentY >= baseY && studentX >= baseX) {
                this.model.setLifePoints(this.model.getLifePoints() - 1);
                this.lifePointsLabel.setText("Life Points: " + Integer.toString(this.model.getLifePoints()));
                checkToRemove = true;
            }

            // If we should remove a student, add the index at which to remove it into the list.
            if (checkToRemove == true) {
                removeIndices.add(removeIndex);
            }
        }
        //If we have to remove a student, do so now after the for loop so the list is not corrupted.
        if (checkToRemove == true) {
            this.studentsGroup.getChildren().remove(this.studentsGroup.getChildren().get(0));
            this.model.removeStudentData(studentDataList.get(removeIndices.get(0)));
        }
    }

    /**
     * Handles the button click for returning to Main Menu. It loads the mainMenu.fxml file.
     * @throws IOException
     */
    public void switchToMainMenu() throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Main.scene = new Scene(newRoot, Main.SCENE_WIDTH, Main.SCENE_LENGTH);
        Main.stage.setScene(Main.scene);
    }

    /**
     * Handles the button click for building towers.
     */
    public void onBuildTower() {
        if (checkLifePoints()) {

            // If they broke they can't buy
            if (model.getMoney() < 100) {
                return;
            }
            // See if tower has already been built and make new cursor
            hasTowerBeenBuilt = false;
            Image image = readImage("tower.jpg");
            Main.root.setCursor(new ImageCursor(image));

            // Get data from model about towers and background tiles
            final ArrayList<Model.BackgroundData> backgroundDataList = this.model.getBackgroundDataList();

            // Cycle through background nodes in tile node list
            int counter = 0;
            for (Node backgroundNode : this.backgroundTiles.getChildren()) {
                final int subCounter = counter;
                // Create new event for each background tile
                backgroundNode.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    //handles the mouseclick for each event. If the tile clicked has not been built on, build on it.
                    public void handle(MouseEvent event) {
                        if (!backgroundDataList.get(subCounter).isHasTower() && hasTowerBeenBuilt == false) {
                            double x = backgroundDataList.get(subCounter).getLocation().getX();
                            double y = backgroundDataList.get(subCounter).getLocation().getY();

                            // Update tower/background information
                            model.addTowerData(new Point2D(x, y));
                            towerTiles.createTower(x + 5, y + 5);
                            model.getBackgroundDataList().get(subCounter).setHasTower(true);

                            // Reset cursor and boolean for tower having been created
                            Main.root.setCursor(Cursor.DEFAULT);
                            hasTowerBeenBuilt = true;

                            // Update money situation
                            model.setMoney(model.getMoney() - 100);
                            moneyLabel.setText("Money: " + Integer.toString(model.getMoney()));
                        }
                    }
                });
                counter++;
            }
        }
    }

    /**
     * Handles the button click for creating a new wave once the old wave ends
     */
    public void onStartWave() {
        if (!hasWaveStarted) {
            this.setUpAnimationTimer();
            hasWaveStarted = true;
            }
    }

    /**
     * This reads in the various images we use in our game.
     * @param fileName
     * @return the image read in from the jpeg file as an Image object
     */
    public Image readImage(String fileName) {
        Image image = null;
        try {
            InputStream in = this.getClass().getResourceAsStream(fileName);
            image = new Image(in);
            in.close();
        } catch(Exception e) {
        }
        return image;
    }
}