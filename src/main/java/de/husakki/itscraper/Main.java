package de.husakki.itscraper;

import de.husakki.itscraper.germantech.Germantech;

/**
 * Starte den Scharber von hier
 */
public class Main {
    public static void main(String[] args) {
        Germantech gt = new Germantech();
        gt.startScraping();
    }
}
