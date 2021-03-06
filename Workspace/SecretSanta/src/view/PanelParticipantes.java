package view;

import javax.swing.*;
import javax.swing.border.*;

import logic.Familia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelParticipantes extends JPanel{

	//------------------------------------------------------------
	//Constantes
	//------------------------------------------------------------

	private static final long serialVersionUID = -1797198812652092492L;

	//------------------------------------------------------------
	//Atributos
	//------------------------------------------------------------

	private SecretSantaUI main;

	private JList<Object> listFamilias;

	private JScrollPane familias;

	private JList<Object> listMiembros;

	private JScrollPane miembros;

	//------------------------------------------------------------
	//Constructor
	//------------------------------------------------------------

	public PanelParticipantes(SecretSantaUI inter){
		main = inter;

		setLayout(new BorderLayout());

		TitledBorder border = BorderFactory.createTitledBorder("Participantes");
		border.setTitleColor(Color.BLUE);
		setBorder(border);

		List<logic.Familia> f = main.getFamilias();
		Object[] fam = f.toArray();

		listFamilias = new JList<Object>(fam);

		familias = new JScrollPane(listFamilias);
		listFamilias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFamilias.setSelectedIndex(0);
		add(BorderLayout.WEST, familias);

		listMiembros = new JList<Object>();

		miembros = new JScrollPane(listMiembros);
		listMiembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFamilias.setSelectedIndex(0);
		
		add(BorderLayout.CENTER, miembros);
		listFamilias.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("rawtypes")
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				int index = list.locationToIndex(evt.getPoint());
				listFamilias.setSelectedIndex(index);
				logic.Familia actual = ((Familia) listFamilias.getSelectedValue());				
				if(actual != null){
					List<logic.Persona> p = main.getMiembros(actual.getName());
					Object[] mem = p.toArray();
					listMiembros.setListData(mem);
				}
				actualizarMiembros();
			}
		});
	}

	//------------------------------------------------------------
	//Metodos
	//------------------------------------------------------------

	public Object getSelectedObject(){
		return listMiembros.getSelectedValue();
	}

	public void actualizar(){
		List<logic.Familia> f = main.getFamilias();
		Object[] fam = f.toArray();
		listFamilias.setListData(fam);
		
		actualizarMiembros();
	}
	
	public void actualizarMiembros(){
		logic.Familia actual = ((Familia) listFamilias.getSelectedValue());

		if(actual != null){
			List<logic.Persona> p = main.getMiembros(actual.getName());
			
			Object[] mem = p.toArray();
			listMiembros.setListData(mem);
			listMiembros.setSelectedIndex(0);
		}
		else
			listMiembros.setListData(new Object[0]);
		
		miembros.removeAll();
		miembros.add(listMiembros);
		miembros.revalidate();
		miembros.repaint();
	}
}
