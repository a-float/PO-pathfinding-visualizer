package pathfindingVisualiser;

import java.util.*;

//TODO make it a singleton?
public class Board {
    private int width;
    private int height;
//    private final Node[][] nodes;
    private final HashMap<Vector2, Node> nodes = new HashMap<>(100);
    private Vector2 startNodePos;
    private Vector2 endNodePos;
    private boolean isWeighted = false;
    private static final List<NodeState> visitableNodeStates = Arrays.asList(NodeState.FREE, NodeState.BUSY, NodeState.END);
    private static final List<NodeState> pathNodeStates = Arrays.asList(
            NodeState.VISITED, NodeState.BUSY, NodeState.PATH, NodeState.END, NodeState.START);

    public Node getNodeAt(Vector2 pos){
        return nodes.get(pos);
    }
    public Node getRandomNode(){
        return getNodeAt(Vector2.createRandom(width,height));
    }

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Node node = new Node(new Vector2(x,y));
                node.setBoard(this);
                nodes.put(node.getPosition(), node);
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
        for(Node node: nodes.values()){
            if (pathNodeStates.contains(node.getState())) {
                node.reset(false);
            }
        }
    }

    public void resetBoard(boolean clearWeights) {  //not using clear Path to not make it 2*n^2
        for(Node node: nodes.values()) {
            node.reset(clearWeights);
        }
    }

    public int getNodeCount() {
        return nodes.size();
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

    public boolean isInBounds(Vector2 pos){
        return !(pos.getX() < 0 || pos.getX() >= getWidth() ||
                pos.getY() < 0 || pos.getY() >= getHeight());
    }
    public void clearWeights(){
        for(Node node: nodes.values()){
            node.setWeight(1);
        }
        isWeighted = false;
    }
    public void randomizeWeights(){
        for(Node node: nodes.values()){
            if (VisualizationManager.random.nextInt(100) > VisualizationManager.WEIGHT_CHANGE) {
                int newWeight = VisualizationManager.random.nextInt(VisualizationManager.MAX_WEIGHT);
                node.setWeight(newWeight);
            }
        }
        isWeighted = true;
    }
    public boolean getIsWeighted(){
        return isWeighted;
    }
}
