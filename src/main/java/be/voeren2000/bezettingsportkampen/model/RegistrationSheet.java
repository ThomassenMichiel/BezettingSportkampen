package be.voeren2000.bezettingsportkampen.model;

import java.util.*;

import org.apache.poi.ss.usermodel.*;

public class RegistrationSheet {
    public static final int MAX_USERS = 25;
    private Workbook workbook;
    private List<OneDayTotal> entriesPerDay;
    private List<List<OneDayTotal>> splitEntries;
    
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
    
    public List<List<OneDayTotal>> getSplitEntries() {
        return splitEntries;
    }
    
    public void setSplitEntries(List<List<OneDayTotal>> splitEntries) {
        this.splitEntries = splitEntries;
    }
}
