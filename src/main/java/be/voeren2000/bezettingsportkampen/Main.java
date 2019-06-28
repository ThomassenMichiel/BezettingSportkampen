package be.voeren2000.bezettingsportkampen;

import be.voeren2000.bezettingsportkampen.service.ReadExcelFile;

public class Main {
    public static void main(String[] args) {
        ReadExcelFile file = new ReadExcelFile();
        file.readFile();
    }
}
