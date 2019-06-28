package be.voeren2000.bezettingsportkampen.model;

import java.time.LocalDate;
import java.util.*;

import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;

public class RegistrationSheet {
    public static final int MAX_USERS = 25;
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
