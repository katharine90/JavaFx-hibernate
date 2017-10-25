package com.katharine.application;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.katharine.JavaFX.RootForm;
import com.katharine.entity.Person;
import com.katharine.entity.Profession;

import javafx.application.Application;

public class App {
	public static void main(String[] args) throws IOException {

	   Application.launch(RootForm.class, args);

	}

}
