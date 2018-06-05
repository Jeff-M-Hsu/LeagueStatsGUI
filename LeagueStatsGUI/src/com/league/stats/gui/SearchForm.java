package com.league.stats.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.sun.media.jfxmedia.logging.Logger;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

public class SearchForm {
	private String name;
	private Summoner yourself;
	private HtmlPage profilePage;
	private HtmlPage livePage;
	private HtmlPage championPage;
	private Summoner[] players;
	private List<HtmlElement> skillBuild;
	private List<HtmlElement> itemList;
	private List<HtmlElement> itemBuild;
	private List<HtmlElement> boots;

	public SearchForm(String searchQuery) {
		name = searchQuery;
	}
	
	String getName() {
		return name;
	}
	
	Summoner getYourself() {
		return yourself;
	}

	Summoner[] getPlayers() {
		return players;
	}

	List<HtmlElement> getSkillBuild() {
		return skillBuild;
	}

	List<HtmlElement> getItemList() {
		return itemList;
	}
	
	List<HtmlElement> getItemBuild() {
		return itemBuild;
	}
	List<HtmlElement> getBootsBuild(){
		return boots;
	}

	public void summonerSearch(WebClient client){
		
		try {
			//Opens home page
			HtmlPage page;
			page = client.getPage("http://na.op.gg/");

			//Gets element for search bar
			HtmlForm searchBar = page.getFirstByXPath("//form[contains(@class, 'summoner-search-form')]");

			//Enters specified name
			searchBar.getInputByName("userName").setValueAttribute(name);

			//Create virtual button and click
			HtmlButton submitButton = (HtmlButton)page.createElement("button");
			submitButton.setAttribute("type", "submit");
			searchBar.appendChild(submitButton);
			profilePage = submitButton.click();

			//Updates profile with the Update button for latest stats
			HtmlButton updateProfile = (HtmlButton)profilePage.getElementById("SummonerRefreshButton");
			updateProfile.click();

			//Fixes capitalization issues if there are any
			//Names of pro players and famous streamers sometimes have 2 name elements
			//which aren't always the same, this gets their summoner name
			List<HtmlElement> summonerName = profilePage.getByXPath("//span[@class='Name']");
			name = summonerName.get(summonerName.size()-1).asText();
			
			getProfileInfo();
			liveGameSearch();
			championAutoSearch(client);
			
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			//System.out.println("Invalid Summoner Name");
		}
	}

	private void getProfileInfo() {

		//Gets level of summoner
		DomElement levelElement = profilePage.getFirstByXPath("//div[@class='ProfileIcon']");
		String level = levelElement.getLastElementChild().asText();

		//Gets league of summoner
		DomElement leagueElement = profilePage.getFirstByXPath("//span[@class='tierRank']");
		DomElement leagueLP = profilePage.getFirstByXPath("//span[@class='LeaguePoints']");
		String league = leagueElement.getTextContent();
		String lp = "";
		if(!league.equals("Unranked")) {
			lp = " (" + leagueLP.getTextContent().replaceAll("[\\n\\t]+", "") + ")";
		}

		//Gets league of summoner last time they placed in a season
		DomElement pastLeagueElement = profilePage.getFirstByXPath("//ul[@class='PastRankList']");
		String lastSeasonRank = "";
		if(pastLeagueElement != null) {
			lastSeasonRank = pastLeagueElement.getLastElementChild().asText();
		}

		//Prints content of resultant page
		//System.out.println("Name: " + name);
		//System.out.println("Level: " + level);
		//System.out.println("Current Rank: " + league + lp);
		//System.out.println("Previous Placement: " + lastSeasonRank + "\n");
		yourself = new Summoner(name, level, league + lp, lastSeasonRank);
	}

