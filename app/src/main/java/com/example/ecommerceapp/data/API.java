package com.example.ecommerceapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecommerceapp.interfaces.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class API {
    public static String apiUrl = "http://192.168.116.97:3000";

    public static void get(Context ctx, String route, ApiResponse callback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                apiUrl + route, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onResponse(Optional.of(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONObject errorObject = new JSONObject();
                        try {
                            errorObject.put("error", true);
                            errorObject.put("msg", error.getMessage());
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            callback.onResponse(Optional.of(errorObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        });

        queue.add(request);
    }

    public static void post(Context ctx, String route, JSONObject params, ApiResponse callback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl + route, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onResponse(Optional.of(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            JSONObject errorObject = new JSONObject();
                            errorObject.put("error", true);
                            errorObject.put("msg", error.getMessage());

                            callback.onResponse(Optional.of(errorObject));
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        queue.add(request);
    }
}
