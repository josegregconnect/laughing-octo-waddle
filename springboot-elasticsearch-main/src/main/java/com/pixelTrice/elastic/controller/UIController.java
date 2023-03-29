package com.pixelTrice.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pixelTrice.elastic.model.Issue;
import com.pixelTrice.elastic.query.ElasticSearchQuery;

import java.io.IOException;

@Controller
public class UIController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @GetMapping("/")
    public String viewHomePage(Model model) throws IOException {
        model.addAttribute("listIssueDocuments",elasticSearchQuery.searchAllDocuments());
        return "index";
    }

    @PostMapping("/saveIssue")
    public String saveIssue(@ModelAttribute("issue") Issue issue) throws IOException {
        elasticSearchQuery.createOrUpdateDocument(issue);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {

        Issue issue = elasticSearchQuery.getDocumentById(id);
        model.addAttribute("issue", issue);
        return "updateIssueDocument";
    }
    
    @GetMapping("/showFormForPreview/{id}")
    public String showFormForPreview(@PathVariable(value = "id") String id, Model model) throws IOException {

        Issue issue = elasticSearchQuery.getDocumentById(id);
        model.addAttribute("issue", issue);
        return "issuePreview";
    }
    
    @GetMapping("/showFormForClose/{id}")
    public String showFormForClose(@PathVariable(value = "id") String id, Model model) throws IOException {

        Issue issue = elasticSearchQuery.getDocumentById(id);
        model.addAttribute("issue", issue);
        return "issueClose";
    }

    @GetMapping("/showNewIssueForm")
    public String showNewIssueForm(Model model) {
        // create model attribute to bind form data
        Issue issue = new Issue();
        model.addAttribute("issue", issue);
        return "newIssueDocument";
    }

    @GetMapping("/deleteIssue/{id}")
    public String deleteIssue(@PathVariable(value = "id") String id) throws IOException {

        this.elasticSearchQuery.deleteDocumentById(id);
        return "redirect:/";
    }
}
