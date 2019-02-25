package rail.network;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

public class GUIconstructor {
    
    public static NodeZ createStation (String nameLong, String nameShort, Vector2 position, double angle) {
        Group stationGroup = new Group();
        
        Circle stationIcon = new Circle(position.x, position.y, 8, Color.WHITE);  
        stationIcon.setStroke(Color.BLACK);
        stationIcon.setStrokeWidth(2);
        
        Label stationNameLong = new Label(nameLong);
        stationNameLong.setLayoutX(position.x + 11);        
        stationNameLong.setLayoutY(position.y -7.5);       
        stationNameLong.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        stationNameLong.setVisible(NetworkManager.useLong);
        
        Label stationNameShort = new Label(nameShort);
        stationNameShort.setLayoutX(position.x + 11);        
        stationNameShort.setLayoutY(position.y -7.5);       
        stationNameShort.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        stationNameShort.setVisible(!NetworkManager.useLong);
        
        stationGroup.getChildren().addAll(stationIcon, stationNameLong, stationNameShort);        
        stationGroup.getTransforms().add(new Rotate(angle, position.x, position.y));  
        return new NodeZ(stationGroup, 1);
    }
    
    public static NodeZ createTrackLine (Vector2 startPos, Vector2 endPos) {
        Group trackGroup = new Group();
        
        Line trackLines1 = new Line(startPos.x, startPos.y, endPos.x, endPos.y);
        trackLines1.setStrokeWidth(8);
        trackLines1.setStroke(Color.BLACK);
        
        Line trackLines2 = new Line(startPos.x, startPos.y, endPos.x, endPos.y);
        trackLines2.setStrokeWidth(4);
        trackLines2.setStroke(Color.WHITE);
                
        trackGroup.getChildren().add(trackLines1);
        trackGroup.getChildren().add(trackLines2);        
        return new NodeZ(trackGroup, 0);        
    }
    
    public static NodeZ createTrain (Vector2 position) {
        Group trainGroup = new Group();
        
        Circle circle = new Circle(position.x, position.y, 4, Color.RED);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.BLACK);
        
        trainGroup.getChildren().add(circle);        
        return new NodeZ(trainGroup, 2);        
    }
    
    public static Node createAddStationMenu () {
        VBox addStationMenu = new VBox(3);
        Label addStationLabel = new Label("Add New Station");
        TextField nameInputL = new TextField("Name Long");
        TextField nameInputS = new TextField("Name Short");
        TextField xPosInput = new TextField("300");
        TextField yPosInput = new TextField("300");
        
        Slider angleInput = new Slider(-90, 90, 0);
        angleInput.setMin(-90);
        angleInput.setMax(90);
        angleInput.setShowTickLabels(true);
        angleInput.setShowTickMarks(true);
        angleInput.setMajorTickUnit(30);
        angleInput.setMinorTickCount(1);
        angleInput.setSnapToTicks(true);
        
        Button addStationButton = new Button("Add");
        addStationButton.setOnAction(e -> {
            NetworkManager.addNewStation(
                    nameInputL.getText(), nameInputS.getText(),
                    xPosInput.getText(), yPosInput.getText(),
                    angleInput.getValue());            
        });    
        
        addStationMenu.getChildren().addAll(
                addStationLabel,
                nameInputL, nameInputS,
                xPosInput, yPosInput,
                angleInput,
                addStationButton
        );
        
        return addStationMenu;
    }
    
    public static Node createAddTrackMenu () {
        VBox addTrackMenu = new VBox(3);
        Label addTrackLabel = new Label("Add New Track");
        TextField statInputA = new TextField("Station A");
        TextField statInputB = new TextField("Station B");
        TextField trackLength = new TextField("Track Length");
        Button addTrackButton = new Button("Add");
        addTrackButton.setOnAction(e -> {
            NetworkManager.addNewTrack(statInputA.getText(), statInputB.getText(), trackLength.getText());            
        }); 
        addTrackMenu.getChildren().addAll(addTrackLabel, statInputA, statInputB, trackLength, addTrackButton);                
        return addTrackMenu;    
    }
    
    public static Node createAddRouteMenu () {
        VBox addRouteMenu = new VBox(3);        
        Label addRouteLabel = new Label("Add New Route");
        ColorPicker colorPicker = new ColorPicker();
        
        HBox enterStation = new HBox();        
        TextField stationInput = new TextField("Station");
        
        CheckBox stopAtStation = new CheckBox();
        HBox stopWrapper = new HBox(stopAtStation);
        stopWrapper.setPadding(new Insets(3, 0, 0, 4));
        
        Button addRouteButton = new Button("Add");
        addRouteButton.setMinWidth(40);
        addRouteButton.setOnAction(e -> {
            NetworkManager.addNewRoute(stationInput.getText(), colorPicker.getValue());
        }); 
        
        enterStation.getChildren().addAll(stationInput, stopWrapper, addRouteButton);
                
        addRouteMenu.getChildren().addAll(addRouteLabel, colorPicker, enterStation);
                
        
        return addRouteMenu;
    }
    
    public static Node createModStationMenu () {  
        VBox modStationMenu = new VBox(15);
        
        TextField nameInput = new TextField("Name Long");
        
        GridPane dPad = new GridPane();
        Button up = new Button("↑");
        up.setOnAction(e -> NetworkManager.findStationByName(
                nameInput.getText(), 
                NetworkManager.useLong).Translate(new Vector2(0, -37.5d))
        );
        up.setPrefWidth(30);
        up.setPrefHeight(30);
        dPad.add(up, 1, 0);
        
        Button down = new Button("↓");
        down.setOnAction(e -> NetworkManager.findStationByName(
                nameInput.getText(), 
                NetworkManager.useLong).Translate(new Vector2(0, 37.5d))
        );
        down.setPrefWidth(30);
        down.setPrefHeight(30);
        dPad.add(down, 1, 2);
        
        Button left = new Button("←");
        left.setOnAction(e -> NetworkManager.findStationByName(
                nameInput.getText(), 
                NetworkManager.useLong).Translate(new Vector2(-37.5d, 0))
        );
        left.setPrefWidth(30);
        left.setPrefHeight(30);
        dPad.add(left, 0, 1);
        
        Button right = new Button("→");
        right.setOnAction(e -> NetworkManager.findStationByName(
                nameInput.getText(), 
                NetworkManager.useLong).Translate(new Vector2(37.5d, 0))
        );
        right.setPrefWidth(30);
        right.setPrefHeight(30);
        dPad.add(right, 3, 1);
        
        dPad.setAlignment(Pos.CENTER);
        
        Slider angleInput = new Slider(-90, 90, 0);
        angleInput.setMin(-90);
        angleInput.setMax(90);
        angleInput.setShowTickLabels(true);
        angleInput.setShowTickMarks(true);
        angleInput.setMajorTickUnit(30);
        angleInput.setMinorTickCount(1);
        angleInput.setSnapToTicks(true);
        
        angleInput.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                Station currStat = NetworkManager.findStationByName(nameInput.getText(), NetworkManager.useLong);
                if (currStat != null) {
                    currStat.Rotate((double) new_val);
                }
            }
        });
        
        modStationMenu.getChildren().addAll(nameInput, dPad, angleInput);
        
        return modStationMenu;
    }
    
}














