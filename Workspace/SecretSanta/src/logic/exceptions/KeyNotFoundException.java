package logic.exceptions;

public class KeyNotFoundException extends Exception{
	
	private static final long serialVersionUID = 2895543028059478229L;

	public KeyNotFoundException(String key){
		super("The key " + key + " couldn't be found");
	}
	
}
