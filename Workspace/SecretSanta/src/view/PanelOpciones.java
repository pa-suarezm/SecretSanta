package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;

public class PanelOpciones extends JPanel implements ActionListener{

	//------------------------------------------------
	//Constantes
	//------------------------------------------------

	private static final long serialVersionUID = 1408453319188800339L;

	public static final String AGREGAR = "AGREGAR";

	public static final String ELIMINAR = "ELIMINAR";

	public static final String SAVE = "GUARDAR";

	public static final String SORT = "SORT";

	public static final String SEND = "SEND";
	
	public static final String LOAD = "LOAD";

	//------------------------------------------------
	//Atributos
	//------------------------------------------------

	private SecretSantaUI main;

	private JButton btnAgregar;

	private JButton btnEliminar;

	private JButton btnGuardar;

	private JButton btnValidar;

	private JButton btnEnviar;
	
	private JButton btnCargar;

	//------------------------------------------------
	//Constructor
	//------------------------------------------------

	public PanelOpciones(SecretSantaUI inter){
		main = inter;

		TitledBorder border = BorderFactory.createTitledBorder("Opciones");
		border.setTitleColor(Color.BLUE);
		setBorder(border);

		setLayout(new GridLayout(1,9));
		
		ImageIcon imgAgregar = new ImageIcon("data/icons/agregar.png");
		btnAgregar = new JButton("Agregar", imgAgregar);
		btnAgregar.addActionListener(this);
		btnAgregar.setActionCommand(AGREGAR);
		add(btnAgregar);

		ImageIcon imgEliminar = new ImageIcon("data/icons/eliminar.png");
		btnEliminar = new JButton("Eliminar", imgEliminar);
		btnEliminar.addActionListener(this);
		btnEliminar.setActionCommand(ELIMINAR);
		add(btnEliminar);

		ImageIcon imgGuardar = new ImageIcon("data/icons/guardar.png");
		btnGuardar = new JButton("Guardar", imgGuardar);
		btnGuardar.addActionListener(this);
		btnGuardar.setActionCommand(SAVE);
		add(btnGuardar);
		
		ImageIcon imgCargar = new ImageIcon("data/icons/cargar.png");
		btnCargar = new JButton("Cargar", imgCargar);
		btnCargar.addActionListener(this);
		btnCargar.setActionCommand(LOAD);
		add(btnCargar);

		ImageIcon imgValidar = new ImageIcon("data/icons/validar.png");
		btnValidar = new JButton("Validar", imgValidar);
		btnValidar.addActionListener(this);
		btnValidar.setActionCommand(SORT);
		add(btnValidar);

		ImageIcon imgEnviar = new ImageIcon("data/icons/enviar.png");
		btnEnviar = new JButton("Enviar Correos", imgEnviar);
		btnEnviar.addActionListener(this);
		btnEnviar.setActionCommand(SEND);
		add(btnEnviar);		
	}

	//------------------------------------------------
	//Metodos
	//------------------------------------------------

	@Override
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if(cmd.equals(AGREGAR)){
			main.agregar();
			main.actualizar();
		}
		else if(cmd.equals(ELIMINAR)){
			main.eliminar();
			main.actualizar();
		}
		else if(cmd.equals(SAVE))
			main.save();
		else if(cmd.equals(SEND))
			main.send();
		else if(cmd.equals(SORT))
			main.sort();
		else if(cmd.equals(LOAD)){
			main.load();
			main.actualizar();
		}
	}

}
