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
	private final String mysqlHost = "localhost";
	private final String mysqlUser = "cww";
	private final String mysqlPass = "355U7HYzfYEybw8f";
	private final String mysqlDB = "clanwar";
	private final String ircServer = "irc.quakenet.org";
	private final String ircChan = "#clanwar.fr";
	private final String ircNickname = "abbybobot";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Faire un lanceur plus propre ?
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
			mysqlConnection = DriverManager.getConnection("jdbc:mysql://"+mysqlHost+"/"+mysqlDB, mysqlUser, mysqlPass);
			
			// On vide les tables direct en arrivant
			// TODO : permettre un paramétrage de cette fonction ?
			Statement stmt = mysqlConnection.createStatement();
            String sql = "TRUNCATE TABLE `wars`;";
            stmt.executeUpdate(sql);
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}

		
		
		/*
		 * Connection au serveur IRC.
		 */
		this.setVerbose(true);
		this.setName(ircNickname);
		this.setLogin(ircNickname);
        try{
        	this.connect(ircServer);
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

		this.joinChannel(ircChan);
	}
	
	public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason){
		removeSeekFromUser(sourceNick);
	}
	
	public void onPart(String channel, String sender, String login, String hostname){
		removeSeekFromUser(sender);
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// On ignore les messages trop courts pour être utile
        if(message.length()<= 3) return;
        // On ignore les messages provenant de certains utilisateurs.
        for(int i=0; i<ignoreNickList.length; i++){
        	if(sender == ignoreNickList[i]) return;
        }
        
        // Commande de parsage des wars
        processMessage(sender, message);
    }
	

	/**
	 * Enleve les seeks des user ayant quit clanwar de la bdd
	 * @param user
	 */
	public void removeSeekFromUser(String user){
		try {
            Statement stmt = mysqlConnection.createStatement();
            String sql = "DELETE FROM `clanwar`.`wars` WHERE `user`='"+ user +"';";
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	public static String[] ignoreNickList = {"[CW|FR]", "Q" };
	public void processMessage(String sender, String message) {
        message = message.trim();
        //String lowMsg = message.toLowerCase();
        
        // TODO : est-ce que l'objet war est bien supprimé a chaque fois ?
        War war = new War(sender, message);
        
        // Si le parser n'a pas réussi a trouver une war on arrete
        
        if(war.isNull()) {
        	// TODO : Rajouter une option debug ou pas : pour faire une version du bot plus légère.
        	try {
                Statement stmt = mysqlConnection.createStatement();
                String sql = "INSERT INTO `untreated` (`id` ,`when` ,`user` ,`msg`) VALUES (NULL , NOW( ), '"+sender+"', '"+message+"');";
                stmt.executeUpdate(sql);

            } catch (SQLException e) {
            	// TODO Auto-generated catch block
    			e.printStackTrace();
            }
        	return;
        }
        
        
       
        /*
         * Insertion du message dans la bdd
         */
        try {
            Statement stmt = mysqlConnection.createStatement();
        
            // Prepare a statement to insert a record
            
            // On insert le nouveau seek, en remplacant l'ancien si un seek du meme joueur existe déjà
            String sql = "INSERT INTO `clanwar`.`wars` (`nbjoueurs` ,`serv` ,`lvl` ,`msg` ,`user` ,`when`) VALUES ('" + war.getNbjoueurs() + "', '"+ war.getServer().getCode()+"', '"+war.getLevel().getCode()+"', '"+message+"', '"+sender+"', NOW( )) ON DUPLICATE KEY UPDATE nbjoueurs='"+war.getNbjoueurs()+"', serv='"+war.getServer().getCode()+"', lvl='"+war.getLevel().getCode()+"', `when`=NOW( ), `msg`='"+message+"';";
            
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
