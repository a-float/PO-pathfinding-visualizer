package pathfindingVisualiser;

import java.util.Stack;

public class DfsAlgorithm extends Pathfinder{
    private final Stack<Node> stack = new Stack<>();

    @Override
    public void start(Board board) {
        stack.clear();
        super.start(board);
        visit(startNode);
    }

    private void visit(Node node){
        if(!node.isStartOrEnd()) {
            node.setState(NodeState.VISITED);
        }
        for(Node n: node.getNeighbours()){
            if(n.getState() != NodeState.BUSY && n.getState() != NodeState.VISITED){
                n.setParent(node);
                if(!n.isStartOrEnd()) {
                    n.setState(NodeState.BUSY);
                }
                stack.push(n);
            }
        }
    }

    @Override
    void search() {
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
            cancelSearch();
        }
    }
}
