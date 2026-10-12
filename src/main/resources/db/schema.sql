CREATE TABLE IF NOT EXISTS jobs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    company_name TEXT,
    job_title TEXT NOT NULL,
    job_url TEXT NOT NULL UNIQUE,
    city TEXT,
    is_remote INTEGER,
    created_at TEXT DEFAULT (CURRENT_DATE)
);

