package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import logic.data_structures.LPHashTable;
import logic.exceptions.*;

import java.io.*;

public class SecretSantaManager {

	//---------------------------------------------
	//Constantes
	//---------------------------------------------

	public static final int MAX_TRIES = 500;
	
	public static final String LOG_ROUTE = "data/logs/log.txt";

	//---------------------------------------------
	//Atributos
	//---------------------------------------------

	private SendEmail sendEmail;

	private LPHashTable<String, Persona> gifters;

	private boolean validated;

	private List<Familia> familias;

	//---------------------------------------------
	//Constructor
	//---------------------------------------------

	public SecretSantaManager(){
		sendEmail = new SendEmail();
		gifters = new LPHashTable<String, Persona>(17);
		validated = false;
		familias = new ArrayList<Familia>();
	}

	//---------------------------------------------
	//Metodos
	//---------------------------------------------

	/**
	 * Asigna los santas secretos de manera aleatoria y valida que la combinaci�n sea
	 * una combinaci�n v�lida. En caso de no serlo vuelve a armar la lista hasta que
	 * sea v�lida o se hayan alcanzado el n�mero m�ximo de intentos; lo que ocurra
	 * primero
	 */
	public void sort() throws KeyNotFoundException, EmptyTableException{
		if((!(gifters.isEmpty())) && (gifters.getN()>=3)){
			Object[] llaves = gifters.keysAsArray();
			int i;
			for(i = MAX_TRIES; i>=0 && !validated; i--){
				Iterator<String> keys = gifters.keys();

				//Sortear
				List<Object> temp = Arrays.asList(llaves);
				Collections.shuffle(temp);
				llaves = temp.toArray();

				//Asignar
				String key = null;
				while(key == null){
					key = keys.next();
				}
				Persona actual = gifters.get(key);
				int cntActual = -1;
				String target = null;
				while(keys.hasNext() && cntActual<gifters.getM()){	

					while(target == null){ 
						cntActual++;
						target = ((String) llaves[cntActual]);
					}

					actual.setTarget(gifters.get(target));
					gifters.put(key, actual);

					//Avanzar
					key = keys.next();
					while(key == null){
						try{
							key = keys.next();
						}
						catch(NoSuchElementException e){
							break;
						}
					}
					if(key != null){
						actual = gifters.get(key);
						cntActual ++;
						target = ((String) llaves[cntActual]);
					}
				}	
				//Validar
				validated = validate();
			}
			
			if(i<=0)
				freeSort();
		}
		else
			throw new EmptyTableException();
	}

	public void freeSort() throws KeyNotFoundException{
		Object[] llaves = gifters.keysAsArray();
		while(!validated){
			Iterator<String> keys = gifters.keys();

			//Sortear
			List<Object> temp = Arrays.asList(llaves);
			Collections.shuffle(temp);
			llaves = temp.toArray();

			//Asignar
			String key = null;
			while(key == null){
				key = keys.next();
			}
			Persona actual = gifters.get(key);
			int cntActual = -1;
			String target = null;
			while(keys.hasNext() && cntActual<gifters.getM()){	

				while(target == null){ 
					cntActual++;
					target = ((String) llaves[cntActual]);
				}

				actual.setTarget(gifters.get(target));
				gifters.put(key, actual);

				//Avanzar
				key = keys.next();
				while(key == null){
					try{
						key = keys.next();
					}
					catch(NoSuchElementException e){
						break;
					}
				}
				if(key != null){
					actual = gifters.get(key);
					cntActual ++;
					target = ((String) llaves[cntActual]);
				}
			}
			
			validated = freeValidate();
		}
	}
	
