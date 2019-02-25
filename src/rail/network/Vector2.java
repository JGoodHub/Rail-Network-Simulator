package rail.network;


public class Vector2 {
    
    public double x;
    public double y;
    
    public Vector2 (double x, double y) {
        this.x = x;
        this.y = y;        
    } 
    
    public Vector2 () {
        x = 0;
        y = 0;
    }
    
    @Override
    public String toString () {
        return x + ", " + y;
    }
    
    @Override
    public boolean equals (Object o) {        
        Vector2 other = (Vector2) o;
        return other.x == x && other.y == y;
    }
    
    
    
}











