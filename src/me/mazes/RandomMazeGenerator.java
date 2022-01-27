package me.mazes;

import me.board.Board;
import me.board.Node;
import me.board.NodeState;

public class RandomMazeGenerator extends MazeGenerator {
    private int wallsToPutCount;

    @Override
    public void start(Board board, Node startNode) {
        super.start(board, startNode);
        wallsToPutCount = (int) Math.round(board.getNodeCount() * 0.24) - 1;
    }

    @Override
    protected void mazeStep() {
        if (wallsToPutCount > 0) {
            Node node = board.getRandomNode();
            while (node.getState() != NodeState.FREE) {
                node = board.getRandomNode();
            }
            node.setState(NodeState.WALL);
            wallsToPutCount--;
        } else {
            done = true;
        }
    }
}
