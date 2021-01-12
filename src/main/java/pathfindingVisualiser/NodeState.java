package pathfindingVisualiser;

//TODO possibly remove the isWallable thing
public enum NodeState {
    WALL, FREE, VISITED, BUSY, START, END, PATH;  //TODO change BUSY to something more descriptive?

    //    private static NodeState[] wallable = new NodeState[] {FREE}
    public String toString(){
        return switch(this){
            case WALL -> "WALL";
            case FREE -> "FREE";
            case VISITED -> "VISITED";
            case BUSY -> "BUSY";
            case START -> "START";
            case END -> "END";
            case PATH -> "PATH";
        };
    }
//    public boolean isWallable(){
//        if(this == )
//    }
}
