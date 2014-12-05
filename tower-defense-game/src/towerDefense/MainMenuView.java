package towerDefense;
/**
 * MainMenuView.java
 * By: Ben Weiss, Anmol Raina, Justin Lim
 *
 * This file is our view class for our main menu. It implements all graphics
 * users see within our main menu.
 */


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainMenuView extends Group {
    // Boolean check to prevent multiple ReadBoxes from being created.
    private boolean doesReadBoxExist;

    /**
     * Empty constructor
     */
    public MainMenuView() {
    }

    /**
     * Instantiates a new ReadBox if no other ReadBoxes have been made.
     * Takes in a file in order to display the desired text
     * @param readFile
     */
    public void createReadBox(String readFile) {
        if (!doesReadBoxExist) {
            ReadBox test = new ReadBox(readFile);
            this.getChildren().add(test);
            doesReadBoxExist = true;
        }
    }
    /**
     * Removes the ReadBox from screen.
     * Takes in a node in order to delete it from the screen.
     * @param box
     */
    public void removeReadBox(Node box) {
        this.getChildren().remove(box);
        doesReadBoxExist = false;
    }
    private class ReadBox extends Parent {

        /**
         * Constructor for ReadBox.
         * Takes in a file in order to display it as text.
         * @param file
         */
        public ReadBox(String file) {

            // Make rectangle box to display text on
            Rectangle box = new Rectangle(125, 75, 500, 350);
            box.setFill(Color.GREEN);
            box.setStroke(Color.BLACK);
            box.setStrokeWidth(5.0);
            this.getChildren().add(box);

            // Make exit button with event handler
            final Button exitButton = new Button("Exit");
            exitButton.setLayoutX(125);
            exitButton.setLayoutY(75);
            this.getChildren().add(exitButton);

            //Creates a new event when the exit button on the read box is clicked. It deletes the current node from
            //tree and goes back to the source.
            exitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removeReadBox(exitButton.getParent());
                }
            });

            // Read in file and make text object
            MainMenuPresenter test = new MainMenuPresenter();
            String text = test.readFile(file);
            final Text displayText = new Text(175, 120, text);
            displayText.setWrappingWidth(300);
            this.getChildren().add(displayText);
        }
    }
}
