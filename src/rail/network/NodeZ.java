package rail.network;

import javafx.scene.Node;

public class NodeZ {
    public int zOrder;
    private Node node;
    
    public NodeZ (Node node, int zOrder) {
        this.node = node;
        this.zOrder = zOrder;
    }
    
    public Node getNode () {
        return node;
    }
    
    public String getZ () {
        return zOrder + "";
    }
    
    
}












