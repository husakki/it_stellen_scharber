package de.husakki.itscraper;

import de.husakki.itscraper.SQLite.SQLFacade;
import de.husakki.itscraper.germantech.Germantech;
import de.husakki.itscraper.interfaces.ISQLite;

/**
 * Starte den Scharber von hier
 */
public class Main {
    public static void main(String[] args) {
        ISQLite db = new SQLFacade();
        Germantech gt = new Germantech(db);
        gt.startScraping();
    }
}
