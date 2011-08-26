package net.mauhiz.irc.bot.triggers.websearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.mauhiz.util.FileUtil;
import net.mauhiz.util.NetUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WebQuery {
    /**
     * 
     */
    private static final String FTP = "ftp";
    /**
     * 
     */
    private static final String GAMETIGER = "gametiger";
    /**
     * 
     */
    private static final String GOOGLE = "google";
    /**
     * 
     */
    private static final String HTTP = "http";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(WebQuery.class);
    /**
     * 
     */
    private static final String YAHOO = "yahoo";
    /**
     * longueur.
     */
    private final int len;
    /**
     * le nombre de resultats.
     */
    private int numResult = 1;
    /**
     * la requete.
     */
    private String query;
    /**
     * le separateur de resultats.
     */
    private String resultSep;
    /**
     * le type.
     */
    private final String type;
    /**
     * url.
     */
    private URI url;

    /**
     * @param type
     *            le type
     * @param rawQuery
     *            la requete
     */
    public WebQuery(String type, String rawQuery) {
        this.type = type;
        try {
            this.query = URLEncoder.encode(rawQuery, FileUtil.UTF8.name());

        } catch (UnsupportedEncodingException uee) {
            throw new IllegalStateException(uee);
        }
        try {
            setUrl();
        } catch (URISyntaxException urie) {
            throw new IllegalStateException(urie);
        }
        len = resultSep.length();
    }

    /**
     * @return un iterateur sur les resultats
     */
    public List<String> results() {
        String page;
        try {
            page = NetUtils.doHttpGet(url);
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
            return null;
        }

        if (StringUtils.isBlank(page)) {
            return null;
        }

        List<String> results = new ArrayList<String>(numResult);
        int index;
        String work;
        if (GAMETIGER.equals(type)) {
            for (int k = 0; k < numResult; ++k) {
                index = page.indexOf(resultSep);
                if (index == -1) {
                    break;
                }
                page = page.substring(index + len);
                index = page.indexOf('>');
                work = page.substring(0, index);
                if ("\"".equals(work)) {
                    break;
                }
                page = page.substring(index);
                results.add(work);
            }
        } else if (YAHOO.equals(type)) {
            page = page.substring(page.indexOf("<h2>RESULTATS WEB</h2>"));
            forloop: for (int k = 0; k < numResult; ++k) {
                while (!page.startsWith(HTTP) && !page.startsWith(FTP)) {
                    index = page.indexOf(resultSep);
                    if (index < 0) {
                        break forloop;
                    }
                    page = page.substring(index + len);
                }
                index = page.indexOf('>');
                work = page.substring(0, index - 1);
                if (work.endsWith(" target=_blank")) {
                    /* Ce resultat est pas bon, faudra en prendre un autre */
                    ++numResult;
                } else {
                    results.add(work);
                }
                page = page.substring(index);
            }
        } else if (GOOGLE.equals(type)) {
            int start = page.indexOf("<h2 class=hd>Resultats de recherche</h2>");
            if (start >= 0) {
                page = page.substring(start);
                forloop: for (int k = 0; k < numResult; ++k) {
                    while (true) {
                        index = page.indexOf(resultSep);
                        if (index == -1) {
                            break forloop;
                        }
                        page = page.substring(index + len);
                        if (page.startsWith(HTTP) || page.startsWith(FTP)) {
                            break;
                        }
                    }
                    index = page.indexOf("\" ");
                    work = page.substring(0, index);
                    /* TODO virer les liens de traduction */
                    results.add(work);
                    page = page.substring(index);
                }
            }
        }
        return results;
    }

    /**
     * @throws URISyntaxException
     */
    private void setUrl() throws URISyntaxException {
        if (GAMETIGER.equals(type)) {
            resultSep = "/search?address=";
            url = new URI(HTTP, "gametiger.net", "/search", "game=cstrike&player=" + query, null);
        } else if (GOOGLE.equals(type)) {
            resultSep = "<a href=\"";
            numResult = 2;
            url = new URI(HTTP, "www.google.fr", "/search", "hl=fr&ie=UTF-8&oe=UTF-8&num=" + numResult + "&q=" + query,
                    null);
        } else if (YAHOO.equals(type)) {
            resultSep = "<a class=yschttl  href=\"";
            url = new URI(HTTP, "fr.search.yahoo.com", "/search", "ie=UTF-8&num=" + numResult + "&p=" + query, null);
        }
    }
}
