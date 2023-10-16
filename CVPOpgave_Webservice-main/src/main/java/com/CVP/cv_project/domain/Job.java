package com.CVP.cv_project.domain;

import com.CVP.cv_project.repos.CategoryRepository;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id", nullable = false)
    private Integer id;
    private String title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "fk_cv_id")
    private CV cv;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_company_id")
    private Company company;
    //@OneToOne(cascade = {CascadeType.ALL},orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "job")
    //private JobTitle jobTitle;

    public Job() {
    }

    public Job(String title, Date startDate, Date endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    //public JobTitle getJobTitle() {
    //    return jobTitle;
    //}
//
    //public void setJobTitle(JobTitle jobTitle) {
    //    this.jobTitle = jobTitle;
    //}

    //public Integer getId() {
    //    return id;
    //}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}