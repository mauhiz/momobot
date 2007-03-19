package momobot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;

import utils.Utils;

/**
 * @author Administrator
 */
public class WebQuery {
    /**
     * longueur.
     */
    private int          len       = 0;

    /**
     * le nombre de résultats.
     */
    private int          numResult = 1;

    /**
     * la requete.
     */
    private String       query;

    /**
     * le séparateur de résultats.
     */
    private String       resultSep;

    /**
     * le type.
     */
    private final String type;

    /**
     * url.
     */
    private HttpURL      url       = null;

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
        } catch (final UnsupportedEncodingException e) {
            Utils.logError(getClass(), e);
        }
        if (this.type.equals("gametiger")) {
            try {
                this.url = new HttpURL("gametiger.net", HttpURL.DEFAULT_PORT,
                        "/search?game=cstrike&player=" + this.query);
            } catch (final URIException e) {
                e.printStackTrace();
            }
            this.resultSep = "/search?address=";
        } else if (this.type.equals("google")) {
            try {
                this.url = new HttpURL("www.google.fr", HttpURL.DEFAULT_PORT,
                        "/search?hl=fr&ie=UTF-8&oe=UTF-8&num=" + this.numResult
                                + "&q=" + this.query);
            } catch (final URIException e) {
                e.printStackTrace();
            }
            this.resultSep = "<a href=";
            this.numResult = 2;
        } else if (this.type.equals("yahoo")) {
            try {
                this.url = new HttpURL("fr.search.yahoo.com",
                        HttpURL.DEFAULT_PORT, "/search?ie=UTF-8&num="
                                + this.numResult + "&p=" + this.query);
            } catch (final URIException e) {
                e.printStackTrace();
            }
            this.resultSep = "<a class=yschttl  href=\"";
        }
        this.len = this.resultSep.length();
    }

    /**
     * @return un iterateur sur les resultats
     */
    public final Iterator < String > results() {
        final Set < String > results = new HashSet < String >(this.numResult);
        String page;
        try {
            final GetMethod gm = new GetMethod(this.url.toString());
            new HttpClient().executeMethod(gm);
            page = gm.getResponseBodyAsString();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
        if (page.length() == 0) {
            return null;
        }
        if (this.type.equals("yahoo")) {
            page = page.substring(page.indexOf("<h2>RESULTATS WEB</h2>"));
        }
        int i = 0;
        for (int k = 0; k < this.numResult; k++) {
            if (this.type.equals("gametiger")) {
                i = page.indexOf(this.resultSep);
                if (i == -1) {
                    return results.iterator();
                }
                page = page.substring(i + this.len);
                i = page.indexOf('>');
                final String work = page.substring(0, i);
                if (work.equals("\"")) {
                    break;
                }
                page = page.substring(i);
                results.add(work);
            } else if (this.type.equals("yahoo")) {
                while (!page.startsWith("http") && !page.startsWith("ftp")) {
                    i = page.indexOf(this.resultSep);
                    if (i == -1) {
                        return results.iterator();
                    }
                    page = page.substring(i + this.len);
                }
                i = page.indexOf('>');
                final String work = page.substring(0, i - 1);
                if (!work.endsWith(" target=_blank")) {
                    results.add(work);
                } else {
                    k--;
                }
                page = page.substring(i);
            } else if (this.type.equals("google")) {
                while (!page.startsWith("http") && !page.startsWith("ftp")) {
                    i = page.indexOf(this.resultSep);
                    if (i == -1) {
                        return results.iterator();
                    }
                    page = page.substring(i + this.len);
                }
                i = page.indexOf('>');
                final String work = page.substring(0, i);
                if (!work.endsWith(" class=fl")) {
                    results.add(work);
                } else {
                    k--;
                }
                page = page.substring(i);
            }
        }
        return results.iterator();
    }
}
