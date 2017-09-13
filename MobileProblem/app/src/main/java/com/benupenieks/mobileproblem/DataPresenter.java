package com.benupenieks.mobileproblem;

import android.content.Context;

import static com.benupenieks.mobileproblem.SearchQuery.QueryType.Customer;
import static com.benupenieks.mobileproblem.SearchQuery.QueryType.StockItem;

/**
 * Created by Ben on 2017-09-12.
 */

public class DataPresenter implements DataContract.Presenter {

    private DataContract.View mView;
    private DataContract.Interactor mInteractor = new DataInteractor();

    public void onSearch(String firstName, String lastName) {
        mInteractor.search(new SearchQuery(Customer, firstName, lastName), this, (Context) mView);
    }

    public void onSearch(String stockItem) {
        mInteractor.search(new SearchQuery(StockItem, stockItem), this, (Context) mView);
    }

    public void onSearchResult(SearchQuery.QueryType queryType, String result) {
        switch (queryType) {
            case Customer:
                mView.updateCustomerResults(result);
                break;
            case StockItem:
                mView.updateStockResults(result);
                break;
        }
    }

    @Override
    public void attachView(DataContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
