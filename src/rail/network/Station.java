package rail.network;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

public class Station {
    
    NodeZ stationNode;
    double angle;
    public Station (String _nameLong, Vector2 _position, double _angle) {
        nameLong = _nameLong;
        position = _position;
        angle = _angle;
        
        stationNode = GUIconstructor.createStation(nameLong, position, angle);
        NetworkManager.addNodeToMap(stationNode);
    }    
    
    public String nameLong;
    
    private ArrayList<Track> adjacentTracks = new ArrayList<>();
    
    public void modifyTracks (Track track, boolean addNew) {
        if (addNew) {
            adjacentTracks.add(track);
        } else {
            adjacentTracks.remove(track);
        }
    }
    
    public ArrayList<Track> getTracks () {
        return adjacentTracks;
    }
    
    public Track isConnectedTo (Station stat) {
        for (Track track : adjacentTracks) {
            if (track.getStations().contains(stat)) {
                return track;
            }
        }
        return null;
    }
    
    private Vector2 position = new Vector2();   
    public Vector2 getStationPosition () {
        return (position);        
    }
    
    public Node getStationNode () {
        return stationNode.node;
    }
    
    public boolean locked = false;
    public void Translate (Vector2 ammount) {
        if (!locked) {
            Group g = (Group) stationNode.node;
            Circle c = (Circle) g.getChildren().get(0);
            Label l = (Label) g.getChildren().get(1);
            Label s = (Label) g.getChildren().get(2);

            g.getTransforms().add(new Rotate(-angle, position.x, position.y));

            position.x += ammount.x;
            position.y += ammount.y;

            c.setCenterX(position.x);
            c.setCenterY(position.y);

            l.setLayoutX(position.x + 11);
            l.setLayoutY(position.y - 7.5);

            s.setLayoutX(position.x + 11);
            s.setLayoutY(position.y - 7.5);

            g.getTransforms().add(new Rotate(angle, position.x, position.y));

            for (Track adjTrack : adjacentTracks) {
                adjTrack.TranslateLine(this);
            }
        }
        
    }
    
    public void Rotate (double newAngle) {
        Group g = (Group) stationNode.node;

        double yx = g.getLocalToSceneTransform().getMyx();
        double yy = g.getLocalToSceneTransform().getMyy();
        double currAngle = Math.atan2(yx, yy);
        currAngle = Math.toDegrees(currAngle);
        currAngle = currAngle < 0 ? currAngle + 360 : currAngle;

        g.getTransforms().add(new Rotate(-currAngle, position.x, position.y));  
        g.getTransforms().add(new Rotate(newAngle, position.x, position.y));
        angle = newAngle;            
            
    }
    
}















