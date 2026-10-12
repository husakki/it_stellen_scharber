package de.husakki.itscraper.SQLite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import de.husakki.itscraper.DTO.Job;
import de.husakki.itscraper.interfaces.ISQLite;

public class SQLFacade implements ISQLite {

    private static final String DB_DIR = "data";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/jobs.db?busy_timeout=5000";
    private static final String SCHEMA_PATH = "/db/schema.sql";

    public SQLFacade() {
        initDatabase();
    }

    private void initDatabase() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (InputStream in = getClass().getResourceAsStream(SCHEMA_PATH)) {
            if (in == null) {
                throw new IllegalStateException("Schema file not found on classpath: " + SCHEMA_PATH);
            }

            String schemaSql = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            try (Connection conn = DriverManager.getConnection(DB_URL);
                    Statement stmt = conn.createStatement()) {
                for (String sql : schemaSql.split(";")) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }

    @Override
    public boolean writeScrapToDB(HashSet<Job> jobSet) {

        return false;
    }

}
