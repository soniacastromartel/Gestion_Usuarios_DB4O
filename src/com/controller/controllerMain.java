package com.controller;

import com.db4o.*;
import com.db4o.ext.*;
import com.db4o.query.Predicate;
import com.domain.Persona;
import com.view.View_Loggin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Sonia Castro (soniacastromartel@gmail.com)
 */
public class controllerMain implements ActionListener {

    View_Loggin view_Loggin;
    Persona persona, persona2;
    private static ObjectContainer db;
    File file;

    public controllerMain() {
	this.view_Loggin = new View_Loggin();
	view_Loggin.setVisible(true);
	file = new File("gestion.db4o");
	//db = Db4oEmbedded.openFile(file.getAbsolutePath());
	storeData();
	initComponents();
	listUsers();
    }

    private void initComponents() {
	view_Loggin.getBtn_Loggin().addActionListener(this);
	view_Loggin.getTxt_nombre().addActionListener(this);
	view_Loggin.getPassTxt_password().addActionListener(this);
	view_Loggin.getBtn_Exit().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == view_Loggin.getBtn_Loggin()) {
	    login();

	}
	if (e.getSource() == view_Loggin.getBtn_Exit()) {
	    System.exit(0);
	}

    }

    private void login() {
	List<Persona> personas = db.query(new Predicate<Persona>() {
	    @Override
	    public boolean match(Persona p) {
		return p.getNombre().equals(view_Loggin.getTxt_nombre().getText()) && p.getPassword().equals(new String(view_Loggin.getPassTxt_password().getPassword()));
	    }
	});
	if (personas.size() > 0) {
	    if (personas.get(0).isAdmin()) {
		controllerAdmin conAdmin = new controllerAdmin(view_Loggin);

	    } else {
		controllerUser conUser= new controllerUser(view_Loggin);
	    }

	} else {
	    JOptionPane.showMessageDialog(view_Loggin, "Loggin Incorrecto!!");

	}

    }

    //CREATE
    private void storeData() {
	try {
	    file.delete();
	    db = Db4oEmbedded.openFile(file.getAbsolutePath());
	    persona = new Persona("admin", "admin", true);
	    persona2 = new Persona("user", "user", false);
	    db.store(persona);
	    db.store(persona2);
	} catch (DatabaseFileLockedException e) {
	    e.printStackTrace();
	} catch (DatabaseClosedException ex) {
	    ex.printStackTrace(System.out);
	} finally {
//	    db.close();
	}

    }

    //READ
    private void listUsers() {
	Persona p = new Persona();
	ObjectSet<Persona> result = db.queryByExample(p);
	while (result.hasNext()) {
	    System.out.println(result.next());

	}
    }
    

}
