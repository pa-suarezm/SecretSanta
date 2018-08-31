package logic.exceptions;

public class EmptyTableException extends Exception{

	private static final long serialVersionUID = 1068160661158800680L;
	
	public EmptyTableException(){
		super("No hay nada en la tabla");
	}
}
