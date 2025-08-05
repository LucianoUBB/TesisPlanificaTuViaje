package com.luciano.tesisplanificatuviaje.api;

import java.util.HashMap;
import java.util.Map;

public class MessageRequest {

    private final String queryInput;

    public MessageRequest(String mensaje) {
        this.queryInput = mensaje;
    }

    public Map<String, Object> getBody() {
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> queryInput = new HashMap<>();
        Map<String, String> text = new HashMap<>();

        text.put("text", this.queryInput);
        text.put("languageCode", "es");

        queryInput.put("text", text);
        root.put("queryInput", queryInput);

        return root;
    }
}
