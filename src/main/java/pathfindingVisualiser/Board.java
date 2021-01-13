package pathfindingVisualiser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO make it a singleton?
public class Board {
    private int width;
    private int height;
    private final Node[][] nodes;
    private Vector2 startNodePos;
    private Vector2 endNodePos;
    private static final List<NodeState> visitableNodeStates = Arrays.asList(NodeState.FREE, NodeState.BUSY, NodeState.END);
    private static final List<NodeState> pathNodeStates = Arrays.asList(NodeState.VISITED, NodeState.BUSY, NodeState.PATH);

    public Node getNodeAt(Vector2 pos){
        return nodes[pos.getY()][pos.getX()];
    }
    public Node getRandomNode(){
        return getNodeAt(Vector2.createRandom(width,height));
    }

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.nodes = new Node[height][width];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                nodes[y][x] = new Node(new Vector2(x,y));
                nodes[y][x].setBoard(this);
            }
        }
        setUpStartAndEnd();
        //TODO add a method setStartNode, setEndNode
    }
    private void setUpStartAndEnd(){    //TODO choose positions
        startNodePos = new Vector2(0, 0);
        endNodePos = new Vector2(width-1, height-1);
        getStartNode().setState(NodeState.START);
        getEndNode().setState(NodeState.END);
    }

    public void clearPath(){
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Node node = nodes[y][x];
                if (pathNodeStates.contains(node.getState())) {
                    node.reset();
                }
            }
        }
        resetStartAndEnd();
    }
    private void resetStartAndEnd(){
        getStartNode().reset();
        getStartNode().setState(NodeState.START);
        getEndNode().reset();
        getEndNode().setState(NodeState.END);
    }
    public void resetBoard() {  //not using clear Path to not make it 2*n^2
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Node node = nodes[y][x];
                if(pathNodeStates.contains(node.getState()) || node.getState() == NodeState.WALL){
                    node.reset();
                }
            }
        }
        resetStartAndEnd();
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                result.append(getNodeAt(new Vector2(x,y)).toCharacter());
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int getNodeCount() {
        return width * height;
    }
    public Node getStartNode(){
        return getNodeAt(startNodePos);
    }
    public Node getEndNode(){
        return getNodeAt(endNodePos);
    }

    public List<Node> getNodeNeighbours(Vector2 position) {
        List<Node> result = new ArrayList<Node>(4);
        for (Vector2 pos : position.getAdjacentPositions()) {
            if (pos.getX() >= 0 && pos.getX() < width
                    && pos.getY() >= 0 && pos.getY() < height) {
                Node neigh = getNodeAt(pos);
                if (visitableNodeStates.contains(neigh.getState())) {
                    result.add(neigh);
                }
            }
        }
        return result;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isInRange(Vector2 pos){
        return !(pos.getX() < 0 || pos.getX() >= getWidth() ||
                pos.getY() < 0 || pos.getY() >= getHeight());
    }
}
