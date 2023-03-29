package com.pixelTrice.elastic.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pixelTrice.elastic.model.Issue;
import com.pixelTrice.elastic.query.ElasticSearchQuery;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @PostMapping("/createOrUpdateDocument")
    public ResponseEntity<Object> createOrUpdateDocument(@RequestBody Issue issue) throws IOException {
          String response = elasticSearchQuery.createOrUpdateDocument(issue);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String issueId) throws IOException {
       Issue issue =  elasticSearchQuery.getDocumentById(issueId);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteDocumentById(@RequestParam String issueId) throws IOException {
        String response =  elasticSearchQuery.deleteDocumentById(issueId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Issue> issues = elasticSearchQuery.searchAllDocuments();
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }
}
