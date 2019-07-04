package be.voeren2000.bezettingsportkampen.service;

import be.voeren2000.bezettingsportkampen.util.PropertiesLoader;
import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.exception.PageNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Content;
import com.afrozaar.wordpress.wpapi.v2.model.Page;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class WordpressService {
    private final Wordpress wordpress;
    
    public WordpressService() {
        wordpress = createConnection();
    }
    
    private static Wordpress createConnection() {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();
            
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            
            requestFactory.setHttpClient(httpClient);
            
            PropertiesLoader propertiesLoader = new PropertiesLoader();
            String baseUrl = propertiesLoader.getString("url");
            String username = propertiesLoader.getString("user");
            String password = propertiesLoader.getString("password");
            boolean debug = Boolean.parseBoolean(propertiesLoader.getString("debug"));
            boolean usePermalinkEndpoint = Boolean.parseBoolean(propertiesLoader.getString("usePermalinkEndpoint"));
            
            return ClientFactory.builder(ClientConfig.of(baseUrl, username, password, usePermalinkEndpoint, debug))
                    .withRequestFactory(requestFactory).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException("Problem with setting up the connection");
        }
    }
    
    
    public Page getPage() {
        try {
            return wordpress.getPage(350L);
        } catch (PageNotFoundException e) {
            throw new RuntimeException("Could not find page");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Could not connect to REST endpoint");
        }
    }
    
    public Document toDocument(String rendered) {
        return Jsoup.parse(rendered);
    }
    
    
    public void insertNewContent(Document doc, Element divContainer) {
        doc.insertChildren(0, divContainer);
    }
    
    public void setPageContent(Page page, Document doc) {
        Content content = page.getContent();
        content.setRendered(doc.toString());
        content.setRaw(doc.toString());
        page.setContent(content);
    }
    
    public void updatePageOnAPI(Page page) {
        wordpress.updatePage(page);
    }
    
    public void removeOldData(Document doc) {
        doc.getElementById("sportkampen").remove();
    }
}
