package com.league.stats.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class Main extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("OP.GG Search");

		initialize();
		showWindow();
	}

	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("page2.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("page1.fxml"));
			AnchorPane overview = (AnchorPane)loader.load();
			Image im = new Image("http://opgg-static.akamaized.net/images/lol/item/1412.png?image=w_42&v=15192252000");
			ImageView image = new ImageView();
			image.setImage(im);
			rootLayout.setCenter(overview);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setJavaScriptEnabled(true);
		SearchForm search = new SearchForm("roflmfaolol");
		//search.summonerSearch(client);
		//search.getProfileInfo();
		//search.liveGameSearch();
		//search.championAutoSearch(client);

		client.close();
		launch(args);
	}
}
