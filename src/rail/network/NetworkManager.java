package rail.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class NetworkManager extends Application {
    
    //-----VARIABLES-----

    private static final ArrayList<Station> stations = new ArrayList<>();
    public static ArrayList<Track> tracks = new ArrayList<>();
    public static ArrayList<Route> routes = new ArrayList<>();    
       
    private boolean running = false;
    
    private static final Circle bottomRightCornerPin = new Circle(590, 590, 1d);

    public static Pane railMap = new Pane();    
    private static final ArrayList<NodeZ> layoutNodes = new ArrayList<>();
    
    //-----METHODS-----
    
    public static ArrayList<Station> getAllStations () {
        return stations;
    }    
    
    public static Station findStationByName (String searchName) {
        for (Station station : stations) {
            if (station.name.equals(searchName)) {
                return station;
            }        
        }        
        return null;
    }
    
    public static void main(String[] args) {
        launch(args);        
    }
        
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rail Network Simulation"); 
        primaryStage.setResizable(false);
        BorderPane borderLayout = new BorderPane();
        
        ScrollPane scrollableMap = new ScrollPane(railMap);
        scrollableMap.setPrefWidth(600);
        scrollableMap.setPannable(true);
        scrollableMap.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollableMap.setVbarPolicy(ScrollBarPolicy.NEVER);           
        borderLayout.setLeft(scrollableMap);
        
        addNodeToMap(new NodeZ(bottomRightCornerPin, 0));
        
        TabPane menu = new TabPane(
                new Tab("Run", createRunTab()),
                new Tab("Add", createAddTab()),
                new Tab("Mod", createModTab()),
                new Tab("Del"));        
        for (Tab tab : menu.getTabs()) {
            tab.setClosable(false);
        }
        menu.getSelectionModel().select(menu.getTabs().get(1));
        menu.setPrefWidth(200);
        borderLayout.setRight(menu);
                
        Scene scene = new Scene(borderLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        reOrderNodes();
    }    
    
    private Node createRunTab () {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(8, 8, 8, 8));
        Button startBut = new Button("Start Simulation");
        startBut.setOnAction(e -> {
            if (running) {
                startBut.setText("Start Simulation");
                running = false;
                for (Route route : routes) {
                    route.StopTrain();
                }
            } else {
                if (routes.size() > 0) {
                    startBut.setText("Stop Simulation");
                    running = true;
                    for (Route route : routes) {
                        route.StartTrain();
                    }        
                    reOrderNodes();
                }                
            }           
        });
        
        wrapper.getChildren().addAll(startBut);
        return wrapper;
    }
    
    private Node createAddTab () {        
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(8, 8, 8, 8));
        wrapper.getChildren().addAll(
                GUIconstructor.createAddStationMenu(),
                GUIconstructor.createAddTrackMenu(),
                GUIconstructor.createAddRouteMenu()
        );        
        return wrapper;       
    }
    
    private Node createModTab () {     
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(8, 8, 8, 8));
        wrapper.getChildren().addAll(
                GUIconstructor.createModStationMenu()
        );        
        return wrapper; 
    }
        
    public static void addNewStation (String name, String _xPos, String _yPos, double angle) {
        try {
            int xPos = Integer.parseInt(_xPos);
            int yPos = Integer.parseInt(_yPos);            
                for (Station station : getAllStations()) {
                    if (name.equals(station.name) || station.getStationPosition().equals(new Vector2(xPos, yPos))) {
                        System.out.println("ERROR: Duplicate Station");
                        return;
                    }
                }
                
                if (xPos > bottomRightCornerPin.getCenterX() - 200) {
                    bottomRightCornerPin.setCenterX(xPos + 200);
                }   
                
                if (yPos > bottomRightCornerPin.getCenterY() - 200) {
                    bottomRightCornerPin.setCenterY(yPos + 200);
                }
                
                Station newStat = new Station(name, new Vector2(xPos, yPos), angle);
                stations.add(newStat);
                reOrderNodes();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Station Vector Parsing");
        }
    }
    
    public static void addNewTrack (String statA, String statB, String length) {
        try {
            int len = Integer.parseInt(length);
            Station a = findStationByName(statA);
            Station b = findStationByName(statB);
            if (a != null && b != null && a.isConnectedTo(b) == null) {                
                tracks.add(new Track(a, b, len));
                reOrderNodes();
            } else {
                System.out.println("ERROR: Track Duplicate");
            }            
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Track Length Parsing");
        }             
    }
    
    public static void addNewRoute (String statStrings, Color col) {
        String[] statStringArray = statStrings.split(", ");   
        Route newRoute = new Route(statStringArray);
        for (Route route : routes) {
            if (route.equalsStations(newRoute)) {
                System.out.println("ERROR: Route Duplicate");
                return;
            }
        }       
        if (newRoute.finalSetup(col)) {
            routes.add(newRoute);
            reOrderNodes(); 
        } else {
            System.out.println("ERROR: Route Not Valid");
        }
    }        
  
    public static void addNodeToMap (NodeZ node) {
        layoutNodes.add(node);
        railMap.getChildren().add(node.node);
    }
    
    public static void removeNodeToMap (NodeZ node) {
        layoutNodes.remove(node);
        railMap.getChildren().remove(node.node);
    }
    
    public static void reOrderNodes () {
        railMap.getChildren().clear();
        Collections.sort(layoutNodes, new Comparator<NodeZ>() {
            @Override
            public int compare(NodeZ nodeA, NodeZ nodeB) {
                if (nodeA.zOrder > nodeB.zOrder)
                    return 1;
                else if (nodeA.zOrder < nodeB.zOrder)
                    return -1;
                else
                    return 0;
            }
        });
        
        for (NodeZ orderedNode : layoutNodes) {
            railMap.getChildren().add(orderedNode.node);
        }
    }
        
}






