package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();

        controller =  loader.getController();
        controller.createPlayground();

     MenuBar menuBar =    createMenu();
     menuBar.prefWidthProperty().bind(primaryStage.widthProperty());


        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);



        Scene scene = new Scene(rootGridPane);

        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private MenuBar createMenu() {
        //file menu
        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> controller.resetGame());
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(event -> controller.resetGame());
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event -> exitGame());

        fileMenu.getItems().addAll(newGame, resetGame,separatorMenuItem, exitGame);

        //help menu
        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame = new MenuItem("About Game");
        aboutGame.setOnAction(event -> aboutConnect4Game());
        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> aboutMe());

        helpMenu.getItems().addAll(aboutGame,separatorMenuItem1, aboutMe);





        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Palak Mittal");
        alert.setContentText("Hi I am Palak. Currently i am exploring the feild of java GUI"+
                " app development. This is my first game app. Soon as i will dive deeper into"+
                " this feild i will try to develop more such game apps. Hope you enjoy this game.");
        alert.show();




    }

    private void aboutConnect4Game() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How To Play");
        alert.setContentText("Connect four is a two player connection game in which the"+
                " player first choose a color and then take turns dropping colored discs"+
                " from the top into a seven coloumn , six row vertically suspended grid"+
                " the peice fall straight down, occuping the next available space within the coloumn"+
                " the objective of the game is to be first to form a horipzontal, verticle,"+
                " or diagonal line of four of one's own discs. Connect four is a solved game "+
                " The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //TODO
    }


    public static void main(String[] args) {
        launch(args);
    }
}
