package com.katharine.JavaFX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeListener;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.katharine.entity.Person;
import com.katharine.entity.ProfileHolder;
import com.katharine.entity.Profession;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

//https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html

/**
 * This class lists a table with database content, it also makes it possible to edit the cells and update the database
 * @author kath
 * @version 1.0.0
 * @since 2018.10.25
 */
public class Read_Update {

	TableView<ProfileHolder> table;
	TableColumn<ProfileHolder, String> area;
	TableColumn<ProfileHolder, Integer> exp;
	TableColumn<ProfileHolder, String> person;
	TableColumn<ProfileHolder, String> name;
	TableColumn<ProfileHolder, String> lname;
	TableColumn<ProfileHolder, String> email;

	Stage primaryStage;
	Session session;
	int clicked;
	String id;
	ImageView imv;
	Image image;
	FileOutputStream fop = null;
	
	/**
	 * 
	 * @param primaryStage
	 * @param home
	 * @param scene
	 * @throws FileNotFoundException
	 * 
	 * readTable displays the database table and all buttons
	 */

	public void readTable(Stage primaryStage, Scene home, Scene scene) throws FileNotFoundException {
		// java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		scene = new Scene(grid);

		Button back = new Button("Go back");
		back.setOnAction(e -> primaryStage.setScene(home));

		Button delete = new Button("Delete");
		delete.setOnAction(this::deleteButton);

		Button download = new Button("Download cv");
		download.setOnAction(event -> {
			Profession selectedPerson = displayTableValue();
			try {
				saveCVFile(selectedPerson);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		area = new TableColumn("Area");
		area.setMinWidth(200);
		area.setCellValueFactory(new PropertyValueFactory<>("area"));

		exp = new TableColumn("Experience");
		exp.setMinWidth(200);
		exp.setCellValueFactory(new PropertyValueFactory<>("experience"));

		person = new TableColumn("Person");
		person.setMinWidth(200);
		person.setCellValueFactory(new PropertyValueFactory<>("owner"));

		name = new TableColumn("Name");
		name.setMinWidth(200);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));

		lname = new TableColumn("Last name");
		lname.setMinWidth(200);
		lname.setCellValueFactory(new PropertyValueFactory<>("lname"));

		email = new TableColumn("Email");
		email.setMinWidth(200);
		email.setCellValueFactory(new PropertyValueFactory<>("email"));

		table = new TableView<>();
		table.setEditable(true);
		table.setItems(getPersonList());
		table.getColumns().addAll(area, exp, person);
		person.getColumns().addAll(name, lname, email);

		updateColumn();

		imv = new ImageView();
		imv.setFitHeight(150);
		imv.setFitWidth(150);
		HBox imageBox = new HBox(imv);

		table.setOnMouseClicked(event -> {
			Profession selectedPerson = displayTableValue();
			displayPhoto(selectedPerson);
		});

		HBox hbox = new HBox(back, delete, download);
		hbox.setSpacing(5);
		grid.add(table, 0, 1);
		grid.add(imageBox, 1, 1);
		grid.add(hbox, 0, 2);
		primaryStage.setTitle("Read from database");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * updateColumn edits the content of clicked area column, it also updates the content in of the conected database
	 */
	public void updateColumn() {
		session = new Configuration().configure().addAnnotatedClass(Person.class).addAnnotatedClass(Profession.class)
				.buildSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		Callback<TableColumn<Profession, String>, TableCell<Profession, String>> cellFactory = (
				TableColumn<Profession, String> p) -> new EditingCell();

		area.setCellFactory(TextFieldTableCell.<ProfileHolder>forTableColumn());
		area.setOnEditCommit((CellEditEvent<ProfileHolder, String> event) -> {

			event.getTableView().getItems().get(event.getTablePosition().getRow()).setArea(event.getNewValue());
			String modyfied = event.getNewValue();

			Profession modyfiedProfession = new Profession();
			modyfiedProfession = session.get(Profession.class, clicked);
			modyfiedProfession.setArea(modyfied);
			session.update(modyfiedProfession);

			transaction.commit();
			session.getSessionFactory().close();

		});
	}
/**
 * 
 * @param event
 * Calls Delete.class and its method deleteFromDatabse
 */
	private void deleteButton(ActionEvent event) {
		Delete del = new Delete();
		del.deleteFromDatabe(table, clicked);
	}
	
 /**
  * Displays any selected atribute from the database table by click of mouse on the chosen row
  * @return
  */
	public Profession displayTableValue() {
		// DISPLAYS DATABASE VALUE
		Profession selectedPerson = table.getSelectionModel().getSelectedItem().getProfession();
		clicked = selectedPerson.getProfessionId();
		String name = selectedPerson.getPerson().getName();
		id = Integer.toString(clicked);
		return selectedPerson;
	}
	
 /**
  * Displays chosen column value of the clicked row
  */
	public void displayColumnValue() {
		TablePosition pos = table.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		// Item here is the table view type:
		ProfileHolder item = table.getItems().get(row);

		TableColumn col = pos.getTableColumn();

		// this gives the value in the selected cell:
		Object data = col.getCellObservableValue(item).getValue();
		System.out.println("DATA: " + data);
	}
	
/**
 * Displays photo associated to the clicked row 
 * @param selectedPerson
 */
	public void displayPhoto(Profession selectedPerson) {
		byte[] pic = selectedPerson.getPerson().getImg();
		try {
			OutputStream output = new FileOutputStream("Files/Downloaded.jpg");
			output.write(pic);
			output.close();

			FileInputStream input = new FileInputStream("Files/Downloaded.jpg");
			image = new Image(input);
			imv.setImage(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/**
 * Downloads cv file from database to your desktop 
 * @param selectedPerson
 * @throws IOException
 */
	public void saveCVFile(Profession selectedPerson) throws IOException {
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		// Show save file dialog
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			String directory = file.getAbsolutePath();

			byte buf[] = selectedPerson.getPerson().getCv();
			fop = new FileOutputStream(directory);
			fop.write(buf);
			fop.close();
		}

	}
	
/**
 * Fills table with data from databse
 * @return
 */

	public ObservableList<ProfileHolder> getPersonList() {
		session = new Configuration().configure().addAnnotatedClass(Person.class).addAnnotatedClass(Profession.class)
				.buildSessionFactory().openSession();

		Transaction transaction = session.beginTransaction();

		Query<Profession> query = session.createQuery("from Profession");
		List<Profession> professionList = query.list();

		ObservableList<ProfileHolder> data = FXCollections.observableArrayList();

		professionList.forEach(profession -> {
			data.add(new ProfileHolder(profession, profession.getArea(), profession.getExperiance(),
					profession.getPerson().getName(), profession.getPerson().getLname(),
					profession.getPerson().getEmail()));
		});

		transaction.commit();
		session.getSessionFactory().close();

		return data;
	}

}
