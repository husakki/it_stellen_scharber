package de.husakki.itscraper.DTO;

/**
 * Job DTO for scraped jobs
 */
public class Job {
    final String companyName;
    final String jobTitle;
    final String jobURL;
    final String city;
    final boolean isRemote;

    public Job(String companyName, String jobTitle, String jobURL, String city, boolean isRemote) {
        if (jobURL == null)
            throw new IllegalArgumentException("jobURL cannot be null");

        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.jobURL = jobURL;
        this.city = city;
        this.isRemote = isRemote;
    }

    @Override
    public String toString() {
        return "Job [companyName=" + companyName + ", jobTitle=" + jobTitle + ", jobURL=" + jobURL + ", city=" + city
                + ", isRemote=" + isRemote + "]";
    }

    // can use jobURL only because it should be unique as the name implies.
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Job job = (Job) o;
        return jobURL.equals(job.jobURL);
    }

    @Override
    public int hashCode() {
        return jobURL.hashCode();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobURL() {
        return jobURL;
    }

    public String getCity() {
        return city;
    }

    public boolean isRemote() {
        return isRemote;
    }

}
