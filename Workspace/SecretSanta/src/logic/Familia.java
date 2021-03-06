package logic;

import java.util.*;

public class Familia {

	//-------------------------------------------
	//Atributos
	//-------------------------------------------
	
	private List<Persona> miembros;
	
	private String nombre;
	
	//-------------------------------------------
	//Constructor
	//-------------------------------------------
	
	public Familia(String pFamilia){
		nombre = pFamilia;
		miembros = new ArrayList<Persona>();
	}
	
	//--------------------------------------------
	//Metodos
	//--------------------------------------------
	
	public void agregarMiembro(Persona pMiembro){
		miembros.add(pMiembro);
	}
	
	public void eliminarMiembro(String pNombreMiembro){
		boolean found = false;
		for(int i=0; i<miembros.size() && !found; i++){
			if(miembros.get(i).getName().toLowerCase().equals((pNombreMiembro.toLowerCase()))){
				miembros.remove(i);
				found = true;
			}
		}
	}
	
	public String getName(){
		return nombre;
	}
	
	public List<Persona> getMembers(){
		return miembros;
	}
	
	@Override
	public String toString(){
		return (nombre + " (" + miembros.size() + ")");
	}
	
	public void eliminar(Persona eliminar){
		miembros.remove(eliminar);
	}
	
}
