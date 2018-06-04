package com.league.stats.gui;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXController {

	private Summoner[] players;
	private List<HtmlElement> item;
	private List<HtmlElement> skills;
	private List<HtmlElement> itemBuild;
	private List<HtmlElement> boots;
	@FXML private TextArea items;
	@FXML private TextArea summonerNames;
	@FXML private TextArea summonerRanks;
	@FXML private TextArea summonerRatios;
	@FXML private ImageView skill_1;
	@FXML private ImageView skill_2;
	@FXML private ImageView skill_3;
	@FXML private ImageView build_1_item_1;
	@FXML private ImageView build_1_item_2;
	@FXML private ImageView build_1_item_3;
	@FXML private ImageView build_2_item_1;
	@FXML private ImageView build_2_item_2;
	@FXML private ImageView build_2_item_3;
	@FXML private ImageView build_3_item_1;
	@FXML private ImageView build_3_item_2;
	@FXML private ImageView build_3_item_3;
	@FXML private ImageView boots_1;
	@FXML private ImageView boots_2;
	@FXML private ImageView boots_3;



	public FXController() {
	}

	@FXML
	private void initialize() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		client.getOptions().setJavaScriptEnabled(true);
		SearchForm search = new SearchForm("dyrus");
		search.summonerSearch(client);
		players = search.getPlayers();
		item = search.getItemList();
		skills = search.getSkillBuild();
		itemBuild = search.getItemBuild();
		boots = search.getBootsBuild();
		playerViewer();
		itemViewer();
		imageAdder();
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
		summonerNames.setText(nameList.trim());
		summonerRanks.setText(rankList.trim());
		summonerRatios.setText(ratioList.trim());
	}

	private void itemViewer() {
		String itemList = "";
		for(int i = 0; i < item.size(); i++) {
			itemList += item.get(i).asText() + "\n";
		}
		items.setText(itemList.trim());
	}

	private void imageAdder() {
		skill_1.setImage(new Image("https:"+skills.get(0).getAttribute("src")));
		skill_2.setImage(new Image("https:"+skills.get(2).getAttribute("src")));
		skill_3.setImage(new Image("https:"+skills.get(4).getAttribute("src")));
		build_1_item_1.setImage(new Image("https:"+itemBuild.get(0).getAttribute("src")));
		build_1_item_2.setImage(new Image("https:"+itemBuild.get(2).getAttribute("src")));
		build_1_item_3.setImage(new Image("https:"+itemBuild.get(4).getAttribute("src")));
		build_2_item_1.setImage(new Image("https:"+itemBuild.get(5).getAttribute("src")));
		build_2_item_2.setImage(new Image("https:"+itemBuild.get(7).getAttribute("src")));
		build_2_item_3.setImage(new Image("https:"+itemBuild.get(9).getAttribute("src")));
		build_3_item_1.setImage(new Image("https:"+itemBuild.get(10).getAttribute("src")));
		build_3_item_2.setImage(new Image("https:"+itemBuild.get(12).getAttribute("src")));
		build_3_item_3.setImage(new Image("https:"+itemBuild.get(14).getAttribute("src")));
		boots_1.setImage(new Image("https:"+boots.get(0).getAttribute("src")));
		boots_2.setImage(new Image("https:"+boots.get(1).getAttribute("src")));
		boots_3.setImage(new Image("https:"+boots.get(2).getAttribute("src")));


	}

	@FXML
	private void close(ActionEvent event) {
		Platform.exit();
	}
}
