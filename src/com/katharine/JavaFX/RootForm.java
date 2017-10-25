package com.katharine.JavaFX;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * @author kath
 * @version 1.0.0
 * @since since 2018.10.25
 */
public class RootForm extends Application {

	Scene C;
	Scene R;
	Scene S;
	Scene main;
	Stage window;
/**
 * Starts gui application
 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		main = new Scene(grid, 400, 400);

		Text sceneTitle = new Text("Please select: ");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Button create = new Button("Insert new User");
		Button read = new Button("Read from database");
		Button search = new Button("Search for employee");

		create.setOnAction(this::createButtonAction);
		read.setOnAction(this::readButtonAction);
		search.setOnAction(this::searchButtonAction);


		grid.add(sceneTitle, 0, 0, 2, 1);
		grid.add(create, 0, 1);
		grid.add(read, 0, 2);
		grid.add(search, 0, 3);

		primaryStage.setTitle("Welcome!");
		primaryStage.setScene(main);
		primaryStage.show();
	}
/**
 * Calls Create.class
 * @param event
 */
	private void createButtonAction(ActionEvent event) {
		Create create = new Create();
		try {
			create.insert(window, C, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/**
 * Calls Read_update.class
 * @param event
 */
	private void readButtonAction(ActionEvent event) {

		Read_Update read = new Read_Update();
		try {
			read.readTable(window, main, R);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
/**
 * Calls Search.class
 * @param event
 */
	private void searchButtonAction(ActionEvent event) {
		Search s = new Search();
		s.searchDataBase(window, main, S);
	}

}
