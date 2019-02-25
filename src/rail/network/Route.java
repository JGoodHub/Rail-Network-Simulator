package rail.network;

import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Route {
       
    private ArrayList<Station> stations = new ArrayList<>();
    
    public Route (String[] stationNames, boolean useLong) {
        for (String name : stationNames) {
            Station nextStat = NetworkManager.findStationByName(name, useLong);
            if (nextStat != null) {
                stations.add(NetworkManager.findStationByName(name, useLong)); 
                nextStat.locked = true;
            }
        }
    }
    
    public ArrayList<Station> getStations () {
        return stations;
    }
    
    public void addStation (Station stat) {
        stations.add(stat);
    }
    
    public boolean isRouteValid () {
        int i = 0;
        int count = 0;
        while (i < stations.size() - 1) {
            for (Track track : stations.get(i).getTracks()) {
                if (track.getStations().contains(stations.get(i + 1))) {
                    count++;
                }
            }            
            i++;
        }               
        return i == count && stations.size() > 0;        
    }
    
    public boolean equalsStations (Object routeOther) {
        return ((Route) routeOther).getStations().equals(stations);
    }
    
    private Train routeTrain;
    
    public void StartTrain () {
        routeTrain = new Train(stations);
    }
    
    public void StopTrain () {
        routeTrain.Delete();
        routeTrain = null;
    }
    
    public boolean finalSetup (Color newCol) {
        if (isRouteValid()) {
            for (int i = 0; i < stations.size() - 1; i++) {
                Track currTrack = stations.get(i).isConnectedTo(stations.get(i + 1));
                currTrack.addColour(newCol);      
                currTrack.locked = true;
            }
            return true;
        } else {
            return false;
        }
    }
    
}










