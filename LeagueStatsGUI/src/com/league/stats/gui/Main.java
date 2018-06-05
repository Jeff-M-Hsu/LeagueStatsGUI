package com.league.stats.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;



public class Main extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("OP.GG Search");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		initialize();
		showDisplay();
	}

	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Window.fxml"));
			WindowController controller = new WindowController();
			loader.setController(controller);
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showDisplay() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Display.fxml"));
			FXController controller = new FXController();
			loader.setController(controller);
			AnchorPane overview = (AnchorPane)loader.load();
			overview.getStylesheets().add(Main.class.getResource("./ModenaMod.css").toExternalForm());
			rootLayout.setCenter(overview);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args){
		launch(args);
	}
}
