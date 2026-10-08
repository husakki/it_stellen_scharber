package de.husakki.itscraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://germantechjobs.de/");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Filteroptionen")).click();
            page.locator("[data-test=\"modal-body\"]").getByText("Remote").click();

            // ! Hardcoded by PW Codegen
            page.getByText("18 Stellen").click();

            Locator allJobs = page.locator("#job-postings-inner-wrapper");

            System.out.println(allJobs);
            // System.out.println("All Jobs:");

            // for (Locator jobs : allJobs.all()) {
            // jobs.allTextContents().forEach(System.out::println);
            // System.out.println("--------------------");
            // }

            page.waitForTimeout(50000);
            // TODO: Dont forget to close the browser
            // browser.close();
        }
    }

}