package pathfindingVisualiser;

import java.util.PriorityQueue;

public class DijkstraAlgorithm extends Pathfinder{
    private final PriorityQueue<Node> queue = new PriorityQueue<>();

    @Override
    public void start(Board board) {    //TODO doesnt actually need the board, maybe no pathfinder does
        super.start(board);
        queue.clear();
        startNode.setDistance(0);
        relax(startNode);
    }

    @Override
    void search() {
        Node node = queue.poll(); //returns null when queue empty
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
            } else {
                relax(node);
            }
        }
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
                queue.add(node);
            }
        }
        if(!relaxNode.isStartOrEnd()){
            relaxNode.setState(NodeState.VISITED);
        }
    }
}