	private void liveGameSearch(){

		try {
			//Clicks the "Live Game" button
			HtmlAnchor livePageAnchor = profilePage.getFirstByXPath("//a[@class='SpectateTabButton']");
			livePage = livePageAnchor.click();
			//Wait for javascript to load on page
			Thread.sleep(8000);

			//Gets names, ranks, W/L ratio of all players in the game
			List<HtmlElement> names = livePage.getByXPath("//a[@class='SummonerName']");
			List<HtmlElement> ranks = livePage.getByXPath("//td[@class='CurrentSeasonTierRank Cell']/div[@class='TierRank']");
			List<HtmlElement> ratio = livePage.getByXPath("//td[@class='RankedWinRatio Cell']");

			//Prints result of above lists and saves into a Summoner object array
			players = new Summoner[names.size()];
			for(int i = 0; i < 10; i++) {
				//System.out.println("Name: " + names.get(i).asText());
				if(ranks.get(i).asText().contains("Level")) {
					//System.out.println("Rank: Unranked (" + ranks.get(i).asText() + ")");
					players[i] = new Summoner(names.get(i).asText(), "Unranked (" + ranks.get(i).asText() + ")", ratio.get(i).asText());
				}
				else {
					//System.out.println("Rank: " + ranks.get(i).asText());
					players[i] = new Summoner(names.get(i).asText(), ranks.get(i).asText(), ratio.get(i).asText());
				}
				//System.out.println("Ranked W/L Ratio: " + ratio.get(i).asText() + "\n");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			//System.out.println("Invalid Name or Summoner is not in a game");
		} catch (IndexOutOfBoundsException e) {
			//System.out.println("Invalid Name or Summoner is not in a game");
		}
	}

	private void championAutoSearch(WebClient client) {

		try {
			//Detects the champion you are using and looks up its op.gg page
			HtmlTableRow select = livePage.getFirstByXPath("//tr[td/a[text()='"+name+"']]");
			HtmlAnchor championSelected= (HtmlAnchor) select.getFirstElementChild().getFirstElementChild();
			championPage = client.getPage("http://na.op.gg"+championSelected.getAttribute("href"));
			skillBuild = skillBuild(client);
			itemList = itemList(client);
			itemBuild = itemBuild(client);
			boots = bootsBuild(client);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NullPointerException e ) {
			//System.out.println("Invalid Name or Summoner is not in a game");
		}
	}

	private List<HtmlElement> skillBuild(WebClient client) throws InterruptedException, FailingHttpStatusCodeException, MalformedURLException, ElementNotFoundException, IOException {
		Thread.sleep(2000);
		//Gets table rows of recommended skill build
		HtmlPage skillPage = client.getPage("http://na.op.gg" + championPage.getAnchorByText("Skills").getAttribute("href"));
		List<HtmlElement> skills = skillPage.getByXPath("//ul[@class='champion-stats__list']/li/img");
		skills.subList(5, skills.size()).clear();
		return skills;
	}

	private List<HtmlElement> itemList(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, ElementNotFoundException, IOException{

		//Open champion's recommended items list
		HtmlPage itemPage = client.getPage("http://na.op.gg" + championPage.getAnchorByText("Items").getAttribute("href"));
		List<HtmlElement> items = itemPage.getByXPath("//div[@class='champion-stats__single__item']/span");
		items = items.subList(3, items.size());
		return items;
	}
	
	private List<HtmlElement> itemBuild(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, ElementNotFoundException, IOException{
		HtmlPage itemPage = client.getPage("http://na.op.gg" + championPage.getAnchorByText("Items").getAttribute("href"));
		List<HtmlElement> items = itemPage.getByXPath("//tbody[@aria-live='polite']//img");
		items.subList(15, items.size()).clear();
		return items;
	}

	private List<HtmlElement> bootsBuild(WebClient client) throws FailingHttpStatusCodeException, MalformedURLException, ElementNotFoundException, IOException{
		HtmlPage itemPage = client.getPage("http://na.op.gg" + championPage.getAnchorByText("Items").getAttribute("href"));
		List<HtmlElement> boots = itemPage.getByXPath("//div[@class='champion-stats__single__item']//img");
		boots.subList(3, boots.size()).clear();
		return boots;
	}
}
