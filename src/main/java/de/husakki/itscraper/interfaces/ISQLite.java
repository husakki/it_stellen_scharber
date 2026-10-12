package de.husakki.itscraper.interfaces;

import java.util.HashSet;

import de.husakki.itscraper.DTO.Job;

public interface ISQLite {
    public boolean writeScrapToDB(HashSet<Job> jobSet);
}
