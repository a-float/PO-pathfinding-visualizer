package pathfindingVisualiser;

import javafx.scene.paint.Color;

import java.util.List;

public class Node implements Comparable<Node>{
    private NodeState state;
    private final Vector2 position; //not settable
    private Board board;
    private Node parent;
    private int weight;

    public void setPartOfWandererPath(boolean partOfWandererPath) {
        isPartOfWandererPath = partOfWandererPath;
    }

    private boolean isPartOfWandererPath = false;

    private double distance;

    public Node(Vector2 position){
        this.position = position;
        this.state = NodeState.FREE;
        this.weight = 1;
        this.distance = Double.POSITIVE_INFINITY;
    }

    public List<Node> getNeighbours(){
        return this.board.getNodeNeighbours(this.position);
    }

    public void setBoard(Board board){
        this.board = board;
    }
    public String toCharacter(){
        return this.state.toString();
    }
    public void setState(NodeState state){
        this.state = state;
    }
    public NodeState getState(){
        return this.state;
    }
    public Node getParent(){
        return parent;
    }
    public void setParent(Node parent){
        this.parent = parent;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.distance, o.getDistance());
    }
    public String toString(){
        return String.format("[%s, %s, %.2f, %d]",position, state, distance, weight);
    }
    public Vector2 getPosition() {
        return position;
    }

    public boolean isStartOrEnd() {
        return this.state == NodeState.END || this.state == NodeState.START;
    }


    public void reset(boolean clearWeights) {
        trySetState(NodeState.FREE);
        this.distance = Double.POSITIVE_INFINITY;
        this.parent = null;

        if(clearWeights) {
            this.weight = 1;
            this.isPartOfWandererPath = false;
        }
    }
    /**
     * If the node is a start or an end, its state does not change
     * Sets the node values do defaults values.
     */
    public void trySetState(NodeState state){
        if(!isStartOrEnd()){
            this.state = state;
        }
    }

    public Color getColor(){
        if(isPartOfWandererPath){
            return NodeState.colorMap.get(NodeState.WANDERER_PATH);
        }
        else if(state != NodeState.FREE){
            return NodeState.colorMap.get(state);
        }
        else{
            double interpolate = (double)weight/VisualizationManager.MAX_WEIGHT;
            return Color.WHITE.interpolate(NodeState.colorMap.get(state),interpolate);
        }
    }

    public void flipWall() {
        if(state == NodeState.WALL) {
            state = NodeState.FREE;
        }
        else{
            trySetState(NodeState.WALL);
        }
    }
}
