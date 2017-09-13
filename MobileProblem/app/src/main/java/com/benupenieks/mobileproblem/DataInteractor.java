package com.benupenieks.mobileproblem;

import android.content.Context;
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
    private final String mRequestUrl = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    private final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "Could not retrieve API data.");
        }
    };

    private RequestQueue mRequestQueue = null;

    public void search(SearchQuery query, final DataContract.Presenter listener, Context context) {
        if (mRequestQueue == null) mRequestQueue = Volley.newRequestQueue(context);
        switch (query.getQueryType()) {
            case Customer:
                Response.Listener<JSONObject> customerResponseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "JSON Response Received");
                        listener.onSearchResult("CURSTOMER");
                        /*try {
                            JSONArray items = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                };
                mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, mRequestUrl, null,
                        customerResponseListener, mErrorListener));
                break;
            case StockItem:
                Response.Listener<JSONObject> stockResponseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "JSON Response Received");
                        listener.onSearchResult("CURSTOMER");
                        /*try {
                            JSONArray items = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                };
                mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, mRequestUrl, null,
                        stockResponseListener, mErrorListener));
                break;
            default:
                Log.d(TAG, "Invalid search query received.");
        }
    }
}



