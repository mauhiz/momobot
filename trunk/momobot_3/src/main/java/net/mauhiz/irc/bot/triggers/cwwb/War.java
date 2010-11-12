package net.mauhiz.irc.bot.triggers.cwwb;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author abby
 */
public class War {
    
    /**
     * Regexp magique de FenX<br>
     * Inutile de mettre Pattern.CASE_INSENSITIVE<br>
     * Parfait pour les 5vs5, 4x4, ...
     */
    public static final Pattern NB_JOUEURS = Pattern.compile("(\\d+)\\s?(vs|vv|o|c|x|n|on|/|v)\\s?(\\d+)");
    
    // Pour les joueurs en 55, 44, 33...
    // public static Pattern patternNbJoueursShort = Pattern.compile("(\\d)(\\d)");
    
    private long id;
    private Level level = Level.UNKOWN;
    
    /**
     * nbjoueur = 0 <=> UNKNOWN
     */
    private int nbjoueurs;
    
    private ServerStatus server = ServerStatus.UNKOWN;
    private String user = "";
    private Calendar when;
    
    public War() {
        super();
    }
    
    /**
     * @param pUser
     * @param pSeekMessage
     */
    public War(String pUser, String pSeekMessage) {
        this();
        // Important
        String seekMessage = pSeekMessage.toLowerCase(Locale.FRENCH);
        if (seekMessage.contains("dispo") || seekMessage.contains("tn") || seekMessage.contains("last")
                || seekMessage.contains("binome") || seekMessage.contains("cherche team")) {
            return;
        }
        
        user = pUser;
        
        Matcher matcherNbJoueurs = NB_JOUEURS.matcher(seekMessage);
        // Matcher matcherNbJoueursShort = patternNbJoueursShort.matcher(SeekMessage);
        /*
         * On detecte le serveur
         */
        // TODO : faire une regexp pour ca aussi ?
        // Du style : (serv)(2 caract maxi)(on|0n|ok|0k)
        // Espace avant le ' on' pour eviter de matcher le 5on5 off
        // Ca ne marche plus pour '5 on 5' OFF
        if (seekMessage.contains(" on") || seekMessage.contains("servon") || seekMessage.contains("servok")
                || seekMessage.contains("serv ok") || seekMessage.contains("serv:ok")
                || seekMessage.contains("serv_on") || seekMessage.contains("serv0n") || seekMessage.contains("serv.on")
                || seekMessage.contains("have serv") || seekMessage.contains("gotserv")) {
            server = ServerStatus.ON;
        } else if (seekMessage.contains("off") || seekMessage.contains("0ff") || seekMessage.contains("noserv")
                || seekMessage.contains("no serv") || seekMessage.contains("pas de serv")
                || seekMessage.contains("pa de serv")) {
            server = ServerStatus.OFF;
        }
        
        /*
         * On detecte le nombre de joueurs
         */
        // TODO : a faire avec des regexp pour les differents nombres de joueurs.
        // (\d+) ?(vs|o|v|on) ?(\d+)
        // case insensitive
        if (matcherNbJoueurs.find()) {
            if (matcherNbJoueurs.group(1).compareTo(matcherNbJoueurs.group(3)) == 0) {
                nbjoueurs = Integer.parseInt(matcherNbJoueurs.group(1));
            }
            // for(int i=0; i<=matcherNbJoueurs.groupCount(); i++){
            // System.out.println("Groupe " + i + " : '" +
            // matcherNbJoueurs.group(i)+"'");
            // }
            // } else if(matcherNbJoueursShort.find()){
            // TODO tester le code ci dessous
            // if (matcherNbJoueurs.group(1).compareTo(matcherNbJoueurs.group(3)) == 0) {
            // this.nbjoueurs = Integer.parseInt(matcherNbJoueurs.group(1));
            // }
            // for (int i = 0; i <= matcherNbJoueursShort.groupCount(); i++) {
            // System.out.println("Groupe " + i + " : '" + matcherNbJoueursShort.group(i) + "'");
            // }
            
        } else {
            // TODO : mettre le 55 en regexp (pour que ca marche aussi avec 33)
            if (seekMessage.contains("pcw") || seekMessage.contains("war") || seekMessage.contains("pracc")
                    || seekMessage.contains("55") || seekMessage.contains("5 5")) {
                nbjoueurs = 5;
            } else if (seekMessage.contains("44") || seekMessage.contains("4 4")) {
                nbjoueurs = 4;
            } else if (seekMessage.contains("33") || seekMessage.contains("3 3")) {
                nbjoueurs = 3;
            } else if (seekMessage.contains("22") || seekMessage.contains("2 2")) {
                nbjoueurs = 2;
            }
        }
        
        /*
         * On detecte le lvl
         */
        if (seekMessage.contains("roxor")) {
            level = Level.ROXOR;
        } else if (seekMessage.contains("skilled") || seekMessage.contains("hard") || seekMessage.contains("high")) {
            level = Level.SKILLED;
        } else if (seekMessage.contains("good") || seekMessage.contains("goood")) {
            level = Level.GOOD;
        } else if (seekMessage.contains("mid+") || seekMessage.contains("mid +") || seekMessage.contains("midl+")
                || seekMessage.contains("mi +")) {
            level = Level.MIDPLUS;
        } else if (seekMessage.contains("mid") || seekMessage.contains("med")) {
            level = Level.MID;
        } else if (seekMessage.contains("low+") || seekMessage.contains("low +")) {
            level = Level.LOWPLUS;
        } else if (seekMessage.contains("low")) {
            level = Level.LOW;
        } else if (seekMessage.contains("noob") || seekMessage.contains("invincible")) {
            level = Level.NOOB;
        }
        
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    
    public Level getLevel() {
        return level;
    }
    public int getNbjoueurs() {
        return nbjoueurs;
    }
    
    public ServerStatus getServer() {
        return server;
    }
    public String getUser() {
        return user;
    }
    /**
     * @return the when
     */
    public Calendar getWhen() {
        return when;
    }
    public boolean isNull() {
        int nulllevel = 0;
        if (level == Level.UNKOWN) {
            nulllevel++;
        }
        if (server == ServerStatus.UNKOWN) {
            nulllevel++;
        }
        if (nbjoueurs == 0) {
            nulllevel++;
        }
        if (nulllevel >= 2) {
            return true;
        }
        return false;
    }
    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * @param level
     */
    public void setLevel(Level level) {
        this.level = level;
    }
    
    /**
     * @param nbjoueurs
     */
    public void setNbjoueurs(int nbjoueurs) {
        this.nbjoueurs = nbjoueurs;
    }
    
    /**
     * @param status
     */
    public void setServer(ServerStatus status) {
        server = status;
    }
    
    /**
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    /**
     * @param when
     *            the when to set
     */
    public void setWhen(Calendar when) {
        this.when = when;
    }
}
