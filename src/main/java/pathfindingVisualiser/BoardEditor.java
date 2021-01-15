package pathfindingVisualiser;

public interface BoardEditor {
    void start(Board board, Node startNode);
    void step();
    boolean isDone();
}
