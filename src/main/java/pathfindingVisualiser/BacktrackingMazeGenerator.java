package pathfindingVisualiser;

import java.util.*;

public class BacktrackingMazeGenerator extends MazeGenerator{
    private boolean isSettingTheGridUp;
    private final Stack<Node> stack = new Stack<>();
    private final Queue<Node> wallsToBuild = new LinkedList<>();
    private Node prevNode;


    private ArrayList<Node> getFarNeighbours(Node node){
        ArrayList<Node> result = new ArrayList<>(4);
        Vector2 newVec;
        Vector2 pos = node.getPosition();
        for(int dx = -2; dx <= 2; dx+=2){
            for(int dy = -2; dy <= 2; dy+=2){
                if(dx == 0 || dy == 0) {
                    newVec = new Vector2(pos.getX() + dx, pos.getY() + dy);
                    if(board.isInBounds(newVec)){
                        Node farNode = board.getNodeAt(newVec); //TODO a lot of hardcoded constraints
                        if(farNode.getState() != NodeState.WALL && farNode.getState() != NodeState.VISITED &&
                                !farNode.isStartOrEnd()){
                           result.add(board.getNodeAt(newVec));
                        }
                    }
                }
            }
        }
        return result;
    }

    private void setUpWallsToBuild(){
        int firstFullColumn = (startNode.getPosition().getX()) % 2;
        for(int x = 0 ; x < board.getWidth(); x+=1){
            for(int y = 0 ; y < board.getHeight(); y+=1){
                if((x-firstFullColumn)%2 == 0) {
                    y++;
                }
                wallsToBuild.add(board.getNodeAt(new Vector2(x, y)));
            }
        }
    }

    private Node getNodeBetween(Node a, Node b){
        Vector2 middlePos = new Vector2((a.getPosition().getX() + b.getPosition().getX())/2,
                                        (a.getPosition().getY() + b.getPosition().getY())/2);
        return board.getNodeAt(middlePos);
    }

    private void visit(Node node){
        if(node.getParent() != null){
            getNodeBetween(node, node.getParent()).trySetState(NodeState.VISITED);
        }
        node.trySetState(NodeState.VISITED);
        List<Node> nodes = getFarNeighbours(node);
        Node chosenNode;
        if(nodes.size() > 0){
            chosenNode = nodes.get(VisualizationManager.random.nextInt(nodes.size()));
            for(Node n: nodes){
                n.setParent(node);
                n.trySetState(NodeState.BUSY);
                if(n != chosenNode){
                    stack.add(n);
                }
            }
            stack.add(chosenNode);  //we want it at the top to visit it in the next iteration
            getNodeBetween(node, chosenNode).trySetState(NodeState.VISITED);
        }
    }

    private void putWalls(int howMany){
        for(int i = 0; i < howMany; i++){
            if(!wallsToBuild.isEmpty()){
                wallsToBuild.remove().trySetState(NodeState.WALL);
            }
            else{
                makeSureEndReachable();
                isSettingTheGridUp = false;
                break;
            }
        }
    }

    private void makeSureEndReachable() {
        Node endNode = board.getEndNode();
        for(Vector2 vec: endNode.getPosition().getAdjacentPositions()){
            if(board.isInBounds(vec)){
                board.getNodeAt(vec).trySetState(NodeState.FREE);
            }
        }
    }

    @Override   //TODO add abstract class here as well probably
    public void start(Board board, Node startNode) {
        super.start(board, startNode);
        stack.clear();
        stack.add(board.getStartNode());
        isSettingTheGridUp = true;
        setUpWallsToBuild();
        prevNode = null;
    }

    @Override
    protected void mazeStep() {
        if (isSettingTheGridUp) {
            putWalls(10);
        }
        else {
            if (!stack.isEmpty()) {
                Node nodeToVisit = stack.pop();
                //used to show the currently visited node and the actual backtracking
                if (prevNode != null) {
                    prevNode.trySetState(NodeState.VISITED);
                }
                if (nodeToVisit.getState() != NodeState.VISITED) {
                    visit(nodeToVisit);
                }
                //this coloring doesn't impact visit logic
                nodeToVisit.trySetState(NodeState.PATH);    //choosing end for the colour
                prevNode = nodeToVisit;
            } else {
                done = true;
            }
        }
    }

    //TODO somethings wrong with visiting order or showing order

}
