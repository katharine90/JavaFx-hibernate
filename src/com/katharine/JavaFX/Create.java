package com.katharine.JavaFX;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.katharine.entity.Person;
import com.katharine.entity.Profession;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class inserts data of a new member to the database
 * @author kath
 * @version 1.0.0
 * @since since 2018.10.25
 */
public class Create {

	private Desktop desktop = Desktop.getDesktop();
	Stage primaryStage;
	byte[] cvData;
	byte[] imgData;

	TextField cvPath;
	TextField picPath;
	TextField userName;
	TextField userLname;
	TextField userEmail;
	TextField userArea;
	TextField userExp;
	Text actiontarget;
/**
 * Method insert displays a gui with textfields
 * @param primaryStage
 * @param scene
 * @param home
 * @throws IOException
 */
	public void insert(Stage primaryStage, Scene scene, Scene home) throws IOException {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);  
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		scene = new Scene(grid);

		Text sceneTitle = new Text("Please enter new user");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Label name = new Label("Name:");
		userName = new TextField();
		Label lastname = new Label("Last name:");
		userLname = new TextField();
		Label email = new Label("Email:");
		userEmail = new TextField();

		Label area = new Label("Profession: ");
		userArea = new TextField();
		Label exp = new Label("Years of experiance: ");
		userExp = new TextField();

		Button cvButton = new Button("add cv");
		cvPath = new TextField();
		Button picButton = new Button("add photo");
		picPath = new TextField();

		Button back = new Button("Go back");
		back.setOnAction(e -> primaryStage.setScene(home));

		cvButton.setOnAction(this::cvOpenDialog);
		picButton.setOnAction(this::imgOpenDialog);

		grid.add(sceneTitle, 0, 0, 2, 1); // colspan 2 columns but only one row
		grid.add(name, 0, 1);
		grid.add(userName, 1, 1);
		grid.add(lastname, 0, 2);
		grid.add(userLname, 1, 2);
		grid.add(email, 0, 3);
		grid.add(userEmail, 1, 3);
		grid.add(area, 0, 4);
		grid.add(userArea, 1, 4);
		grid.add(exp, 0, 5);
		grid.add(userExp, 1, 5);
		grid.add(cvButton, 0, 6);
		grid.add(cvPath, 1, 6);
		grid.add(picButton, 0, 7);
		grid.add(picPath, 1, 7);

		Button submit = new Button("add user");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(submit);
		grid.add(hbBtn, 1, 9);

		actiontarget = new Text();
		grid.add(actiontarget, 1, 11);
		grid.add(back, 0, 11);

		submit.setOnAction(this::insertDatabase);

		primaryStage.setTitle("Enter new user");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
/**
 * Method insertDatabase gets values from textfields and inserts them as a new row in the database
 * @param event
 */
	public void insertDatabase(ActionEvent event) {
		String path = cvPath.getText();
		String imagePath = picPath.getText();
		try {
			cvData = cvFile(path);
			imgData = imgFile(imagePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String expe = userExp.getText();
		int experiance = Integer.parseInt(expe);

		if (userName.getText().isEmpty() == false && userLname.getText().isEmpty() == false
				&& userEmail.getText().isEmpty() == false && userArea.getText().isEmpty() == false
				&& userExp.getText().isEmpty() == false) {

			try {
				Session session = new Configuration().configure().addAnnotatedClass(Person.class).buildSessionFactory()
						.openSession();

				Transaction transaction = session.beginTransaction();
				Person person = new Person();
				person.setName(userName.getText());
				person.setLname(userLname.getText());
				person.setEmail(userEmail.getText());
				person.setCv(cvData);
				person.setImg(imgData);
				session.persist(person);

				List<Profession> profession = new LinkedList<Profession>();
				profession.add(new Profession(userArea.getText(), experiance, person));

				Iterator i = profession.iterator();
				while (i.hasNext()) {
					session.persist(i.next());
				}

				transaction.commit();
				session.getSessionFactory().close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			actiontarget.setFill(Color.GREEN);
			actiontarget.setText("User added successfully");

		} else {
			actiontarget.setFill(Color.RED);
			actiontarget.setText("Empty fields!");
		}
	}

	/**
	 * Opens Filechooser to select a cv file the user can upload
	 * @param event
	 */
	public void cvOpenDialog(ActionEvent event) {
		FileChooser cvFile = new FileChooser();
		File file = cvFile.showOpenDialog(primaryStage);
		if (file != null) {
			cvPath.setText(file.getAbsolutePath());
		}
	}
/**
 * FileChooser to select a image file the user can upload
 * @param event
 */
	public void imgOpenDialog(ActionEvent event) {
		FileChooser picFile = new FileChooser();
		File file = picFile.showOpenDialog(primaryStage);
		if (file != null) {
			picPath.setText(file.getAbsolutePath());
		}
	}
/**
 * Converts a file to bytes, returns bytes
 * @param cvLinkPath
 * @return
 * @throws IOException
 */
	public static byte[] cvFile(String cvLinkPath) throws IOException {
		File cvfile = new File(cvLinkPath);
		byte[] cvData = new byte[(int) cvfile.length()];

		FileInputStream inputStream = new FileInputStream(cvfile);
		inputStream.read(cvData);
		inputStream.close();

		return cvData;
	}
/**
 * Converts a file to bytes, returns bytes
 * @param imgLinkPath
 * @return
 * @throws IOException
 */
	public static byte[] imgFile(String imgLinkPath) throws IOException {
		File imgFile = new File(imgLinkPath);
		byte[] imgData = new byte[(int) imgFile.length()];

		FileInputStream inputStream = new FileInputStream(imgFile);
		inputStream.read(imgData);
		inputStream.close();

		return imgData;
	}

}
