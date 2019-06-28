package be.voeren2000.bezettingsportkampen.model;

import java.time.LocalDate;

public class OneDayTotal {
    private LocalDate localDate;
    private double amount;
    
    public OneDayTotal(LocalDate localDate, double amount) {
        this.localDate = localDate;
        this.amount = amount;
    }
    
    public LocalDate getLocalDate() {
        return localDate;
    }
    
    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
