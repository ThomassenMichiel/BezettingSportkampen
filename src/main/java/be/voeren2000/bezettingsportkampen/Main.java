package be.voeren2000.bezettingsportkampen;

import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import be.voeren2000.bezettingsportkampen.service.ReadExcelFile;
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
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Workbook workbook = WorkbookFactory.create(new File("Lijst inschr. sportkamp ZOMER 2019.xlsx"));
            RegistrationSheet registrationSheet = new RegistrationSheet(workbook);
            
            ReadExcelFile file = new ReadExcelFile();
            file.readFile(registrationSheet);
            
            JtwigTemplate template = JtwigTemplate.classpathTemplate("table.twig");
            JtwigModel model = JtwigModel.newModel()
                    .with("splitTotals", registrationSheet.getSplitEntries())
                    .with("maxAllowed", RegistrationSheet.MAX_USERS);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            template.render(model, baos);
            Page page = getPage(baos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static Page getPage(String s) {
        String baseUrl = null;
        String username = null;
        String password = null;
        boolean debug = false;
    
        Wordpress wordpress = ClientFactory.fromConfig(ClientConfig.of(baseUrl, username, password, false, debug));
        try {
            Page page = wordpress.getPage(350l);
            Content content = page.getContent();
            Document doc = Jsoup.parse(content.getRendered());
            Element table = doc.getElementById("omnisport");
            table.remove();
            
            doc.select("body").first().insertChildren(0)
            
            return page;
        } catch (PageNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
