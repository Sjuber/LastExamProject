package com.CVP.cv_project.handlers;

import com.CVP.cv_project.domain.LinkedInDataPacket;
import com.CVP.cv_project.dtos.CompanyDTO;
import com.CVP.cv_project.dtos.CoursesCertificationDTO;
import com.CVP.cv_project.dtos.EducationDTO;
import com.CVP.cv_project.dtos.JobDTO;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class LinkedInScraper {

    private final String linkedInURL;

    LinkedInScraper(String linkedInURL) {
        this.linkedInURL = linkedInURL;
    }

    public LinkedInDataPacket Scrape() throws IOException, ParseException {
        // GetSessionKey();
        return GetDataFromURL();
    }

    private LinkedInDataPacket GetDataFromURL() throws IOException, ParseException {
        //Document doc = Jsoup.connect(linkedInURL).get();
        Document doc = Jsoup.parse(Files.readString(Path.of(linkedInURL)));

        //Finding individual elements
        Elements nameElements = doc.select("h1");
        Elements aboutElements = doc.select("#about + div + div > div > div > div > span.visually-hidden");
        Elements experienceElements = doc.select("#experience + div + div > ul > li");
        Elements educationElements = doc.select("#education + div + div > ul > li");
        Elements licenseElements = doc.select("#licenses_and_certifications + div + div > ul > li");

        //TODO Could use SimpleDTO instead
        //Creating dataPacket to transport data

        return new LinkedInDataPacket(
                nameElements.text(),
                aboutElements.text(),
                FetchJobsFromElements(experienceElements),
                FetchEducationsFromElements(educationElements),
                FetchCertificatesFromElements(licenseElements)
        );
    }

    private ArrayList<JobDTO> FetchJobsFromElements(Elements experienceElements) throws ParseException {
        //Filter data to fit data structure
        ArrayList<JobDTO> jobs = new ArrayList<>();
        for (Element experienceElement : experienceElements) {

            //Test for format of job before we begin
            String pattern = "[12][0-9]{3}"; //regex pattern to test for valid year between 1000-2999
            String entryForTest = experienceElement.select(".visually-hidden").get(2).text(); //Testing 3rd line on job format.

            if (Pattern.compile(pattern).matcher(entryForTest).find()) {
                /*
                If test is SUCCESSFUL, parse in order:
                Index[0] Job title
                Index[1] Company name
                Index[2] Start year & end year
                 */

                JobDTO job = new JobDTO();

                //Fetching job title
                job.setTitle(experienceElement.select(".visually-hidden").get(0).text());

                //Fetching company name and splitting using regex to only get desireble data
                CompanyDTO company = new CompanyDTO();
                company.setName(experienceElement.select(".visually-hidden").get(1).text().split("·")[0]);
                job.setCompany(company);

                //Fetching start and ending year
                String str = experienceElement.select(".visually-hidden").get(2).text();
                job.setStartDate(SeparateDatesFromString(str).get(0));
                job.setEndDate(SeparateDatesFromString(str).get(1));

                //Adds to the list of jobs
                jobs.add(job);

            } else {
                /*
                If test FAILED, parse in order:
                Index[0] Job title
                Index[1] Company name
                Index[2] Start year & end year
                (This happens if the LinkedIn profile has multiple different titles in a row, at the same company)
                 */

                //Fetching company name
                String companyName = experienceElement.select(".visually-hidden").get(0).text();

                Elements titleElements = experienceElement.select("ul > li");

                //Running through multiple job-titles
                for (Element titleElement : titleElements) {
                    JobDTO job = new JobDTO();
                    CompanyDTO company = new CompanyDTO();

                    //Setting company name
                    company.setName(companyName);
                    job.setCompany(company);

                    //Fetching job title
                    job.setTitle(titleElement.select(".visually-hidden").get(0).text());


                    //Fetching start and ending year
                    String str = titleElement.select(".visually-hidden").get(2).text();
                    job.setStartDate(SeparateDatesFromString(str).get(0)); //Getting the start year
                    job.setEndDate(SeparateDatesFromString(str).get(1)); //Getting the end year

                    //Adds to the list of jobs
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    private ArrayList<EducationDTO> FetchEducationsFromElements(Elements educationElements) throws ParseException {
        //Filter data to fit data structure
        ArrayList<EducationDTO> educations = new ArrayList<>();
        for (Element educationElement : educationElements) {
            EducationDTO edu = new EducationDTO();

            //Fetching Title & place of education
            edu.setTitle(educationElement.select(".visually-hidden").get(0).text() + ", " + educationElement.select(".visually-hidden").get(1).text());

            //Fetching start and end year
            String str = educationElement.select(".visually-hidden").get(2).text();
            edu.setStartYear(SeparateYearFromString(str).get(0));
            edu.setEndYear(SeparateYearFromString(str).get(1));

            //Adds to the list of educations
            educations.add(edu);
        }
        return educations;
    }

    private ArrayList<CoursesCertificationDTO> FetchCertificatesFromElements(Elements licenseElements) throws ParseException {
        //Filter data to fit data structure
        ArrayList<CoursesCertificationDTO> certificates = new ArrayList<>();
        for (Element licenseElement : licenseElements) {
            CoursesCertificationDTO cert = new CoursesCertificationDTO();

            //Fetching Title of certification/license
            cert.setTitle(licenseElement.select(".visually-hidden").get(0).text());

            //Fetching month & year when awarded license/certificate
            String str = licenseElement.select(".visually-hidden").get(2).text();
            cert.setEndDate(SeparateDatesFromString(str).get(0)); //Getting the end date

            //Adds to the list of certificates
            certificates.add(cert);
        }
        return certificates;
    }


    //UTIL METHODS
    //UTIL METHODS
    //UTIL METHODS

    ArrayList<Date> SeparateDatesFromString(String str) throws ParseException {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MMM", new Locale("da", "DK"));
        String[] strs = str.split("[. ·-]"); //Seperating years from string using regex

        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<String> savedSplits = new ArrayList<>();

        for (int i = 0; i < strs.length; i++) {
                if (strs[i].length() > 0 && strs[i].length() < 5) {savedSplits.add(strs[i]);}
                if (savedSplits.size() > 3) break;
        }

        while (savedSplits.size() < 4) {
            savedSplits.add(null);
        }

        try {
            String dateStr1 = savedSplits.get(1) + "-" + savedSplits.get(0) + ".";
            String dateStr2 = savedSplits.get(3) + "-" + savedSplits.get(2) + ".";

            dates.add(yearFormat.parse(dateStr1));

             if (dateStr2.contains("dag") || dateStr2.contains("null") ) { //should check for a list of exceptions in different languages.
                dates.add(null);
            }
            else {
                dates.add(yearFormat.parse(dateStr2));
            }

        } catch (IndexOutOfBoundsException e) {/*Do nothing in this case and return what's left*/}
        return dates;
    }

    ArrayList<Integer> SeparateYearFromString(String str) throws ParseException {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String[] strs = str.split("[. ·-]"); //Seperating years from string using regex

        ArrayList<Integer> years = new ArrayList<>();

        for (int i = 0; i < strs.length; i++) {
            try {
                years.add(Integer.parseInt(strs[i]));
            } catch (Exception e) {
                //Nothing happens, skip and go to next
            } finally {
                if (years.size() > 1) break;
            }
        }
        return years;
    }



    //Testcode for LinkedIn login
    private String GetSessionKey() {
        try {
            String url = "https://www.linkedin.com/uas/login?goback=&trk=hb_signin";
            Connection.Response response = Jsoup
                    .connect(url)
                    .method(Connection.Method.GET)
                    .execute();

            Document responseDocument = response.parse();
            Element loginCsrfParam = responseDocument
                    .select("input[name=loginCsrfParam]")
                    .first();

            response = Jsoup.connect("https://www.linkedin.com/uas/login-submit")
                    .cookies(response.cookies())
                    .data("loginCsrfParam", loginCsrfParam.attr("value"))
                    .data("session_key", "your_login")
                    .data("session_password", "your_pass")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .execute();

            Document document = response.parse();

            System.out.println(document.title());

            System.out.println("Welcome "
                    + document.select(".act-set-name-split-link").html());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "wat";
    }

}