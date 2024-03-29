package momobot.websearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WebQuery {
    /**
     * 
     */
    private static final String FTP       = "ftp";
    /**
     * 
     */
    private static final String GAMETIGER = "gametiger";
    /**
     * 
     */
    private static final String GOOGLE    = "google";
    /**
     * 
     */
    private static final String HTTP      = "http";
    /**
     * logger.
     */
    private static final Logger LOG       = Logger.getLogger(WebQuery.class);
    /**
     * 
     */
    private static final String YAHOO     = "yahoo";
    /**
     * longueur.
     */
    private final int           len;
    /**
     * le nombre de résultats.
     */
    private int                 numResult = 1;
    /**
     * la requete.
     */
    private String              query;
    /**
     * le séparateur de résultats.
     */
    private String              resultSep;
    /**
     * le type.
     */
    private final String        type;
    /**
     * url.
     */
    private HttpURL             url;

    /**
     * @param type1
     *            le type
     * @param query1
     *            la requete
     */
    public WebQuery(final String type1, final String query1) {
        this.type = type1;
        try {
            this.query = URLEncoder.encode(query1, "utf-8");
            setUrl();
        } catch (final UnsupportedEncodingException uee) {
            LOG.warn(uee, uee);
        } catch (final URIException urie) {
            LOG.warn(urie, urie);
        }
        this.len = this.resultSep.length();
    }

    /**
     * @return un iterateur sur les resultats
     */
    public final List<String> results() {
        final List<String> results = new ArrayList<String>(this.numResult);
        final GetMethod method = new GetMethod(this.url.toString());
        String page = null;
        try {
            new HttpClient().executeMethod(method);
            page = method.getResponseBodyAsString();
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        } finally {
            method.releaseConnection();
        }
        if (null == page || StringUtils.isBlank(page)) {
            return null;
        }
        int index;
        String work;
        if (this.type.equals(GAMETIGER)) {
            for (short k = 0; k < this.numResult; ++k) {
                index = page.indexOf(this.resultSep);
                if (index == -1) {
                    break;
                }
                page = page.substring(index + this.len);
                index = page.indexOf('>');
                work = page.substring(0, index);
                if ("\"".equals(work)) {
                    break;
                }
                page = page.substring(index);
                results.add(work);
            }
        } else if (this.type.equals(YAHOO)) {
            page = page.substring(page.indexOf("<h2>RESULTATS WEB</h2>"));
            forloop : for (short k = 0; k < this.numResult; ++k) {
                while (!page.startsWith(HTTP) && !page.startsWith(FTP)) {
                    index = page.indexOf(this.resultSep);
                    if (index < 0) {
                        break forloop;
                    }
                    page = page.substring(index + this.len);
                }
                index = page.indexOf('>');
                work = page.substring(0, index - 1);
                if (work.endsWith(" target=_blank")) {
                    /* Ce résultat est pas bon, faudra en prendre un autre */
                    ++this.numResult;
                } else {
                    results.add(work);
                }
                page = page.substring(index);
            }
        } else if (this.type.equals(GOOGLE)) {
            forloop : for (short k = 0; k < this.numResult; ++k) {
                while (!page.startsWith(HTTP) && !page.startsWith(FTP)) {
                    index = page.indexOf(this.resultSep);
                    if (index == -1) {
                        break forloop;
                    }
                    page = page.substring(index + this.len);
                }
                index = page.indexOf('>');
                work = page.substring(0, index);
                if (work.endsWith(" class=fl")) {
                    /* Ce résultat est pas bon, faudra en prendre un autre */
                    ++this.numResult;
                } else {
                    results.add(work);
                }
                page = page.substring(index);
            }
        }
        return results;
    }

    /**
     * @throws URIException
     */
    private void setUrl() throws URIException {
        if (this.type.equals(GAMETIGER)) {
            this.resultSep = "/search?address=";
            this.url = new HttpURL("gametiger.net", HttpURL.DEFAULT_PORT, "/search?game=cstrike&player=" + this.query);
        } else if (this.type.equals(GOOGLE)) {
            this.resultSep = "<a href=";
            this.numResult = 2;
            this.url = new HttpURL("www.google.fr", HttpURL.DEFAULT_PORT, "/search?hl=fr&ie=UTF-8&oe=UTF-8&num="
                    + this.numResult + "&q=" + this.query);
        } else if (this.type.equals(YAHOO)) {
            this.resultSep = "<a class=yschttl  href=\"";
            this.url = new HttpURL("fr.search.yahoo.com", HttpURL.DEFAULT_PORT, "/search?ie=UTF-8&num="
                    + this.numResult + "&p=" + this.query);
        }
    }
}
