package com.controller;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oIOException;
import com.domain.User;
import com.view.View_Loggin;
import com.view.View_User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Sonia Castro (soniacastromartel@gmail.com)
 */
public class controllerUser implements ActionListener {

    View_User view_User;
    private static ObjectContainer db;
    File file;
    User user;

    public controllerUser(View_Loggin view_Loggin) {
	this.view_User = new View_User(view_Loggin, true);
	file = new File("usuarios.db4o");
	db = Db4oEmbedded.openFile(file.getAbsolutePath());
	initComponents();

	view_User.setVisible(true);
    }

    private void initComponents() {
	view_User.getTxt_FullName().setEditable(false);
	view_User.getBtn_Exit().addActionListener(this);
	view_User.getBtn_Search().addActionListener(this);
	view_User.getBtn_Update().addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == view_User.getBtn_Exit()) {
	    view_User.dispose();
	    db.close();
	}
	if (e.getSource() == view_User.getBtn_Search()) {
	    search();

	}

	if (e.getSource() == view_User.getBtn_Update()) {
	    update();

	}
    }

//FIND BY ID
    private void search() {
	user = new User();
	user.setClave(view_User.getTxt_Clave().getText());
	if (view_User.getTxt_Clave().getDocument().getLength() <= 0) {
	    view_User.getTxt_Clave().setEditable(true);
	    JOptionPane.showMessageDialog(view_User, "Debe introducir una clave");

	} else {
	    try {
		view_User.getTxt_Clave().setEditable(false);
		ObjectSet<User> result = db.queryByExample(user);
		if (result.hasNext()) {
		    user = result.next();
		    view_User.getTxt_FullName().setText(user.getNombre() + " " + user.getApellido());
		    view_User.getTxt_Email().setText(user.getEmail());
		    view_User.getTxt_Telefono().setText(user.getTelefono());
		} else {
		    view_User.getTxt_Clave().setEditable(true);
		    JOptionPane.showMessageDialog(view_User, "La clave no corresponde a ningún usuario");

		}
	    } catch (DatabaseClosedException | Db4oIOException e) {
		e.printStackTrace(System.out);
	    }
	}

    }

    //UPDATE CONTACT DATA
    private void update() {
	user = new User();
	user.setClave(view_User.getTxt_Clave().getText());
	try {
	    ObjectSet<User> result = db.queryByExample(user);
	    if (result.hasNext()) {
		user = result.next();
		user.setEmail(view_User.getTxt_Email().getText());
		user.setTelefono(view_User.getTxt_Telefono().getText());
		db.store(user);
	    }
	    JOptionPane.showMessageDialog(view_User, "Usuario actualizado correctamente");
	    cleanTxtFields();
	} catch (DatabaseClosedException | Db4oIOException e) {
	    e.printStackTrace(System.out);
	}

    }

    //TextFields Cleaning
    private void cleanTxtFields() {
	view_User.getTxt_Clave().setText("");
	view_User.getTxt_FullName().setText("");
	view_User.getTxt_Email().setText("");
	view_User.getTxt_Telefono().setText("");
	view_User.getTxt_Clave().setEditable(true);

    }

}
