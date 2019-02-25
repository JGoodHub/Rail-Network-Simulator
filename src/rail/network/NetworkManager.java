package rail.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

    private static ArrayList<Station> stations = new ArrayList<Station>();
    public static ArrayList<Track> tracks = new ArrayList<Track>();
    public static ArrayList<Route> routes = new ArrayList<Route>();    
       
    public static ArrayList<Station> getAllStations () {
        return stations;
    }
    
    public static Station findStationByName (String name, boolean useLong) {
        for (Station station : stations) {
            if (station.getName(useLong).equals(name)) {
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
        addNodeToMap(new NodeZ(corner, 0));
        
        TabPane menu = new TabPane(
                new Tab("Run", createRunTab()),
                new Tab("Add", createAddTab()),
                new Tab("Mod", createModTab()),
                new Tab("Del"),
                new Tab("Misc", createMiscTab()));        
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
    
    private boolean running = false;
    private Node createRunTab () {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(8, 8, 8, 8));
        Button startBut = new Button("Start Simulation");
        startBut.setOnAction(e -> {
            if (running) {
                if (running) {
                    startBut.setText("Start Simulation");
                    running = false;
                    for (Route route : routes) {
                        route.StopTrain();
                    }
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
    
    public static boolean useLong = true;
    private Node createMiscTab () {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(8, 8, 8, 8));
        
        CheckBox nameTypeToggle = new CheckBox("Use Long Name");
        nameTypeToggle.setOnAction(e -> toggleNameLength());
        nameTypeToggle.setSelected(true);
        
        wrapper.getChildren().add(nameTypeToggle);
        return wrapper;        
    }
    
    private static Circle corner = new Circle(590, 590, 0.01d);
    public static void addNewStation (String nL, String nS, String x, String y, double angle) {
        try {
            int xPos = Integer.parseInt(x);
            int yPos = Integer.parseInt(y);            
                for (Station station : getAllStations()) {
                    if (nL.equals(station.getName(true)) || nS.equals(station.getName(false)) || station.getStationPosition().equals(new Vector2(xPos, yPos))) {
                        System.out.println("ERROR: Duplicate Station");
                        return;
                    }
                }
                
                if (xPos > corner.getCenterX() - 200) {corner.setCenterX(xPos + 200);}                
                if (yPos > corner.getCenterY() - 200) {corner.setCenterY(yPos + 200);}
                
                Station newStat = new Station(nL, nS, new Vector2(xPos, yPos), angle);
                stations.add(newStat);
                reOrderNodes();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Station Vector Parsing");
        }
    }
    
    public static void addNewTrack (String statA, String statB, String length) {
        try {
            int len = Integer.parseInt(length);
            Station a = findStationByName(statA, useLong);
            Station b = findStationByName(statB, useLong);
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
        Route newRoute = new Route(statStringArray, useLong);
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
        
    public static Pane railMap = new Pane();    
    private static ArrayList<NodeZ> layoutNodes = new ArrayList<>();
    
    public static void addNodeToMap (NodeZ node) {
        layoutNodes.add(node);
        railMap.getChildren().add(node.getNode());
    }
    
    public static void removeNodeToMap (NodeZ node) {
        layoutNodes.remove(node);
        railMap.getChildren().remove(node.getNode());
    }
    
    public static void reOrderNodes () {
        railMap.getChildren().clear();
        Collections.sort(layoutNodes, new Comparator<NodeZ>() {
            @Override
            public int compare(NodeZ node1, NodeZ node2) {
                return  node1.getZ().compareTo(node2.getZ());
            }
        });
        
        for (NodeZ n : layoutNodes) {
            railMap.getChildren().add(n.getNode());
        }
    }
    
    private void toggleNameLength () {
        useLong = !useLong;        
        for (Station stat : stations) {
            Group g = (Group) stat.getStationNode();
            Label l = (Label) g.getChildren().get(1);
            Label s = (Label) g.getChildren().get(2);
            l.setVisible(useLong);
            s.setVisible(!useLong);      
        }
        
        
    }
        
}






