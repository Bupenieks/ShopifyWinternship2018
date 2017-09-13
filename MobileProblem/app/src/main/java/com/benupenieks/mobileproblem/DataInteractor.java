package com.benupenieks.mobileproblem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

/**
 * Created by Ben on 2017-09-12.
 */

public class DataInteractor implements DataContract.Interactor {

    private final String TAG = "DataInteractor";
    private final String mBaseRequestUrl = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    private final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "Could not retrieve API data.");
        }
    };

    private RequestQueue mRequestQueue = null;

    public void search(final SearchQuery query, final DataContract.Presenter listener, Context context) {
        if (mRequestQueue == null) mRequestQueue = Volley.newRequestQueue(context);

        switch (query.getQueryType()) {
            case Customer:
                final String customerRequestUrl = mBaseRequestUrl + "&fields=customer";
                final Response.Listener<JSONObject> customerResponseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "JSON Response Received");
                        JSONArray orders;
                        JSONObject customer;
                        Double amountSpent = 0.0;
                        try {
                            orders = response.getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                customer = orders.getJSONObject(i);
                                if (customer.has("customer")) {
                                    customer = customer.getJSONObject("customer");
                                    if (customer.getString("first_name").equals(query.getFirstName()) && customer.getString("last_name").equals(query.getLastName())) {
                                        amountSpent += customer.getDouble("total_spent");
                                    }
                                } else {
                                    continue;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Rounds decimals
                        amountSpent = (double) Math.round(amountSpent * 100) / 100;
                        listener.onSearchResult(query.getQueryType(), query.getFirstName() + " " + query.getLastName() + " spent $" + Double.toString(amountSpent));
                    }
                };

                mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, customerRequestUrl, null,
                        customerResponseListener, mErrorListener));

                break;
            case StockItem:
                final String stockItemRequestUrl = mBaseRequestUrl + "&fields=line_items";
                final Response.Listener<JSONObject> itemResponseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "JSON Response Received");
                        JSONArray orders, lineItems;
                        int numPurchased = 0;
                        try {
                            orders = response.getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject ithLineItemObject = orders.getJSONObject(i);
                                if (ithLineItemObject.has("line_items")) {
                                    lineItems = ithLineItemObject.getJSONArray("line_items");
                                } else {
                                    continue;
                                }
                                for (int j = 0; j < lineItems.length(); j++) {
                                    JSONObject lineItem = lineItems.getJSONObject(j);
                                    if (lineItem.getString("title").equals(query.getStockItem())) {
                                        numPurchased += lineItem.getInt("quantity");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listener.onSearchResult(query.getQueryType(), "You sold " + Integer.toString(numPurchased) + " of " + query.getStockItem());
                    }
                };

                mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, stockItemRequestUrl, null,
                        itemResponseListener, mErrorListener));
                break;
            default:
                Log.d(TAG, "Invalid search query received.");
        }



    }
}



