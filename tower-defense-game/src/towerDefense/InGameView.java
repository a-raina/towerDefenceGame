package towerDefense;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

/**
 * Created by rainaa, weissb, and limju on 11/13/14.
 *
 * This class is where all of our objects are drawn. Coordinates for objects are passed in from the presenter
 * and the objects are drawn and connected as nodes to groups of that object. Those groups are stored as nodes
 * in the larger inGameView class.
 */
public class InGameView extends Group {
    private Model model;

    public InGameView() {
    }

    /**
     * This method creates a group of background tile object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createBackgroundTile(int xCoordinate, int yCoordinate) {
        BackgroundTile background = new BackgroundTile(xCoordinate, yCoordinate);
        this.getChildren().add(background);
    }
    /**
     * This method creates a group of path tile object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createPathTile(double xCoordinate, double yCoordinate) {
        PathTile path = new PathTile(xCoordinate, yCoordinate);
        this.getChildren().add(path);
    }
    /**
     * This method creates a group of student object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createStudent(double xCoordinate, double yCoordinate) {
        Student student = new Student(xCoordinate, yCoordinate);
        this.getChildren().add(student);
    }
    /**
     * This method creates a group of tower object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createTower(double xCoordinate, double yCoordinate) {
        Tower tower = new Tower(xCoordinate, yCoordinate);
        this.getChildren().add(tower);
    }
    /**
     * This method creates a group of base object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createBase(double xCoordinate, double yCoordinate) {
        Base base = new Base(xCoordinate, yCoordinate);
        this.getChildren().add(base);
    }
    /**
     * This method creates a group of label object and adds it to the inGameView group.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void createLoseLabel(double xCoordinate, double yCoordinate) {
        LoseLabel loseLabel = new LoseLabel(xCoordinate, yCoordinate);
        this.getChildren().add(loseLabel);
    }

    /**
     * This class is responsible for drawing the towers.
     */
    private class Tower extends Group {
        /**
         * This constructor draws each individual tower and adds it to the tower group.
         *
         * @param xCoordinate
         * @param yCoordinate
         */
        public Tower(double xCoordinate, double yCoordinate) {
            // Tower image is passed in.
            InGamePresenter test = new InGamePresenter();
            Image image = test.readImage("tower.jpg");
            // Tower image is resized.
            ImageView resizedImage = new ImageView(image);
            resizedImage.setFitHeight(Model.TILE_SIZE - 10);
            resizedImage.setFitWidth(Model.TILE_SIZE - 10);
            // Tower image position is set.
            resizedImage.setX(xCoordinate);
            resizedImage.setY(yCoordinate);
            // Tower image is added to tower group as node.
            this.getChildren().add(resizedImage);
        }
    }

    /**
     * This class is responsible for drawing the students.
     */
    public class Student extends Group {
        private ImageView resizedImage;
        /**
         * This constructor draws draws each individual student and adds it to the student group.
         *
         * @param xCoordinate
         * @param yCoordinate
         */
        public Student(double xCoordinate, double yCoordinate) {
            // Student image is passed in.
            InGamePresenter test = new InGamePresenter();
            Image image = test.readImage("student.jpg");
            // Student image is resized.
            this.resizedImage = new ImageView(image);
            this.resizedImage.setFitHeight(Model.TILE_SIZE/3);
            this.resizedImage.setFitWidth(Model.TILE_SIZE/3);
            // Student image position is set.
            this.resizedImage.setX(xCoordinate);
            this.resizedImage.setY(yCoordinate);
            // Student image is added to student group as node.
            this.getChildren().add(resizedImage);
        }
        /**
         * This method moves the student based on coordinates given as parameters.
         *
         * @param xCoordinate
         * @param yCoordinate
         */
        public void moveStudent(double xCoordinate, double yCoordinate) {
            this.resizedImage.setX(xCoordinate);
            this.resizedImage.setY(yCoordinate);
        }
    }

    /**
     * This class is responsible for drawing the path tiles.
     */
    private class PathTile extends Group {
        /**
         * This constructor draws each individual path tile and adds it to the path group.
         *
         * @param xCoordinate
         * @param yCoordinate
         */
        public PathTile(double xCoordinate, double yCoordinate) {
            Rectangle pathTile = new Rectangle(xCoordinate, yCoordinate, Model.TILE_SIZE, Model.TILE_SIZE);
            pathTile.setFill(Color.BROWN);
            this.getChildren().add(pathTile);
        }
    }

    /**
     * This class is responsible for drawing the background tiles.
     */
    private class BackgroundTile extends Group {
        /**
         * This constructor draws each individual background tile and adds it to the background group.
         *
         * @param xCoordinate
         * @param yCoordinate
         */
        public BackgroundTile(int xCoordinate, int yCoordinate) {
            Rectangle backgroundTile = new Rectangle(xCoordinate, yCoordinate, Model.TILE_SIZE, Model.TILE_SIZE);
            backgroundTile.setFill(Color.GREEN);
            backgroundTile.setStroke(Color.BLACK);
            backgroundTile.setStrokeWidth(1.0);
            this.getChildren().add(backgroundTile);
        }
    }

    /**
     * This class is responsible for drawing the base.
     */
    private class Base extends Group {
        /**
         * This constructor draws the base image and adds it to the base group.
         * @param xCoordinate
         * @param yCoordinate
         */
        public Base(double xCoordinate, double yCoordinate) {
            InGamePresenter test = new InGamePresenter();
            // Base image is passed in.
            Image image = test.readImage("base.jpg");
            // Base image is resized.
            ImageView resizedImage = new ImageView(image);
            // Base image position is set.
            resizedImage.setX(xCoordinate);
            resizedImage.setY(yCoordinate);
            // Base image is added to base group as node.
            this.getChildren().add(resizedImage);
        }
    }

    /**
     * This class is responsible for drawing the label at the end of the game.
     */
    private class LoseLabel extends Group {
        /**
         * This constructor draws a label when the user's life points reach 0.
         * @param xCoordinate
         * @param yCoordinate
         */
        public LoseLabel(double xCoordinate, double yCoordinate) {
            Label label = new Label("You Lose." + "\n" + "Exit to the Main Menu and try again!");
            label.setLayoutX(xCoordinate);
            label.setLayoutY(yCoordinate);
            this.getChildren().add(label);
        }

    }
}
