package com.CVP.cv_project.handlers;

import com.CVP.cv_project.domain.KnowCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:testApplication.properties")
class LinkedInScraperTest {

    @Test
    void testScraping() throws MalformedURLException, ParseException {
        //Arrange
        String filePath = "./src/test/resources/testfiles/";
        String fileName = "test-site.html";
        //Act
        LinkedInScraper scraper = new LinkedInScraper(filePath + fileName);
        try {
            System.out.println(scraper.Scrape().getName());
            System.out.println(scraper.Scrape().getAbout());
            System.out.println(scraper.Scrape().getJobs());
            System.out.println(scraper.Scrape().getEducations());
            System.out.println(scraper.Scrape().getCertificates());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Assert
    }

    @Test
    void testScraping2() throws MalformedURLException, ParseException {
        //Arrange
        String filePath = "./src/test/resources/testfiles/";
        String fileName = "test-site2.html";
        //Act
        LinkedInScraper scraper = new LinkedInScraper(filePath + fileName);
        try {
            System.out.println(scraper.Scrape().getName());
            System.out.println(scraper.Scrape().getAbout());
            System.out.println(scraper.Scrape().getJobs());
            System.out.println(scraper.Scrape().getEducations());
            System.out.println(scraper.Scrape().getCertificates());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Assert
    }
}