package com.controller;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.domain.User;
import com.view.View_Admin;
import com.view.View_Loggin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sonia Castro (soniacastromartel@gmail.com)
 */
public class controllerAdmin implements ActionListener {

    View_Admin view_Admin;
    DefaultTableModel modelo;
    private static ObjectContainer db;
    File file;
    User user;

    public controllerAdmin(View_Loggin view_Loggin) {
	this.view_Admin = new View_Admin(view_Loggin, true);
	file = new File("usuarios.db4o");
	db = Db4oEmbedded.openFile(file.getAbsolutePath());
	initComponents();

	view_Admin.setVisible(true);
    }

    private void initComponents() {
	view_Admin.getBtn_Exit().addActionListener(this);
	view_Admin.getBtn_Insertar().addActionListener(this);
	view_Admin.getBtn_Delete().addActionListener(this);
	view_Admin.getBtn_Update().addActionListener(this);
	view_Admin.getTabla_Usuarios().setModel(fullSearch());
	view_Admin.getTabla_Usuarios().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) {
		    selected_row();

		}
	    }
	});

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == view_Admin.getBtn_Exit()) {
	    view_Admin.dispose();
	    db.close();

	}
	if (e.getSource() == view_Admin.getBtn_Insertar()) {
	    if (view_Admin.getTxt_Clave().getDocument().getLength() <= 0) {
		JOptionPane.showMessageDialog(view_Admin, "La clave no puede estar vacía");
	    } else {
		storeUsers();
	    }

	}
	if (e.getSource() == view_Admin.getBtn_Update()) {
	    if (view_Admin.getTxt_Clave().getDocument().getLength() <= 0) {
		JOptionPane.showMessageDialog(view_Admin, "La clave no puede estar vacía");
	    } else {
		updateUsers();
	    }

	}
	if (e.getSource() == view_Admin.getBtn_Delete()) {
	    deleteUsers();

	}

    }

//MODELO PARA LA TABLA
    private DefaultTableModel setTitles() {
	modelo = new DefaultTableModel() {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	modelo.addColumn("Clave");
	modelo.addColumn("Nombre");
	modelo.addColumn("Apellido");
	modelo.addColumn("Email");
	modelo.addColumn("Telefono");

	return modelo;
    }

    private DefaultTableModel fullSearch() {
	setTitles();
	User u = new User();
	ObjectSet<User> result = db.queryByExample(u);
	Object fila[] = new Object[5];
	for (int i = 0; i < result.size(); i++) {
	    fila[0] = result.get(i).getClave();
	    fila[1] = result.get(i).getNombre();
	    fila[2] = result.get(i).getApellido();
	    fila[3] = result.get(i).getEmail();
	    fila[4] = result.get(i).getTelefono();
	    modelo.addRow(fila);

	}
	return modelo;
    }

    //CREATE
    private void storeUsers() {
	try {
	    user = new User();
	    setUser();
	    db.store(user);
	    JOptionPane.showMessageDialog(view_Admin, "Usuario añadido correctamente");
	    cleanTxtFields();
	    view_Admin.getTabla_Usuarios().removeAll();
	    view_Admin.getTabla_Usuarios().setModel(fullSearch());

	} catch (DatabaseFileLockedException | DatabaseClosedException e) {
	    e.printStackTrace(System.out);
	}

    }

    //UPDATE
    private void updateUsers() {
	try {
	    user = new User();
	    int row = view_Admin.getTabla_Usuarios().getSelectedRow();
	    if (row >= 0) {
		user.setNombre(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 1)));
	    }
	    ObjectSet<User> result = db.queryByExample(user);
	    if (result.hasNext()) {
		user = result.next();
		setUser();
		db.store(user);
	    }

	    JOptionPane.showMessageDialog(view_Admin, "Usuario actualizado correctamente");
	    cleanTxtFields();
	    view_Admin.getTabla_Usuarios().removeAll();
	    view_Admin.getTabla_Usuarios().setModel(fullSearch());

	} catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
	    e.printStackTrace(System.out);
	}

    }

    //DELETE
    private void deleteUsers() {
	try {
	    user = new User();
	    setUser();
	    ObjectSet<User> result = db.queryByExample(user);
	    if (result.hasNext()) {
		user = result.next();
		db.delete(user);
	    }

	    JOptionPane.showMessageDialog(view_Admin, "Usuario borrado correctamente");
	    cleanTxtFields();
	    view_Admin.getTabla_Usuarios().removeAll();
	    view_Admin.getTabla_Usuarios().setModel(fullSearch());

	} catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
	    e.printStackTrace(System.out);
	}

    }

    //DOCUMENT LIST
    private void listUsers() {
	try {
	    User p = new User();
	    ObjectSet<User> result = db.queryByExample(p);
	    while (result.hasNext()) {
		System.out.println(result.next());

	    }

	} catch (DatabaseClosedException | DatabaseFileLockedException | DatabaseReadOnlyException | Db4oIOException | IncompatibleFileFormatException | OldFormatException e) {
	    e.printStackTrace(System.out);
	}

    }

    //Table Row Selection
    private void selected_row() {
	int row = view_Admin.getTabla_Usuarios().getSelectedRow();
	if (row >= 0) {
	    view_Admin.getTxt_Clave().setText(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 0)));
	    view_Admin.getTxt_Nombre().setText(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 1)));
	    view_Admin.getTxt_Apellido().setText(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 2)));
	    view_Admin.getTxt_Email().setText(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 3)));
	    view_Admin.getTxt_Telefono().setText(String.valueOf(view_Admin.getTabla_Usuarios().getValueAt(row, 4)));

	}
    }

    //TextFields Cleaning
    private void cleanTxtFields() {
	view_Admin.getTxt_Clave().setText("");
	view_Admin.getTxt_Nombre().setText("");
	view_Admin.getTxt_Apellido().setText("");
	view_Admin.getTxt_Email().setText("");
	view_Admin.getTxt_Telefono().setText("");

    }

    //User Setting
    private void setUser() {
	user.setClave(view_Admin.getTxt_Clave().getText());
	user.setNombre(view_Admin.getTxt_Nombre().getText());
	user.setApellido(view_Admin.getTxt_Apellido().getText());
	user.setEmail(view_Admin.getTxt_Email().getText());
	user.setTelefono(view_Admin.getTxt_Telefono().getText());

    }

}
