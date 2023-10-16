package com.CVP.cv_project.domain;

import java.util.ArrayList;

public class LinkedInDataPacket {

    private String name;
    private String about;
    private ArrayList jobs;
    private ArrayList educations;
    private ArrayList certificates;

   public LinkedInDataPacket(String name, String about, ArrayList jobs, ArrayList educations, ArrayList certificates) {
        this.name = name;
        this.about = about;
        this.jobs = jobs;
        this.educations = educations;
        this.certificates = certificates;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public ArrayList getJobs() {
        return jobs;
    }

    public ArrayList getEducations() {
        return educations;
    }

    public ArrayList getCertificates() {
        return certificates;
    }
}
