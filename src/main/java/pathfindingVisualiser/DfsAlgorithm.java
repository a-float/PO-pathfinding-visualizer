package pathfindingVisualiser;

import java.util.Stack;

public class DfsAlgorithm extends Pathfinder{
    private final Stack<Node> stack = new Stack<>();

    public DfsAlgorithm(){
        super(false);
    }

    @Override
    public void start(Board board, Node startNode) {
        stack.clear();
        super.start(board, startNode);
        startNode.setDistance(0);
        visit(startNode);
    }

    /**
     * Add the first nodes neighbour to the stack. Sets it as visited.
     * @param node node to visit
     */
    private void visit(Node node){
        node.trySetState(NodeState.VISITED);
        for(Node n: node.getNeighbours()){
            if(n.getState() != NodeState.VISITED){
                n.setParent(node);
                n.trySetState(NodeState.BUSY);
                n.setDistance(node.getDistance()+1);
                stack.push(n);
            }
        }
    }

    @Override
    protected void search() {
        if (!stack.isEmpty()){
            Node nodeToVisit = stack.pop();
            if (nodeToVisit.getState() == NodeState.END) {
                finishSearch(nodeToVisit);
            }
            else{
                visit(nodeToVisit);
            }
        }
        else{
            abandonSearch();
        }
    }
}
