package com.midland.ynote.Utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatGPT {
    private final RequestQueue queue;
    private final String baseUrl = "https://api.openai.com/v1/engines/davinci/jobs";

    public ChatGPT(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void getResponse(String prompt, final ChatGPTResponseListener listener) {
        JSONObject request = new JSONObject();
        try {
            request.put("prompt", prompt);
            request.put("max_tokens", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, baseUrl, request,
                response -> listener.onResponse(response.toString()), error -> listener.onError(error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                String token = "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";
                String token1 = "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";
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token1);
                headers.put("User-Agent", "Mozilla/5.0 ");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public interface ChatGPTResponseListener {
        void onResponse(String response);

        void onError(String error);
    }
}
