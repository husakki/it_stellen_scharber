package de.husakki.itscraper.SQLite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    public void writeScrapToDB(HashSet<Job> jobSet) {
        String sql = "INSERT OR IGNORE into jobs(company_name, job_title, job_url, city, is_remote, created_at) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement prepStatement = conn.prepareStatement(sql);) {
            for (Job job : jobSet) {
                prepStatement.setString(1, job.getCompanyName());
                prepStatement.setString(2, job.getJobTitle());
                prepStatement.setString(3, job.getJobURL());
                prepStatement.setString(4, job.getCity());
                prepStatement.setBoolean(5, job.isRemote());
                prepStatement.setString(6, java.time.LocalDate.now().toString());
                prepStatement.addBatch();
            }

            prepStatement.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
