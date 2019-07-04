package be.voeren2000.bezettingsportkampen;

import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import be.voeren2000.bezettingsportkampen.service.ExcelFileService;
import be.voeren2000.bezettingsportkampen.service.HtmlTableService;
import be.voeren2000.bezettingsportkampen.service.WordpressService;
import be.voeren2000.bezettingsportkampen.util.PropertiesLoader;
import com.afrozaar.wordpress.wpapi.v2.model.Page;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        args = new String[2];
        args[0] = "Lijst inschr. sportkamp ZOMER 2019.xlsx";
        args[1] = "Lijst inschr. kleuters ZOMER 2019.xlsx";
        
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        
        Element divContainer = new Element("div");
        divContainer.attr("id", propertiesLoader.getString("html_container_id"));
        for (int i = 0; i < args.length; i++) {
            try (Workbook workbook = WorkbookFactory.create(new File(args[i]))) {
                RegistrationSheet registrationSheet = new RegistrationSheet(workbook);
                ExcelFileService file = new ExcelFileService();
                file.readFile(registrationSheet);
                HtmlTableService htmlTableService = new HtmlTableService();
                String headerName = args[i].split(" ")[2];
                headerName = headerName.substring(0, 1).toUpperCase() + headerName.substring(1).toLowerCase();
                divContainer.appendChild(htmlTableService.createH1Header(headerName));
                divContainer.appendChild(htmlTableService.processRegistrations(registrationSheet));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        WordpressService wordpressService = new WordpressService();
        Page page = wordpressService.getPage();
        Document document = wordpressService.toDocument(page.getContent().getRendered());
        wordpressService.removeOldData(document);
        wordpressService.insertNewContent(document,divContainer);
        wordpressService.setPageContent(page,document);
        wordpressService.updatePageOnAPI(page);
    }
    
}
