package pathfindingVisualiser;

import java.util.ArrayList;
import java.util.List;

//TODO make it a singleton?
public class Board {
    private int width;
    private int height;
    private final Node[][] nodes;
    private final Vector2 startNodePos;
    private final Vector2 endNodePos;

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
        startNodePos = new Vector2(0, 0);
        endNodePos = new Vector2(width-1, height-1);
        getStartNode().setState(NodeState.START);
        getEndNode().setState(NodeState.END);
        //TODO add a method setStartNode, setEndNode
    }
    public void clearAfterPathfinder(){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Node node = nodes[y][x];
                if(node.getState() == NodeState.BUSY || node.getState() == NodeState.VISITED){
                    node.setState(NodeState.FREE);
                }
                node.setDistance(Double.POSITIVE_INFINITY);
            }
        }
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
                if (neigh.getState() == NodeState.FREE || neigh.getState() == NodeState.BUSY) { //TODO change the hardcoding
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
}
