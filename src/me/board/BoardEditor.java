package me.board;

/**
 * Modifies the given board a little bit every time the step method is called. Used by the VisualizationManager.
 */
public interface BoardEditor {
    void start(Board board, Node startNode);
    void step();
    boolean isDone();
}
