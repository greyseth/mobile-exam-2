package com.example.ecommerceapp.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public interface ApiResponse {
    void onResponse(Optional<JSONObject> response) throws JSONException;
}
