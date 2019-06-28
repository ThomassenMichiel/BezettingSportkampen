package be.voeren2000.bezettingsportkampen;

import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import be.voeren2000.bezettingsportkampen.service.ReadExcelFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

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
            template.render(model, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
