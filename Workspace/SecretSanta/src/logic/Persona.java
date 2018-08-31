package logic;

public class Persona {

	//-------------------------------------------
	//Atributos
	//-------------------------------------------
	
	private String fullName;
	
	private String family;
	
	private String email;
	
	private Persona giftTo;
	
	//-------------------------------------------
	//Constructor
	//-------------------------------------------
	
	public Persona(String pName, String pFamily, String pEmail){
		fullName = pName;
		family = pFamily;
		email = pEmail;
		giftTo = null;
	}
	
	//------------------------------------------
	//Metodos
	//------------------------------------------
	
	public String getName(){
		return fullName;
	}
	
	public String getFamily(){
		return family;
	}
	
	public String getEmail(){
		return email;
	}
	
	public Persona getTarget(){
		return giftTo;
	}
	
	public void setTarget(Persona pTarget){
		giftTo = pTarget;
	}
	
	@Override
	public String toString(){
		return (fullName + " - " + email);
	}
	
}
