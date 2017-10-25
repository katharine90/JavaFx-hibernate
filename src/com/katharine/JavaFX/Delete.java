package com.katharine.JavaFX;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.katharine.entity.Person;
import com.katharine.entity.Profession;
import com.katharine.entity.ProfileHolder;

import javafx.scene.control.TableView;

/**
 * @author kath
 * @version 1.0.0
 * @since 2018.10.25
 */

public class Delete {

/**
 * Deletes data from table and from database
 * @param table
 * @param clicked
 */
	public void deleteFromDatabe(TableView<ProfileHolder> table, int clicked) {
		Session session = new Configuration().configure().addAnnotatedClass(Person.class).addAnnotatedClass(Profession.class)
				.buildSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		table.getItems().removeAll(table.getSelectionModel().getSelectedItems());
		Profession proff = session.get(Profession.class, clicked);
		session.remove(proff);

		transaction.commit();
		session.getSessionFactory().close();
	}
}
