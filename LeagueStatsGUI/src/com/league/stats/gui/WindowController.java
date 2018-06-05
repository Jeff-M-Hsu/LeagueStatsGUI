package com.league.stats.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WindowController {
	public WindowController() {
	}
	
	public void initialize() {
	}
	@FXML
	private void close(ActionEvent event) {
		Platform.exit();
	}
}
