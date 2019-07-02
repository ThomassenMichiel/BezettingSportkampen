package be.voeren2000.bezettingsportkampen.service;

import be.voeren2000.bezettingsportkampen.model.OneDayTotal;
import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import be.voeren2000.bezettingsportkampen.util.PropertiesLoader;
import org.jsoup.nodes.Element;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlTable {
    private PropertiesLoader propertiesLoader;
    
    public HtmlTable() {
        propertiesLoader = new PropertiesLoader();
    }
    
    public Element processRegistrations(RegistrationSheet registrationSheet) {
        int size = registrationSheet.getEntriesPerDay().size();
        Element divContainer = createTable();
        for (int i = 0; i < size; i += 5) {
            List<OneDayTotal> splitDays = registrationSheet
                    .getEntriesPerDay()
                    .subList(i, Math.min(i + 5, size));
            populateTable(splitDays, divContainer);
        }
        return divContainer;
    }
    
    private Element createTable() {
        return new Element("table");
    }
    
    public Element createH1Header(String header) {
        Element h1 = new Element("h1");
        h1.appendText(header);
        return h1;
    }
    
    private void populateTable(List<OneDayTotal> items, Element divContainer) {
        Element dateRow = new Element("tr");
        Element amountRow = new Element("tr");
        for (OneDayTotal oneDayTotal : items) {
            String dateString = oneDayTotal.getLocalDate().format(DateTimeFormatter.ofPattern("E d MMM"));
            createRow("th", dateString, dateRow);
            
            String amount = oneDayTotal.getAmount() >= 25
                    ? propertiesLoader.getString("max_registrations_reached")
                    : String.valueOf((int) oneDayTotal.getAmount() + "/" + propertiesLoader.getString("max_registrations_per_day"));
            createRow("td", amount, amountRow);
        }
        Element table = divContainer.select("table").last();
        table.appendChild(dateRow);
        table.appendChild(amountRow);
    }
    
    private void createRow(String elementType, String value, Element row) {
        Element element = new Element(elementType);
        element.appendText(value);
        row.appendChild(element);
    }
}
