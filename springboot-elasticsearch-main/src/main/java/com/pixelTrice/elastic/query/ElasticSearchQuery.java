package com.pixelTrice.elastic.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pixelTrice.elastic.model.Issue;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "issues";


    public String createOrUpdateDocument(Issue issue) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(issue.getId())
                .document(issue)
        );
        if (response.result().name().equals("Created")) {
            return new StringBuilder("Document has been successfully created.").toString();
        } else if (response.result().name().equals("Updated")) {
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public Issue getDocumentById(String issueId) throws IOException {
        Issue issue = null;
        GetResponse<Issue> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(issueId),
                Issue.class
        );

        if (response.found()) {
        	issue = response.source();
            System.out.println("Issue name " + issue.getName());
        } else {
            System.out.println("Issue not found");
        }

        return issue;
    }

    public String deleteDocumentById(String issueId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(issueId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Product with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Issue not found");
        return new StringBuilder("Issue with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public List<Issue> searchAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Issue.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Issue> issues = new ArrayList<>();
        for (Hit object : hits) {

            System.out.print(((Issue) object.source()));
            issues.add((Issue) object.source());

        }
        return issues;
    }
}
