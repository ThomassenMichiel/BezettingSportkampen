package be.voeren2000.bezettingsportkampen.service;

import be.voeren2000.bezettingsportkampen.model.OneDayTotal;
import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ReadExcelFile {
    public void readFile() {
        try {
            Workbook workbook = WorkbookFactory.create(new File("Lijst inschr. sportkamp ZOMER 2019.xlsx"));
            RegistrationSheet registrationSheet = new RegistrationSheet(workbook);
            
            getDatesFromSheet(registrationSheet);
            
            List<List<OneDayTotal>> splitTotals = new ArrayList<>();
            int size = registrationSheet.getEntriesPerDay().size();
            for (int i = 0; i < size; i += 5) {
                splitTotals.add(registrationSheet.getEntriesPerDay().subList(i, Math.min(i + 5, size)));
            }
            
            JtwigTemplate template = JtwigTemplate.classpathTemplate("table.twig");
            JtwigModel model = JtwigModel.newModel().with("splitTotals", splitTotals);
            template.render(model, System.out);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getDatesFromSheet(RegistrationSheet registrationSheet) {
        Sheet sheet = registrationSheet.getWorkbook().getSheetAt(0);
        for (Cell cell : sheet.getRow(5)) {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Instant instant = cell.getDateCellValue().toInstant();
                LocalDate date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
                registrationSheet.getEntriesPerDay().add(new OneDayTotal(date, getTotals(registrationSheet, cell.getAddress())));
            }
        }
    }
    
    private double getTotals(RegistrationSheet registrationSheet, CellAddress address) {
        Sheet sheet = registrationSheet.getWorkbook().getSheetAt(0);
        Row row = sheet.getRow(address.getRow() - 3);
        Cell cell = row.getCell(address.getColumn());
        if (cell.getCellType() == CellType.FORMULA) {
            return cell.getNumericCellValue();
        }
        return -1d;
    }
}
