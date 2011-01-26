package net.mauhiz.fbook.puzzle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.logging.Logger;

import net.mauhiz.fbook.CommandLineClient;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * see http://www.davideisenstat.com/fbpfaq/
 * @author mauhiz
 */
public class FbookEmail {
	
	private static final Logger LOG = Logger.getLogger("out");

	private static MultiPartEmail newGmail(String user, String pw) throws EmailException {
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(user, pw));
		email.setTLS(true);
		email.setFrom(user + "@gmail.com");

		return email;
	}

	public static void main(String... args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, EmailException, IOException {
		Properties props = new Properties();
		InputStream cfgFile = FileUtils.openInputStream(new File(CommandLineClient.PROJECT_FOLDER,
				"src/main/resources/account.properties"));
		try {
			props.load(cfgFile);
			
		} finally {
			cfgFile.close();
		}

		String mailUser = props.getProperty("gmail.user");
		String mailPassword = props.getProperty("gmail.password");
		boolean localTest = Boolean.parseBoolean(props.getProperty("local_test"));
		String targetAddress = findEmail();

		for (String clsName : args) {
			Class<?> solverCls = Class.forName(clsName);
			Object obj = solverCls.newInstance();
			if (obj instanceof ExportedRunnable) {
				ExportedRunnable solver = (ExportedRunnable) obj;
				File zip = solver.generatePack();

				if (zip != null) {
					MultiPartEmail email = newGmail(mailUser, mailPassword);
	
					email.setSubject(solver.getName());
					email.addTo(localTest ? email.getFromAddress().toString() : targetAddress);

					EmailAttachment attachment = new EmailAttachment();
					attachment.setPath(zip.getPath());
					attachment.setDisposition(EmailAttachment.ATTACHMENT);
					attachment.setName(zip.getName());
					email.attach(attachment);
	
					email.send();
					LOG.info("Application sent for problem: " + email.getSubject());
				}
			}
		}
	}

	/**
	 * note : bit operation on long underflows.
	 */
	public static String findEmail() {
		BigInteger source = new BigInteger("FACEB00C", 16);
		BigInteger result = source.shiftRight(2);
		return result.toString() + "@fb.com";
	}
}
