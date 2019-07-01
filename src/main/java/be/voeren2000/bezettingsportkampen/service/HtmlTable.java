package be.voeren2000.bezettingsportkampen.service;

import be.voeren2000.bezettingsportkampen.model.OneDayTotal;
import org.jsoup.nodes.Element;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlTable {
    public Element createTable(List<List<OneDayTotal>> items) {
        Element table = new Element("table");
        for (List<OneDayTotal> item : items) {
            Element dateRow = new Element("tr");
            Element amountRow = new Element("tr");
            for (OneDayTotal oneDayTotal : item) {
                Element th = new Element("th");
                th.appendText(oneDayTotal.getLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                dateRow.appendChild(th);
                
                Element td = new Element("td");
                td.appendText(String.valueOf(oneDayTotal.getAmount()));
                amountRow.appendChild(td);
            }
            table.appendChild(dateRow);
            table.appendChild(amountRow);
        }
        
        Element h1 = new Element("h1");
        h1.appendText("Omnisport");
        Element div = new Element("div");
        div.attr("id","omnisport");
        div.appendChild(h1);
        div.appendChild(table);
        return div;
    }
}
