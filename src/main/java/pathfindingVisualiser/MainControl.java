package pathfindingVisualiser;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MainControl implements Initializable {
    private Timeline timer;
    private final Map<String, Vector2> boardSizes = Map.of(     //TODO items out of order
            "small (14x7)", new Vector2(14,8),
            "medium (28x16)", new Vector2(28,16),
            "big (49x28)", new Vector2(49,28),
            "large (70x40)", new Vector2(70,40),
            "small square (15x15)", new Vector2(15,15),
            "bigger square (30x30)", new Vector2(30,30)
            );


    @FXML
    Canvas boardCanvas;
    @FXML
    ChoiceBox<String> algChoiceBox, mazeChoiceBox;
    private VisualizationManager manager;
    private boolean isPlaying = false;
    private double cellSize;
    private boolean changingToWalls = false;

    @FXML
    StackPane canvasParentPane;
    @FXML
    Menu boardSizesMenu;

    @FXML
    private void canvasPressed(MouseEvent event){
        Vector2 mousePos = getMousePosition(event);
        if(manager.getBoard().isInBounds(mousePos)) {
            changingToWalls = manager.flipWall(getMousePosition(event));
            showBoard(true);
        }
    }
    @FXML
    private void canvasDragged(MouseEvent event){
        manager.flipToWall(getMousePosition(event), changingToWalls);
        //if walls get turned into free, there might be a black halo, around the place where the wall used to be
        showBoard(!changingToWalls);
    }

    private Vector2 getMousePosition(MouseEvent event){
        int x = (int) Math.floor(event.getX() / cellSize);
        int y = (int) Math.floor(event.getY() / cellSize);
        return new Vector2(x,y);
    }

    @FXML
    private void playPause(ActionEvent event){
        if(isPlaying)timer.pause();
        else timer.play();
        isPlaying = !isPlaying;
    }

    @FXML
    private void step(ActionEvent event){
        //the animation is not paused and there is an active board editor
        if(isPlaying) {
            if(manager.isPerforming) {
                for(int i = 0; i < manager.stepsPerFrame; i++) {
                    manager.step();
                }
                showBoard(false);
            }
            else{
                playPause(new ActionEvent());
            }
        }
    }

    @FXML
    private void performOneStep(ActionEvent event){
        if(manager.isPerforming){
            manager.step();
            showBoard(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = VisualizationManager.getInstance();
        manager.setBoard(new Board(63,36)); //board ratio is about 7:4
        cellSize = calcCellSize();  //need to be before setUpCanvas size, as the second uses callSize
        setUpBoardMenuItem();

        mazeChoiceBox.getItems().addAll(manager.getMazeNames());
        algChoiceBox.getItems().addAll(manager.getAlgsNames());
        createTimer(0.05);

        Platform.runLater(
            () -> boardCanvas.getScene().addEventHandler(KeyEvent.KEY_PRESSED,
                    (key) -> handleKeyPress(key))
        );

        Platform.runLater(this::setUpCanvasSize);
        Platform.runLater(() -> showBoard(false));
    }

    private void handleKeyPress(KeyEvent key) {
        if(manager.handleKeyPress(key)){
            showBoard(true);
        }
    }

    private double calcCellSize() {
        Bounds bounds = boardCanvas.getBoundsInLocal();
        return manager.getMaxCellSize(new Vector2((int)bounds.getWidth(), (int)bounds.getHeight()));
    }

    private void setUpBoardMenuItem() {
        ToggleGroup toggleGroup = new ToggleGroup();
        for(String str: boardSizes.keySet()){
            RadioMenuItem newItem = new RadioMenuItem(str);
            newItem.setOnAction(e -> {
                changeCanvasSize(newItem.getText());
            });
            toggleGroup.getToggles().add(newItem);
            boardSizesMenu.getItems().add(newItem);
        }
    }

    private void changeCanvasSize(String sizeName){
        manager.changeBoardSize(boardSizes.get(sizeName));
        setUpCanvasSize();
        cellSize = calcCellSize();
        showBoard(true);
    }

    private void setUpCanvasSize(){
        //get parent size
        Vector2 parentSize = Vector2.zero();
        parentSize.setX((int)canvasParentPane.getWidth());
        parentSize.setY((int)canvasParentPane.getHeight());
        System.out.println(parentSize);

        cellSize = manager.getMaxCellSize(parentSize);
        System.out.println(cellSize);
        //apply it to the canvas
        boardCanvas.setWidth(Math.round(manager.getBoard().getWidth()*cellSize));
        boardCanvas.setHeight(Math.round(manager.getBoard().getHeight()*cellSize));
    }

    private void createTimer(double seconds) {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timeline(new KeyFrame(Duration.seconds(seconds), this::step));
        timer.setCycleCount(Timeline.INDEFINITE);
        if(isPlaying)timer.play();
    }

    private void startGeneration(ChoiceBox<String> generatorChoiceBox){ //TODO ChoiceBox should hold BoardEditors
        if(!manager.isPerforming) {
            System.out.println(generatorChoiceBox.getValue());
            if(generatorChoiceBox.getValue()!= null) {
                manager.init(generatorChoiceBox.getValue());
                playPause(new ActionEvent());   //should the generation autostart
            }
        }
    }

    public void showBoard(boolean withClear) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        if(withClear) {
            Bounds bounds = boardCanvas.getBoundsInLocal();
            gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
        }
        Board board = manager.getBoard();
        for(int x = 0; x < board.getWidth(); x++){
            for(int y = 0; y < board.getHeight(); y++){
                Node node = board.getNodeAt(new Vector2(x,y));
                gc.setFill(node.getColor());
                //the magic numbers help prevent small gaps between cells
                gc.fillRect(node.getPosition().getX()*cellSize-0.25, node.getPosition().getY()*cellSize-0.25, cellSize+0.5, cellSize+0.5);
            }
        }
    }

    @FXML
    private void startPathfinding(ActionEvent event){
        manager.clearPath();
        startGeneration(algChoiceBox);
    }
    @FXML
    private void startMazeGeneration(ActionEvent event){
        manager.resetBoard(false);
        startGeneration(mazeChoiceBox);
    }
    @FXML
    private void clearPath(ActionEvent event){
        manager.clearPath();
        showBoard(true);
    }
    @FXML
    private void resetBoard(ActionEvent event){
        manager.resetBoard(true);
        showBoard(true);
    }
    @FXML
    private void setWeights(ActionEvent event){
        manager.setWeights();
        showBoard(false);
    }
    @FXML
    private void clearWeights(ActionEvent event){
        manager.clearWeights();
        showBoard(true);
    }
    @FXML
    private void clearWandererPath(ActionEvent event){
        manager.clearWandererPath();
        showBoard(true);
    }
}
