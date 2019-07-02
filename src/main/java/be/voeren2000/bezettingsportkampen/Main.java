package be.voeren2000.bezettingsportkampen;

import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import be.voeren2000.bezettingsportkampen.service.HtmlTable;
import be.voeren2000.bezettingsportkampen.service.ReadExcelFile;
import be.voeren2000.bezettingsportkampen.util.PropertiesLoader;
import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.exception.PageNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Content;
import com.afrozaar.wordpress.wpapi.v2.model.Page;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*args = new String[2];
        args[0] = "Lijst inschr. sportkamp ZOMER 2019.xlsx";
        args[1] = "Lijst inschr. kleuters ZOMER 2019.xlsx";*/
    
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        
            Element divContainer = new Element("div");
            divContainer.attr("id", propertiesLoader.getString("html_container_id"));
            for (int i = 0; i < args.length; i++) {
                try (Workbook workbook = WorkbookFactory.create(new File(args[i]))) {
                    RegistrationSheet registrationSheet = new RegistrationSheet(workbook);
                    ReadExcelFile file = new ReadExcelFile();
                    file.readFile(registrationSheet);
                    HtmlTable htmlTable = new HtmlTable();
                    String headerName = args[i].split(" ")[2];
                    headerName = headerName.substring(0,1).toUpperCase() + headerName.substring(1).toLowerCase();
                    divContainer.appendChild(htmlTable.createH1Header(headerName));
                    divContainer.appendChild(htmlTable.processRegistrations(registrationSheet));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            getPage(divContainer);
    }
    
    private static Page getPage(Element divContainer) {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        String baseUrl = propertiesLoader.getString("url");
        String username = propertiesLoader.getString("user");
        String password = propertiesLoader.getString("password");
        boolean debug = Boolean.parseBoolean(propertiesLoader.getString("debug"));
        boolean usePermalinkEndpoint = Boolean.parseBoolean(propertiesLoader.getString("usePermalinkEndpoint"));
    
        Wordpress wordpress = ClientFactory.fromConfig(ClientConfig.of(baseUrl, username, password, usePermalinkEndpoint, debug));
        
        try {
            Page page = wordpress.getPage(350l);
            Content content = page.getContent();
            Document doc = Jsoup.parse(content.getRendered());
            
            removeOldData(doc);
            insertNewData(doc, divContainer);
            
            content.setRendered(doc.body().toString());
            page.setContent(content);
            wordpress.updatePage(page);
            return page;
        } catch (PageNotFoundException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException hcee) {
            hcee.printStackTrace();
        }
        return null;
    }
    
    private static void removeOldData(Document doc) {
        doc.getElementById("omnisport").remove();
        doc.getElementById("kleuterkamp").remove();
    }
    
    private static void insertNewData(Document doc, Element divContainer) {
        doc.body().insertChildren(0,divContainer);
    }
}
