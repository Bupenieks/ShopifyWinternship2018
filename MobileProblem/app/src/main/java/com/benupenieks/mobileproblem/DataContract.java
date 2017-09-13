package com.benupenieks.mobileproblem;

import android.content.Context;

/**
 * Created by Ben on 2017-09-12.
 */

public interface DataContract {

    interface View {
        void search();

        void updateQueryResults(String result);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void onSearch(String firstName, String lastName);

        void onSearch(String stockItem);

        void onSearchResult(String result);
    }

    interface Interactor{
        void search(SearchQuery query, DataContract.Presenter listener, Context context);
    }
}
