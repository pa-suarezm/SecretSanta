package logic;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	//-----------------------------------------------------------
	//Constantes
	//-----------------------------------------------------------

	public static final String SENDER_EMAIL = "bedoyasecretsanta2017@gmail.com";

	public static final String SENDER_HOST = "smtp.gmail.com";

	public static final String SENDER_PASSWORD = "Holakase33";

	public static final String SUBJECT = "Secret Santa 2017";

	public static final String MESSAGE = "Mensaje de prueba";

	public static final boolean sessionDebug = false;

	//--------------------------------------------------------------
	//Atributos
	//--------------------------------------------------------------

	private static String receiverEmail = "jcbedoya3@gmail.com";

	//--------------------------------------------------------------
	//Main
	//--------------------------------------------------------------

	public static void main(String[] args) {
		try{
			Properties props = System.getProperties();

			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SENDER_HOST);
			props.put("mail.smtp.port", "587"); //PUERTO
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.trust", SENDER_HOST); //POR SI EXPLOTA
			props.put("mail.smtp.starttls.required", "true");

			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); //!!!
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug); //Debug
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(SENDER_EMAIL)); //Sender
			InternetAddress[] address = {new InternetAddress(receiverEmail)}; //Receiver
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(SUBJECT);
			msg.setSentDate(new Date());
			msg.setText(MESSAGE);

			Transport transport = mailSession.getTransport("smtp");
			transport.connect(SENDER_HOST, SENDER_EMAIL, SENDER_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("---------------------------------------------------------------");
			System.out.println("Message sent to " + receiverEmail);
			System.out.println("---------------------------------------------------------------");
		}	
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
