import java.io.*;

import java.sql.*;

import org.jibble.pircbot.*;

/**
 * 
 * @author abby
 *
 */
public class ClanwarWatcherBot extends PircBot{
	java.sql.Connection mysqlConnection; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		ClanwarWatcherBot bot = new ClanwarWatcherBot();
		
	}
	
	public ClanwarWatcherBot() {
		/*
		 * Connexion au serveur sql
		 */
		String driverName = "org.gjt.mm.mysql.Driver"; // MySQL MM JDBC driver
        try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			mysqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/clanwar", "cww", "355U7HYzfYEybw8f");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}

		
		
		/*
		 * Connection au serveur IRC.
		 */
		this.setVerbose(true);
		this.setName("abbytestozor");
		this.setLogin("abbytestozor");
        try{
        	this.connect("irc.quakenet.org");
        }
        catch(NickAlreadyInUseException e){
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.joinChannel("#clanwar.fr");
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
        //String lowMsg = message.trim().toLowerCase();
        // TODO : filtrer certains types de messages ?
        
        processMessage(sender, message);
    }
	
	public void processMessage(String sender, String message) {
        message = message.trim();
        String lowMsg = message.toLowerCase();
        
        // TODO : est-ce que l'objet war est bien supprimé a chaque fois ?
        War war = new War(sender, message);
        
        // Si le parser n'a pas réussi a trouver une war on arrete
        // TODO : Stocker les messages dont le traitement a échoué quelque part ?
        if(war.isNull()) return;
        
        
       
        /*
         * Insertion du message dans la bdd
         */
        try {
            Statement stmt = mysqlConnection.createStatement();
        
            // Prepare a statement to insert a record
            
            // TODO : remplacer le seek d'un joueur portant le meme nom fait auparavant au lieu d'en rajouter un nouveau
            String sql = "INSERT INTO `clanwar`.`wars` (`nbjoueurs` ,`serv` ,`lvl` ,`msg` ,`user` ,`datetime`) VALUES ('" + war.getNbjoueurs() + "', '"+ war.getServer().getCode()+"', '"+war.getLevel().getCode()+"', '"+message+"', '"+sender+"', NOW( ));";
            
            // Execute the insert statement
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }

        
        
        // debug info
        //System.out.println(lowMsg);
    }
	
}
