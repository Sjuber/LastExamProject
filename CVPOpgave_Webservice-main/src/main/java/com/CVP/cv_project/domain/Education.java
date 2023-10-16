package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;

@Data
@Entity
@Table(name = "educations")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id", nullable = false)
    private Integer id;
    private String title;
    private Integer startYear;
    private Integer endYear;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_cv_id")
    private CV cv;

    public Education() {
    }

    public Education(String title, int startYear, int endYear) throws ParseException {

        this.title = title;
        this.startYear = startYear;
        this.endYear = endYear;
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        //this.startYear = (Date)sdf.parse(Integer.toString(startYear));
        //this.endYear = (Date)sdf.parse(Integer.toString(endYear));
        */
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) throws ParseException {
        this.startYear = startYear;
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        this.startYear = (Date)sdf.parse(Integer.toString(startYear));
        */
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) throws ParseException {
        this.endYear = endYear;
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        this.endYear = (Date)sdf.parse(Integer.toString(endYear));
        */
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public Integer getId() {
        return id;
    }


}