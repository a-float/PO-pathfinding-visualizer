package pathfindingVisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//its a singleton class //TODO should it be? Same with BoardEditors
public class VisualizationManager {
    public int stepsPerFrame;
    private Board board;        //TODO move it to a separate data class?
    private final static HashMap<String, BoardEditor> mazeGenerators= new HashMap<>();
    private final static HashMap<String, BoardEditor> pathfinders= new HashMap<>();
    public boolean isPerforming = false;
    private BoardEditor currentEditor;
    private static VisualizationManager INSTANCE;

    private VisualizationManager(){
        mazeGenerators.put("Random Maze", new RandomMazeGenerator());
        mazeGenerators.put("Recursive Maze", new RecursiveMazeGenerator());
        pathfinders.put("Dijkstra's algorithm", new DijkstraAlgorithm());
        pathfinders.put("A* algorithm", new AstarAlgorithm());
        pathfinders.put("DFS algorithm", new DfsAlgorithm());
        pathfinders.put("BFS algorithm", new BfsAlgorithm());
    }

    public static VisualizationManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new VisualizationManager();
        }
        return INSTANCE;
    }

    public void setBoard(Board board){
        this.stepsPerFrame = Math.max(1, board.getNodeCount()/500);
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

    public void init(String editorToStartName) {
        if(pathfinders.containsKey(editorToStartName)){
            currentEditor = pathfinders.get(editorToStartName);
        }
        else{
            currentEditor = mazeGenerators.get(editorToStartName);
        }
        currentEditor.start(board);
        System.out.println("starting "+editorToStartName);
        isPerforming = true;
    }
    public void resetBoard(){
        if(!isPerforming) {
            board.resetBoard();
        }
    }
    public void clearPath(){
        if(!isPerforming) {
            board.clearPath();
        }
    }

    public void step(){
        if(!currentEditor.isDone()){currentEditor.step();}
        else isPerforming = false;
    }

    public double getCellSize(Vector2 size){
        System.out.println(size+" from getCellSize");
        return Math.min(size.getX()/board.getWidth(), size.getY()/board.getHeight());
    }
}
