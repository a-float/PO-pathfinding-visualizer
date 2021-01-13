package pathfindingVisualiser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AstarAlgorithm extends Pathfinder{
    private final HashMap<Node, Double> nodeValues = new HashMap<>();
    private Node endNode;
    Comparator<Node> nodeComparator = Comparator
            .comparing(nodeValues::get);

    private final PriorityQueue<Node> queue = new PriorityQueue<>(3, nodeComparator);

    @Override
    public void start(Board board) {    //TODO doesnt actually need the board, maybe no pathfinder does
        super.start(board);
        queue.clear();
        nodeValues.clear();
        startNode.setDistance(0);
        endNode = board.getEndNode();
        relax(startNode);
    }

    @Override
    void search() {
        Node node = queue.poll(); //returns null when the queue is empty
        if (node == null){  //the end node has not been found
            isSearching = false;
            System.out.print("No path exists.");
            done = true;
        }
        else {
            if (node.getState() == NodeState.END) {
                isSearching = false;
                isShowingPath = true;
                target = node;
                System.out.println("The end node has been found. Showing the path!");
            }
            else if(node.getState() != NodeState.VISITED){
                relax(node);
            }
        }
    }

    /**
     * @param a start node
     * @return manhattan distance between endNode and startNodes' positions
     */
    private int manhattanDistFromEnd(Node a){
        Vector2 diff = Vector2.subtract(endNode.getPosition(), a.getPosition());
        return diff.getX()*diff.getX() + diff.getY()*diff.getY();
    }

    private void relax(Node relaxNode){
        for(Node node: relaxNode.getNeighbours()){
            double newDistance = relaxNode.getDistance() + node.getWeight();
            if(newDistance < node.getDistance()) {
                node.setParent(relaxNode);
                node.setDistance(newDistance);
                if(!node.isStartOrEnd()) {
                    node.setState(NodeState.BUSY);
                }
                nodeValues.put(node, node.getDistance()+manhattanDistFromEnd(node));
                queue.add(node);
            }
        }
        if(!relaxNode.isStartOrEnd()){
            relaxNode.setState(NodeState.VISITED);
        }
    }
}
