package pathfindingVisualiser;

import java.util.List;

public class Node implements Comparable<Node>{
    private NodeState state;
    private Vector2 position;
    private Board board;
    private Node parent;
    private int weight;

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

    //TODO below looks kind of bad, do i change it or no? remember to ask
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

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
