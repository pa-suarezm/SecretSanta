package view;

import java.awt.BorderLayout;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.Controller;
import logic.Familia;
import logic.exceptions.*;

import java.util.List;

public class SecretSantaUI extends JFrame{

	//------------------------------------------------
	//Constantes
	//------------------------------------------------

	private static final long serialVersionUID = 3097270821902603830L;

	//------------------------------------------------
	//Atributos
	//------------------------------------------------

	private String ultimoDirectorio;

	private PanelOpciones opciones;

	private PanelParticipantes participantes;

	//------------------------------------------------
	//Constructor
	//------------------------------------------------

	public SecretSantaUI(){

		ultimoDirectorio = "";
		try{
			this.setIconImage(ImageIO.read(new File("data/icons/giftbox.png")));
		}
		catch(Exception e){
			//Nada
		}
		
		setSize(880,640);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Secret Santa");
		setLayout(new BorderLayout());

		opciones = new PanelOpciones(this);
		add(BorderLayout.NORTH, opciones);

		participantes = new PanelParticipantes(this);
		add(BorderLayout.CENTER, participantes);
	}

	//------------------------------------------------
	//Metodos
	//------------------------------------------------

	public void sort(){
		try{
			Controller.sort();
		}
		catch(EmptyTableException f){
			JOptionPane.showMessageDialog(this, "No hay suficientes participantes para asignar", "EmptyTableException",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch(KeyNotFoundException e){
			JOptionPane.showMessageDialog(this, "Ocurri� un error inesperado. Revise la consola para m�s detalles", 
					"KeyNotFoundException", JOptionPane.ERROR_MESSAGE);
			return;
		}

		ImageIcon icon = new ImageIcon("data/icons/validar.png"); //TODO EXP
		JOptionPane.showMessageDialog(this, "La lista se valid� correctamente", "Success", JOptionPane.INFORMATION_MESSAGE, icon);

		Controller.saveLog();
	}

	public void send(){
		ImageIcon icon = new ImageIcon("data/icons/enviar.png");
		try{
			int option = JOptionPane.showConfirmDialog(this, "�Est� seguro que quiere enviar los correos?", "Enviar correos",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon);

			if(option == JOptionPane.NO_OPTION)
				return;

			JOptionPane.showMessageDialog(this, "Por favor no cierre la aplicaci�n"
					+ "\nSe mostrar� una ventana como esta cuando se env�en los correos", "Enviar correos", JOptionPane.WARNING_MESSAGE);
			Controller.sendEmails();
		}
		catch(ListNotValidException lnve){
			JOptionPane.showMessageDialog(this, lnve.getMessage(), "Lista no v�lida", JOptionPane.WARNING_MESSAGE);
			return;
		}
		catch(KeyNotFoundException knfe){
			JOptionPane.showMessageDialog(this, "Ocurri� un error inesperado. Revise la consola para m�s detalles", 
					"KeyNotFoundException", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this, "Ocurri� un error inesperado. Revise la consola para m�s detalles", 
					e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Se han enviado todos los correos", "Success", JOptionPane.INFORMATION_MESSAGE, icon);
	}

	public void save(){		
		JFileChooser fc = new JFileChooser( ultimoDirectorio );
		fc.setDialogTitle( "Guardar" );
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
		fc.setMultiSelectionEnabled( false );

		boolean termine = false;

		int resultado = fc.showSaveDialog( this );

		while( !termine ){
			if( resultado == JFileChooser.APPROVE_OPTION ){
				File seleccionado = fc.getSelectedFile( );
				ultimoDirectorio = seleccionado.getParentFile( ).getAbsolutePath( );

				int respuesta = JOptionPane.YES_OPTION;

				// Si el archivo ya existe hay que pedir confirmaci�n para sobrescribirlo
				if( seleccionado.exists( ) ){
					respuesta = JOptionPane.showConfirmDialog( this, "�Desea sobrescribir el archivo seleccionado?",
							"Sobrescribir", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
				}

				// Si la respuesta fue positiva (o si no fue necesario hacer la pregunta) se graba el archivo
				if( respuesta == JOptionPane.YES_OPTION ){
					try{
						Controller.save(seleccionado.getAbsolutePath());
						termine = true;
					}
					catch(IOException e){
						JOptionPane.showMessageDialog( this, "Hubo problemas guardando el archivo:\n" + e.getMessage( ),
								"Error", JOptionPane.ERROR_MESSAGE );
					}
				}
				else
					resultado = fc.showSaveDialog( this );
			}
			else
				termine = true;
		}
	}

	public void load(){
		JFileChooser fc = new JFileChooser(ultimoDirectorio);
		fc.setDialogTitle("Cargar");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled( false );

		boolean termine = false;

		int resultado = fc.showSaveDialog( this );

		while(!termine){
			if( resultado == JFileChooser.APPROVE_OPTION ){
				File seleccionado = fc.getSelectedFile( );
				ultimoDirectorio = seleccionado.getParentFile( ).getAbsolutePath( );
				if(seleccionado.exists()){
					try{
						Controller.load(seleccionado.getAbsolutePath());
						termine = true;
					}
					catch(IOException e){
						JOptionPane.showMessageDialog( this, "Hubo problemas cargando el archivo:\n" + e.getMessage( ),
								"Error", JOptionPane.ERROR_MESSAGE );
					}
				}
				else
					resultado = fc.showSaveDialog( this );
			}
			else
				termine = true;
		}
	}

	public void actualizar(){
		participantes.actualizar();
	}

	public List<Familia> getFamilias(){
		return Controller.getFamilies();
	}

	public void agregar(){

		try{
			String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del nuevo participante");

			if(nombre == null){
				JOptionPane.showMessageDialog(this, "El nombre que ingres� no es v�lido", "Nombre no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(nombre.trim().equals("")){
				JOptionPane.showMessageDialog(this, "El nombre que ingres� no es v�lido", "Nombre no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String correo = JOptionPane.showInputDialog(this, "Ingrese el correo del nuevo participante");


			if(correo == null){
				JOptionPane.showMessageDialog(this, "El correo que ingres� no es v�lido", "Correo no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if((correo.split("@").length<2)){
				JOptionPane.showMessageDialog(this, "El correo que ingres� no es v�lido", "Correo no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(correo.trim().equals("")){
				JOptionPane.showMessageDialog(this, "El correo que ingres� no es v�lido", "Correo no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String familia = JOptionPane.showInputDialog(this, "Ingrese el nombre de la familia a la que pertenece");

			if(familia == null){
				JOptionPane.showMessageDialog(this, "El nombre que ingres� no es v�lido", "Nombre no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(familia.trim().equals("")){
				JOptionPane.showMessageDialog(this, "El nombre que ingres� no es v�lido", "Nombre no v�lido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try{
				Controller.add(nombre, correo, familia);
			}
			catch(RepeatedMemberException rme){
				JOptionPane.showMessageDialog(this, "El nombre que ingres� ya se encuentra registrado", "RepeatedMemberException",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		catch(NullPointerException e){
			//Nada
		}
	}

	public void eliminar(){
		if(participantes.getSelectedObject() != null){
			logic.Persona eliminado = ((logic.Persona) participantes.getSelectedObject());
			Controller.delete(eliminado);
		}
		else{
			JOptionPane.showMessageDialog(this, "Debe seleccionar a un participante", "No se puede eliminar",
					JOptionPane.WARNING_MESSAGE);
		}
		actualizar();
	}

	public List<logic.Persona> getMiembros(String familyName){
		return Controller.getMembers(familyName);
	}

	//------------------------------------------------
	//Main
	//------------------------------------------------

	public static void main(String[] args) {
		SecretSantaUI ui = new SecretSantaUI();
		ui.setVisible(true);
	}
}
