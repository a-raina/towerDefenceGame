package towerDefense;

/**
 * Created by limj, weissb, rainaa.
 *
 * This is a Model class for a tower defense game. It stores all information about
 * the current game state while including an update method, serialization,
 * and getters and setters.
 */

import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Point2D;


/**
 * The Model class contains the location of the path the studentsGroup will be
 * traveling down to attack the base. It also has the user's current life
 * points, money, level, score and a list of the towers the user has created
 * and a list of all the studentsGroup traveling down the path to attack the base.
 */
public class Model {
    public final static int TILE_SIZE = 50;

    private int lifePoints;
    private int money;
    private int currentScore;
    private ArrayList<Integer> highScoreList;
    private int currentLevel;
    private ArrayList<TowerData> towerDataList;
    private ArrayList<StudentData> studentDataList;
    private ArrayList<PathData> pathDataList;
    private ArrayList<BackgroundData> backgroundDataList;
    private BaseData base;

    /**
     * Constructor for the Model
     */
    public Model() {
        this.lifePoints = 100;
        this.money = 200;
        this.currentScore = 0;
        this.currentLevel = 0;
        this.towerDataList = new ArrayList<TowerData>();
        this.studentDataList = new ArrayList<StudentData>();
        this.pathDataList = new ArrayList<PathData>();
        this.backgroundDataList = new ArrayList<BackgroundData>();
    }

    /**
     * This method saves the score of a game in the high score list if the score is high enough.
     *
     * @param newScore
     */
    public void saveHighScoreList(Integer newScore) {
        //sorting the list
        loadHighScoreList();

        Collections.sort(highScoreList);
        //replacing the last element with the new high Score nd sorting the new list
        if(newScore> highScoreList.get(highScoreList.size()-1)) {
            highScoreList.set(highScoreList.get(highScoreList.size() - 1), newScore);
            Collections.sort(highScoreList);
            uploadHighScore();
        }

    }
    /**
     * This method uploads the new highScore list to the file.
     */
    public void uploadHighScore(){
        try {
            File file = new File("highscoreslist.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0 ; i < highScoreList.size(); i++) {
                bw.write(highScoreList.get(i));
            }

            bw.close();
        } catch(Exception e) {
        }
    }

