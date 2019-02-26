package rail.network;

import javafx.scene.Node;

/**
 * Adds the ability to sort JavaFX nodes by a z order
 * @author JGoodHub
 */
public class NodeZ {
    
    //-----VARIABLES-----

    public int zOrder;
    public Node node;
    
    //-----METHODS-----
    
    /**
     * Sets up the node and its z order
     * 
     * @param node
     * @param zOrder
     */
    public NodeZ (Node node, int zOrder) {
        this.node = node;
        this.zOrder = zOrder;
    }
    
}












