package com.CVP.cv_project.resources;


import com.CVP.cv_project.utils.PersistenceDummyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;


@RestController
@RequestMapping(PopulateDummyDataResource.USERS_V_1_POPULATE)
@CrossOrigin(origins = "http://localhost:4200")
public class PopulateDummyDataResource {

    public static final String USERS_V_1_POPULATE = "users/v1/populate";

    @Autowired
    private PersistenceDummyData persistDumData;

// FOR TESTDATA ONLY
   @GetMapping( path = "{amount}",produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> populateRole(@PathVariable int amount){
        try {
            persistDumData.populate(amount);
            return new ResponseEntity<>("Congratz! You have now filled the database with " + amount + " dummies, few salesmen and one admin ;) ", HttpStatus.CREATED);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("Some objects was not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Unfortunately - this happen: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (ParseException e) {
            return new ResponseEntity<>("Couldn't parse year into date: " + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    } 
}