	private boolean freeValidate(){
		Iterator<String> keys = gifters.keys();
		
		String key = null;
		while(key == null && keys.hasNext())
			key = keys.next();
		
		while(keys.hasNext()){
			try{
				Persona actual = gifters.get(key);
				if(actual.getTarget().equals(actual) || actual.getTarget() == null)
					return false;
				key = keys.next();
				while(key == null && keys.hasNext())
					key = keys.next();
				if(key == null)
					break;
			}
			catch(KeyNotFoundException e){
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Si la colecci�n de participantes ya est� validada, env�a los correos correspondientes.
	 * La interfaz gr�fica es la que se encargar� de verificar que los correos asociados a
	 * cada persona sea medianamente v�lido. Se asume que tanto el correo como el dominio al
	 * que se est� enviando existen
	 */
	public void sendEmails()throws Exception{
		if(validated){
			Iterator<String> keys = gifters.keys();
			String key = null;
			while(key == null){
				key = keys.next();
			}

			Persona actual = gifters.get(key);
			while(keys.hasNext()){

				String message = "Hola " + actual.getName() + "\nLa persona a la que le tienes que dar un regalo es " + 
						actual.getTarget().getName() + ".";
				sendEmail.setMessage(message);
				sendEmail.setReceiver(actual.getEmail());
				sendEmail.sendEmail();

				key = keys.next();
				while(key == null){
					try{
						key = keys.next();
					}
					catch(NoSuchElementException e){
						break;
					}
				}

				if(key != null)
					actual = gifters.get(key);
			}
		}
		else
			throw new ListNotValidException();
	}

	/**
	 * Guarda la informaci�n actual del sistema
	 */
	public void save(String pRuta)throws IOException{
		if(!pRuta.contains(".txt"))
			pRuta+=".txt";
		
		File f = new File(pRuta);
		if(!f.exists())
			f.createNewFile();
		
		PrintWriter out = new PrintWriter(f);

		out.println(gifters.getN()-1);
		
		Iterator<String> keys = gifters.keys();
		String key = null;
		
		while(key == null && keys.hasNext())
			key = keys.next();
		
		while(keys.hasNext()){
			Persona actual = null;
			try{
				actual = gifters.get(key);
				out.println(actual.getName() + "," + actual.getEmail() + "," + actual.getFamily());
			}
			catch(KeyNotFoundException e){
				//Nada
			}
			key = keys.next();
			while(key == null && keys.hasNext())
				key = keys.next();			
		}
		
		out.close();
	}
	
	/**
	 * Carga la informaci�n actual del sistema a partir de la ruta que entra
	 */
	public void load(String pRuta)throws IOException{
		gifters.reset();
		familias.clear();
		BufferedReader in = new BufferedReader(new FileReader(new File(pRuta)));
		int cnt = Integer.parseInt(in.readLine().trim());
		for(int i=0; i<cnt; i++){
			String line = in.readLine();
			String[] datos = line.split(",");
			try{
				add(datos[0], datos[1], datos[2]);
			}
			catch(RepeatedMemberException e){
				//Nada
			}
		}
		in.close();
	}

	/**
	 * Valida que los participantes sean un santa secreto v�lido. Un santa secreto es
	 * v�lido s� y solo s� tiene un objetivo, dicho objetivo no es �l mismo y el objetivo
	 * es de distinta familia
	 * @return True si todos los participantes son santas v�lidos. False en caso contrario
	 */
	private boolean validate(){
		Iterator<String> names = gifters.keys();

		String nActual = null;
		while(nActual == null){
			nActual = names.next();
		}

		try{
			Persona actual = gifters.get(nActual);
			while(names.hasNext()){
				if(actual.getTarget() == null)
					return false;
				else{
					Persona target = actual.getTarget();
					if(actual.getFamily().equals(target.getFamily()) || actual.equals(target))
						return false;
				}

				nActual = names.next();
				while(nActual == null && names.hasNext())
					nActual = names.next();
				if(nActual != null)
					actual = gifters.get(nActual);
				else
					break;
			}
		}
		catch(KeyNotFoundException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Familia> getFamilies(){
		return familias;
	}

	private void createFamily(String name){		
		Familia aux = new Familia(name);
		familias.add(aux);
	}

	public void add(String name, String email, String family)throws RepeatedMemberException{
		int index = -1;
		for(int i=0; i<familias.size() && (index == -1); i++){
			if(familias.get(i).getName().toLowerCase().equals(family.toLowerCase()))
				index = i;
		}

		Persona nuevo = new Persona(name, family, email);

		if(index != -1)
			familias.get(index).agregarMiembro(nuevo);
		else{
			createFamily(family);
			familias.get(familias.size()-1).agregarMiembro(nuevo);
		}

		gifters.put((name+family+email).toLowerCase().trim(), nuevo);
	}

	public List<Persona> getMembers(String family){
		for(int i=0; i<familias.size(); i++){
			if(family.toLowerCase().equals(familias.get(i).getName().toLowerCase())){
				return (familias.get(i).getMembers());
			}
		}

		return new ArrayList<Persona>();
	}

	public void delete(Persona eliminar){
		gifters.delete(eliminar.getName()+eliminar.getFamily()+eliminar.getEmail());
		for(int i=0; i<familias.size(); i++){
			Familia actual = familias.get(i);
			actual.eliminar(eliminar);
			if(actual.getMembers().isEmpty())
				familias.remove(i);
		}
	}
	
	public void saveLog(){
		Iterator<String> keys = gifters.keys();
		
		String key = null;
		while(key == null && keys.hasNext()){
			key = keys.next();
		}
		
		try{
			File f = new File(LOG_ROUTE);
			if(!f.exists())
				f.createNewFile();
			PrintWriter out = new PrintWriter(f);
			
			out.println("=====================================Secret Santa Log=====================================");
			out.println("Date: " + (new Date()));
			out.println("------------------------------------------------------------------------");
			while(keys.hasNext()){
				Persona actual = gifters.get(key);
				Persona target = actual.getTarget();
				out.println(actual.getName() + " " + actual.getFamily() + "----------->" + target.getName() + " " + target.getFamily());
				out.println("------------------------------------------------------------------------");
				key = keys.next();
				while(key == null && keys.hasNext())
					key = keys.next();
			}
			
			out.println("==========================================================================================");
			
			out.close();
		}
		catch(Exception e){
			//Nada
		}
	}
}
