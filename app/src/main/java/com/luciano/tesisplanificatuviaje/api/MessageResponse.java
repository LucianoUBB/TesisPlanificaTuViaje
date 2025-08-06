package com.luciano.tesisplanificatuviaje.api;

public class MessageResponse {

    private QueryResult queryResult;

    public String getFulfillmentText() {
        return queryResult != null ? queryResult.fulfillmentText : "";
    }

    public static class QueryResult {
        public String fulfillmentText;
    }
}

