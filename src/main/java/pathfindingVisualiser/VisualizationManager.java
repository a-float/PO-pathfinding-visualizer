package pathfindingVisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//its a singleton class //TODO should it be? Same with BoardEditors
public class VisualizationManager {
    private Board board;        //TODO move it to a separate data class?
    private final static HashMap<String, BoardEditor> mazeGenerators= new HashMap<>();
    private final static HashMap<String, BoardEditor> pathfinders= new HashMap<>();
    public boolean isPerforming = false;
    private BoardEditor currentEditor;
    private static VisualizationManager INSTANCE;

    private VisualizationManager(){
        mazeGenerators.put("Random Maze", new RandomMazeGenerator());
        pathfinders.put("Dijkstra's algorithm", new DijkstraAlgorithm());
    }

    public static VisualizationManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new VisualizationManager();
        }
        return INSTANCE;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
    public List<String> getMazeNames(){
        return new ArrayList<>(mazeGenerators.keySet());
    }
    public List<String> getAlgsNames(){
        return new ArrayList<>(pathfinders.keySet());
    }

    public void init(String algToPerform) {
        currentEditor = pathfinders.get(algToPerform);
        currentEditor.start(board);
        System.out.println("starting "+algToPerform);
        isPerforming = true;
    }
    public void step(){
        if(!currentEditor.isDone()){currentEditor.step();}
        else isPerforming = false;
    }
}
