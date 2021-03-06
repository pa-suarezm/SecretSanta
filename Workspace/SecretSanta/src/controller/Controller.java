package controller;

import java.io.IOException;
import java.util.*;

import logic.Familia;
import logic.Persona;
import logic.SecretSantaManager;
import logic.exceptions.*;

public class Controller {

	//-------------------------------------------
	//Atributos
	//-------------------------------------------
	
	private static SecretSantaManager manager = new SecretSantaManager();
	
	//------------------------------------------
	//Metodos
	//------------------------------------------
	
	public static void sort() throws KeyNotFoundException, EmptyTableException{
		manager.sort();
	}
	
	public static void sendEmails() throws KeyNotFoundException, ListNotValidException, Exception{
		manager.sendEmails();		
	}
	
	public static void save(String pRuta) throws IOException{
		manager.save(pRuta);
	}
	
	public static List<Familia> getFamilies(){
		return manager.getFamilies();
	}
	
	public static void add(String name, String email, String family) throws RepeatedMemberException{
		manager.add(name, email, family);
	}
	
	public static void delete(logic.Persona eliminar){
		manager.delete(eliminar);
	}
	
	public static List<Persona> getMembers(String family){
		return manager.getMembers(family);
	}
	
	public static void saveLog(){
		manager.saveLog();
	}
	
	public static void load(String pRuta) throws IOException{
		manager.load(pRuta);
	}
}
