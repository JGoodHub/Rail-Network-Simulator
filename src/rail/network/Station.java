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
    public Station (String nameLong, String nameShort, Vector2 position, double angle) {
        setName(nameLong, nameShort);
        truePosition = position;
        this.angle = angle;
        
        stationNode = GUIconstructor.createStation(nameLong, nameShort, position, angle);
        NetworkManager.addNodeToMap(stationNode);
    }    
    
    private String nameLong;
    private String nameShort;
    
    public void setName (String newNameLong, String newNameShort) {
        nameLong = newNameLong;
        nameShort = newNameShort;         
    }    
    
    public String getName (boolean getLong) {
        if (getLong) {
            return nameLong;
        } else {
            return nameShort;
        }
    } 
    
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
    
    private Vector2 truePosition = new Vector2();   
    public Vector2 getStationPosition () {
        return (truePosition);        
    }
    
    public Node getStationNode () {
        return stationNode.getNode();
    }
    
    public boolean locked = false;
    public void Translate (Vector2 ammount) {
        if (!locked) {
            Group g = (Group) stationNode.getNode();
            Circle c = (Circle) g.getChildren().get(0);
            Label l = (Label) g.getChildren().get(1);
            Label s = (Label) g.getChildren().get(2);

            g.getTransforms().add(new Rotate(-angle, truePosition.x, truePosition.y));

            truePosition.x += ammount.x;
            truePosition.y += ammount.y;

            c.setCenterX(truePosition.x);
            c.setCenterY(truePosition.y);

            l.setLayoutX(truePosition.x + 11);
            l.setLayoutY(truePosition.y - 7.5);

            s.setLayoutX(truePosition.x + 11);
            s.setLayoutY(truePosition.y - 7.5);

            g.getTransforms().add(new Rotate(angle, truePosition.x, truePosition.y));

            for (Track adjTrack : adjacentTracks) {
                adjTrack.TranslateLine(this);
            }
        }
        
    }
    
    public void Rotate (double newAngle) {
        Group g = (Group) stationNode.getNode();

        double yx = g.getLocalToSceneTransform().getMyx();
        double yy = g.getLocalToSceneTransform().getMyy();
        double currAngle = Math.atan2(yx, yy);
        currAngle = Math.toDegrees(currAngle);
        currAngle = currAngle < 0 ? currAngle + 360 : currAngle;

        g.getTransforms().add(new Rotate(-currAngle, truePosition.x, truePosition.y));  
        g.getTransforms().add(new Rotate(newAngle, truePosition.x, truePosition.y));
        angle = newAngle;            
            
    }
    
}















