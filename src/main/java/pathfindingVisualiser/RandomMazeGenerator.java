package pathfindingVisualiser;

import java.util.Random;

public class RandomMazeGenerator implements BoardEditor{
    private Board board;
    private int wallsToPut;

    @Override
    public void start(Board board) {    //TODO add wallFactor back?
        this.board = board;
        wallsToPut = (int) Math.round(board.getNodeCount() * 0.26) - 1;
        System.out.println(wallsToPut);
    }

    @Override
    public void step() {
        if(wallsToPut > 0){
            Node node = board.getRandomNode();
            while(node.getState() != NodeState.FREE){
                node = board.getRandomNode();
            }
            node.setState(NodeState.WALL);
            wallsToPut--;
        }
    }

    @Override
    public boolean isDone() {
        return wallsToPut == 0;
    }
}
