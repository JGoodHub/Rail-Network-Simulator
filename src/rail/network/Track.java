package rail.network;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;

public class Track {          
    
    private Station stationA;
    private Station stationB;
    private int length;
    
    public Track (Station statA, Station statB, int length) {
        setA(statA);
        setB(statB);
        this.length = length;
        
        trackLine = GUIconstructor.createTrackLine(statA.getStationPosition(), statB.getStationPosition());
        NetworkManager.addNodeToMap(trackLine); 
    }
    
    public Track(String nameA, String nameB, int length, boolean useLong) {
        setA(NetworkManager.findStationByName(nameA, useLong));
        setB(NetworkManager.findStationByName(nameB, useLong));
        this.length = length;
        
        trackLine = GUIconstructor.createTrackLine(getStationA().getStationPosition(), getStationB().getStationPosition());
        NetworkManager.addNodeToMap(trackLine);  
    }
        
    public void setA (Station newStation) {
        stationA = newStation;
        newStation.modifyTracks(this, true);
    }
    
    public void setB (Station newStation) {
        stationB = newStation;
        newStation.modifyTracks(this, true);
    }    
    
    public Station getStationA () {
        return stationA;
    }
    
    public Station getStationB () {
        return stationB;
    }
    
    public int getLength () {
        return length;
    }
    
    public ArrayList<Station> getStations () {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(stationA);
        stations.add(stationB);
        return stations;
    }
    
    NodeZ trackLine;
    private ArrayList<Color> colors = new ArrayList<>();
    int colCount = 0;    
    public void addColour (Color newCol) {
        colors.add(newCol);
        colors.add(colCount++, newCol);
        Group lineGroup = (Group) trackLine.getNode();
        Line line = (Line) lineGroup.getChildren().get(1);
        
        ArrayList<Stop> stopList = new ArrayList<>();
        double stopCount = 0;  
        
        for (int p = 0; p < colors.size(); p++) {
            stopList.add(new Stop(stopCount, colors.get(p)));
            stopList.add(new Stop(stopCount + (0.99d / colors.size()), colors.get(p)));
            stopCount += 1d / colors.size();
        }        
        
        line.setStroke(new LinearGradient(
                line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(),
                false,
                CycleMethod.NO_CYCLE, 
                stopList));        
    }      

    public boolean locked = false;
    public void TranslateLine (Station newEndPos) {
        if (!locked) {
            if (newEndPos == stationA) {
                Group g = (Group) trackLine.getNode();
                Line lStroke = (Line) g.getChildren().get(0);
                lStroke.setStartX(newEndPos.getStationPosition().x);
                lStroke.setStartY(newEndPos.getStationPosition().y);
                Line lFill = (Line) g.getChildren().get(1);
                lFill.setStartX(newEndPos.getStationPosition().x);
                lFill.setStartY(newEndPos.getStationPosition().y);
            } else {
                Group g = (Group) trackLine.getNode();
                Line lStroke = (Line) g.getChildren().get(0);
                lStroke.setEndX(newEndPos.getStationPosition().x);
                lStroke.setEndY(newEndPos.getStationPosition().y);
                Line lFill = (Line) g.getChildren().get(1);
                lFill.setEndX(newEndPos.getStationPosition().x);
                lFill.setEndY(newEndPos.getStationPosition().y);
            }
        }        
    }
    
    
    
}


    










