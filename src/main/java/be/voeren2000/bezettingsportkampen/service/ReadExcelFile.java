package be.voeren2000.bezettingsportkampen.service;

import be.voeren2000.bezettingsportkampen.model.OneDayTotal;
import be.voeren2000.bezettingsportkampen.model.RegistrationSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReadExcelFile {
    public void readFile(RegistrationSheet registrationSheet) {
        getDataFromSheet(registrationSheet);
    }
    
    private void getDataFromSheet(RegistrationSheet registrationSheet) {
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
