package rail.network;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Train {
    
    private ArrayList<Station> stops;
    private int currStationPtr = 0;
    private Station currStation;
    private int nextStationPtr = 1;
    private Station nextStation;
    private boolean reversed = false;
    
    private NodeZ trainNode;
    
    public Train (ArrayList<Station> stops) {
        this.stops = stops;
        
        currStation = stops.get(currStationPtr);
        nextStation = stops.get(nextStationPtr);
        
        trainNode = GUIconstructor.createTrain(stops.get(0).getStationPosition());
        NetworkManager.addNodeToMap(trainNode);
        
        GoToNextStop();
    }
    
    public void Delete () {
        NetworkManager.removeNodeToMap(trainNode);
    }
    
    private void GoToNextStop () {     
        Line path = new Line(currStation.getStationPosition().x, currStation.getStationPosition().y, 
                             nextStation.getStationPosition().x, nextStation.getStationPosition().y); 
        
        double duration = 0;
        for (Track track : currStation.getTracks()) {
            if (track.getStations().contains(nextStation)) {
                duration = track.getLength() / 1d;
            }
        }
        
        PathTransition pathTrans = new PathTransition(Duration.seconds(duration), path, trainNode.getNode());
        pathTrans.setOnFinished(e -> {
            if (nextStationPtr == stops.size() - 1) {
                reversed = true;
                currStationPtr = stops.size() - 1;
                nextStationPtr = stops.size() - 2;
                currStation = stops.get(currStationPtr);
                nextStation = stops.get(nextStationPtr);
                GoToNextStop();
            } else if (nextStationPtr == 0) {                                    
                reversed = false;
                currStationPtr = 0;
                nextStationPtr = 1;
                currStation = stops.get(currStationPtr);
                nextStation = stops.get(nextStationPtr);
                GoToNextStop();
            } else {
                if (!reversed) {
                    currStationPtr++;
                    nextStationPtr++;
                    currStation = stops.get(currStationPtr);
                    nextStation = stops.get(nextStationPtr);
                    GoToNextStop();
                } else {
                    currStationPtr--;
                    nextStationPtr--;
                    currStation = stops.get(currStationPtr);
                    nextStation = stops.get(nextStationPtr);
                    GoToNextStop();
                }
            }
        });
        
        Random r = new Random();
        pathTrans.setDelay(Duration.seconds(((r.nextDouble() * 1.5) + 0.5) * 1d));
        pathTrans.play();
    }
    
    
    
}










