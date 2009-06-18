package net.mauhiz.irc.base.data;

import java.util.Date;

/**
 * @author mauhiz
 * 
 */
public class Topic {
    private String byWhom;
    private Date lastModif;
    private String value;
    
    public Topic(String byWhom, Date lastModif, String value) {
        this.byWhom = byWhom;
        this.lastModif = lastModif;
        this.value = value;
    }
    /**
     * @return the byWhom
     */
    public String getByWhom() {
        return byWhom;
    }
    /**
     * @return the lastModif
     */
    public Date getLastModif() {
        return lastModif;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param byWhom
     *            the byWhom to set
     */
    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }
    /**
     * @param lastModif
     *            the lastModif to set
     */
    public void setLastModif(Date lastModif) {
        this.lastModif = lastModif;
    }
    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
