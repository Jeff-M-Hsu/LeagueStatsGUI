package com.league.stats.gui;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class FXController {
	
	private Summoner[] players;
	private List<HtmlElement> item;
	@FXML
	private TextArea items;
	@FXML
	private TextArea summonerNames;
	@FXML
	private TextArea summonerRanks;
	@FXML
	private TextArea summonerRatios;	
	
	public FXController() {
	}
	
	@FXML
	private void initialize() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		client.getOptions().setJavaScriptEnabled(true);
		SearchForm search = new SearchForm("c9 sneaky");
		search.summonerSearch(client);
		players = search.getPlayers();
		item = search.getItemList();
		playerViewer();
		itemViewer();
		client.close();
	}
	
	private void playerViewer() {
		String nameList = "";
		String rankList = "";
		String ratioList = "";
		for(int i = 0; i < players.length; i++) {
			nameList += players[i].getName() + "\n";
			rankList += players[i].getRank() + "\n";
			ratioList += players[i].getRatio() + "\n";
		}
		nameList.trim();
		rankList.trim();
		ratioList.trim();
		summonerNames.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
		summonerRanks.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
		summonerRatios.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
		summonerNames.setText(nameList);
		summonerRanks.setText(rankList);
		summonerRatios.setText(ratioList);
	}
	
	private void itemViewer() {
		String itemList = "";
		for(int i = 0; i < item.size(); i++) {
			itemList += item.get(i).asText() + "\n";
		}
		items.setText(itemList);
	}
	
	@FXML
	private void close(ActionEvent event) {
		Platform.exit();
	}
}
