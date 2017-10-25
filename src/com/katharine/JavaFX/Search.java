package com.katharine.JavaFX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.katharine.entity.Person;
import com.katharine.entity.Profession;
import com.katharine.entity.ProfileHolder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 * 
 * @author kath
 * @version 1.0.0
 * @since since 2018.10.25
 */
public class Search {

	TextField keyword;
	TableView<ProfileHolder> table = new TableView();
/**
 * Creates gui for search application
 * @param primaryStage
 * @param home
 * @param scene
 */
	public void searchDataBase(Stage primaryStage, Scene home, Scene scene) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		TableColumn<ProfileHolder, String> area = new TableColumn("Area");
		area.setMinWidth(200);
		area.setCellValueFactory(new PropertyValueFactory<>("area"));
		
		TableColumn<ProfileHolder, Integer> exp = new TableColumn("Experience");
		exp.setMinWidth(200);
		exp.setCellValueFactory(new PropertyValueFactory<>("experience"));
		
		TableColumn<ProfileHolder, String> name = new TableColumn("Name");
		name.setMinWidth(200);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<ProfileHolder, String> lname = new TableColumn("Last name");
		lname.setMinWidth(200);
		lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
				
		scene = new Scene(grid);

		Label label = new Label("Search by profession");
		keyword = new TextField();
		Button search = new Button("search");
		HBox hbox = new HBox(keyword, search);

		search.setOnAction(e -> { 
			String key = keyword.getText();
			table.setItems(getResult(key));
		});
		
		table.getColumns().addAll(area, exp, name, lname);

		Button back = new Button("Go back");
		back.setOnAction(e -> primaryStage.setScene(home));

		grid.add(label, 0, 1);
		grid.add(hbox, 0, 2);
		grid.add(table, 0, 3);
		grid.add(back, 0, 5);
		primaryStage.setTitle("Read from database");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Fills table with search results
	 * @param keyword
	 * @return
	 */
	public ObservableList<ProfileHolder> getResult(String keyword) {
		Session session = new Configuration().configure().addAnnotatedClass(Person.class)
				.addAnnotatedClass(Profession.class).buildSessionFactory().openSession();

		Transaction transaction = session.beginTransaction();

		// NÄR MAN JOBBAR MED HQL OCH SKA DEFINERA ENTITETSTYPER MÅSTE MAN ANVÄNDA ATTRIBUT SOM MAN SJÄLV HAR DÖPT INTE VAD DE HETER I TABELLERNA	
		// Såsom: "select p.area, p.experiance, a.name, a.lname, a.email from Profession p INNER JOIN p.person a where p.area like '%Mark%'"
		
		Query<Profession> query = session.createQuery("from Profession p where p.area like '%" + keyword + "%'");
		List<Profession> list = query.list();
		
		ObservableList<ProfileHolder> data = FXCollections.observableArrayList();
		
		list.forEach(profession -> {
			data.add(new ProfileHolder(profession, profession.getArea(), profession.getExperiance(),  profession.getPerson().getName(),
					profession.getPerson().getLname(), profession.getPerson().getEmail()));
		});	
		
		return data;
	}

}
