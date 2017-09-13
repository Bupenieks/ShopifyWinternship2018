package com.benupenieks.mobileproblem;

/**
 * Created by Ben on 2017-09-12.
 */

public class SearchQuery {
    public enum QueryType {
        Customer, StockItem
    }

    private QueryType mQueryType;
    private String mFirstName, mLastName, mStockItem;

    public SearchQuery(QueryType queryType, String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;
        mQueryType = queryType;
    }

    public SearchQuery(QueryType queryType, String itemName) {
        mStockItem = itemName;
    }

    public QueryType getQueryType() {
        return mQueryType;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getStockItem() {
        return mStockItem;
    }


}
