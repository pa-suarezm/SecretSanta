package logic.exceptions;

public class RepeatedMemberException extends Exception{

	private static final long serialVersionUID = 7966732192407088480L;

	public RepeatedMemberException(String pMiembro, String pFamilia){
		super("La persona " + pMiembro + "ya está registrada en la familia " + pFamilia);
	}
	
}