    /**
     * This method takes the number and velocity of students and adds it to the studentDataList.
     * @param numberOfStudents
     * @param newVelocity
     */
    public void generateStudentList(int numberOfStudents, int newVelocity) {
        for (int i = 0; i < numberOfStudents; i++) {
            this.addStudentData(this.pathDataList.get(1).getLocation().getX() + 25, -50 * (numberOfStudents+1));
        }
    }
    /**
     * This method returns a list of high scores from a a separate file.
     *
     * @return an Array List of Integers that represents the current high scores.
     */
    public void loadHighScoreList() {
        try {
            InputStream in = this.getClass().getResourceAsStream("highscoreslist.txt");
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                this.highScoreList.add(Integer.parseInt(line));
            }
            br.close();
            isr.close();
            in.close();
        } catch(Exception e) {
        }
    }

    /**
     * This method creates the coordinates for our path and saves the data in the pathDataList.
     */
    public void createPath() {
        for (int y = 0; y < Main.SCENE_LENGTH; y += this.TILE_SIZE) {
            PathData path = new PathData(350, y);
            this.pathDataList.add(path);
        }
    }

    public int getLifePoints() {
        return this.lifePoints;
    }

    public void setLifePoints(int newLifePoints) {
        this.lifePoints = newLifePoints;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int newMoney) {
        this.money = newMoney;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public void setCurrentScore(int newCurrentScore) {
        this.currentScore = newCurrentScore;
    }

    public int getCurrentLevel(){
        return this.currentLevel;
    }

    public ArrayList<Integer> getHighScoreList() {
        return this.highScoreList;
    }

    public void updateCurrentLevel() {
        this.currentLevel++;
    }

    public ArrayList<TowerData> getTowerDataList() {
        return this.towerDataList;
    }

    public void addTowerData(Point2D location) {
        TowerData tower = new TowerData(location);
        this.towerDataList.add(tower);
    }

    public void removeTowerData(TowerData oldTower) {
        this.towerDataList.remove(oldTower);
    }

    public ArrayList<StudentData> getStudentDataList() {
        return this.studentDataList;
    }
    public void addStudentData(double xCoordinate, double yCoordinate) {
        this.studentDataList.add(new StudentData(xCoordinate, yCoordinate));
    }
    public void removeStudentData(StudentData oldStudent) {
        this.studentDataList.remove(oldStudent);
    }

    public ArrayList<PathData> getPathDataList() {
        return this.pathDataList;
    }

    public void addPathData(PathData newPath) {
        this.pathDataList.add(newPath);
    }

    public void removePathData(PathData oldPath) {
        this.pathDataList.remove(oldPath);
    }

    public ArrayList<BackgroundData> getBackgroundDataList() {
        return this.backgroundDataList;
    }
    public void addBackgroundData(int x, int y) {
        BackgroundData tile = new BackgroundData(x, y, false);
        this.backgroundDataList.add(tile);
    }
    public void setBase(Point2D location) {
        base = new BaseData(location);
    }
    public Point2D getBase() {
        return this.base.getLocation();
    }

    /**
     * This class is responsible for saving all the data about the towers.
     */
    public class TowerData {
        private Point2D location;
        private String type;
        private String upgradeType;
        private final double KILL_RADIUS = 80;

        /**
         * This is the constructor for the tower class.
         * @param location
         */
        public TowerData(Point2D location) {
            this.location = location;
            this.type = type;
            this.upgradeType = null;
        }

        /**
         * This method checks whether the coordinates of a student are in range of a tower.
         * @param studentLocation
         * @return
         */
        public boolean isInRange(Point2D studentLocation) {
            double x1 = studentLocation.getX();
            double y1 = studentLocation.getY();
            double x2 = location.getX();
            double y2 = location.getY();
            double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            if (KILL_RADIUS >= distance) {
                return true;
            }
            else {
                return false;
            }
        }
        public Point2D getLocation(){
            return this.location;
        }
        public String getType(){
            return this.type;
        }
        public String getUpgradeType(){
            return this.upgradeType;
        }
        public void setUpgradeType(String upgradeType){
            this.upgradeType = upgradeType;
        }
    }

    /**
     * This class contains all the data for the students.
     */
    public class StudentData {
        private Point2D location;
        private String type;
        private double velocityX;
        private double velocityY;

        /**
         * This is the constructor for the student class.
         * @param xCoordinate
         * @param yCoordinate
         */
        public StudentData(double xCoordinate, double yCoordinate) {
            this.type = type;
            this.velocityX = 0;
            this.velocityY = 5;
            this.location = new Point2D(xCoordinate, yCoordinate);
        }
        public Point2D getLocation(){
            return this.location;
        }

        public void setLocation(double xCoordinate, double yCoordinate) {
            this.location = new Point2D(xCoordinate, yCoordinate);
        }
        public String getType(){
            return this.type;
        }

        /**
         * This method moves the coordinates of the student.
         */
        public void step() {
            this.setLocation(this.getLocation().getX() + this.velocityX,
                    this.getLocation().getY() + this.velocityY);
        }
        public double getVelocityX() {
            return velocityX;
        }
        public void setVelocityX(double velocityX) {
            this.velocityX = velocityX;
        }
        public double getVelocityY() {
            return velocityY;
        }
        public void setVelocityY(double velocityY) {
            this.velocityY = velocityY;
        }

    }

    /**
     * This is the class for the path data.
     */
    public class PathData {
        private Point2D location;
        private String type;

        /**
         * This is the constructor for the path.
         * @param x
         * @param y
         */
        public PathData(int x, int y) {
            this.location = new Point2D(x, y);
            this.type = null;
        }
        public Point2D getLocation(){
            return this.location;
        }
        public String getType(){
            return this.type;
        }
    }

    /**
     * This class contains the data for the background tiles.
     */
    public class BackgroundData {
        private Point2D location;
        private String type;
        private boolean hasTower;

        /**
         * This is the constructor for the background
         * @param x
         * @param y
         * @param hasTower
         */
        public BackgroundData(int x, int y, boolean hasTower) {
            this.hasTower = hasTower;
            this.location = new Point2D(x, y);
            this.type = null;
        }
        public Point2D getLocation(){
            return this.location;
        }
        public String getType(){
            return this.type;
        }

        /**
         * This method checks to see if the coordinates of a background tile matches the coordinates of a tower on it.
         * @return
         */
        public boolean isHasTower(){
            return this.hasTower;
        }

        /**
         * This method places the coordinates of a tower on the coordinates of a background tile.
         * @param hasTower
         */
        public void setHasTower(boolean hasTower){
            this.hasTower = hasTower;
        }
    }

    /**
     * This class contains the base data.
     */
    public class BaseData {
        private Point2D location;

        /**
         * This is the constructor for the base.
         * @param location
         */
        public BaseData(Point2D location) {
            this.location = location;
        }
        public Point2D getLocation(){
            return this.location;
        }
    }
}