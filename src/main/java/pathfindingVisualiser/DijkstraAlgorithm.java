package pathfindingVisualiser;

import java.util.PriorityQueue;

public class DijkstraAlgorithm implements BoardEditor{
    private PriorityQueue<Node> queue;
    private boolean done;

    @Override
    public void start(Board board) {    //TODO doesnt actually need the board, maybe no pathfinder does
        queue = new PriorityQueue<>();
        Node startNode = board.getStartNode();
        startNode.setDistance(0);
        relax(startNode);
        done = false;
    }

    private void relax(Node relaxNode){
        for(Node node: relaxNode.getNeighbours()){
            double newDistance = relaxNode.getDistance() + node.getWeight();
            if(newDistance < node.getDistance()) {
                node.setParent(relaxNode);
                node.setDistance(newDistance);
                node.setState(NodeState.BUSY);
                queue.add(node);
            }
        }
        if(relaxNode.getState() != NodeState.START){    //TODO add end node here as well
            relaxNode.setState(NodeState.VISITED);
        }
    }

    @Override
    public void step() {
        System.out.println(queue);
        Node node = queue.poll(); //returns null when queue empty
        if(node == null)done = true;    //TODO some status code? target found/target not found
        else{
            relax(node);
            if(node.getState() == NodeState.END){
                done = true;
            }
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
