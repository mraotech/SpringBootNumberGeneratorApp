package com.mahesh.numbergenerator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultListResponse {

    // If the request is bulk request, then results will be updated and if it's a single request then result is updated.
    // NON_NULL values are excluded so that user will see either results OR result in their response
    private List<String> results;
    private String result;

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
