package be.voeren2000.bezettingsportkampen.model;

import java.util.*;

import org.apache.poi.ss.usermodel.*;

public class RegistrationSheet {
    private Workbook workbook;
    private List<OneDayTotal> entriesPerDay;
    
    public RegistrationSheet(Workbook workbook) {
        this.workbook = workbook;
        this.entriesPerDay = new ArrayList<>();
    }
    
    public Workbook getWorkbook() {
        return workbook;
    }
    
    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
    
    public List<OneDayTotal> getEntriesPerDay() {
        return entriesPerDay;
    }
    
    public void setEntriesPerDay(List<OneDayTotal> entriesPerDay) {
        this.entriesPerDay = entriesPerDay;
    }
}
