package com.benupenieks.mobileproblem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements DataContract.View {

    private TextView nameLabel, dataLabel;
    private EditText firstNameQuery, lastNameQuery, stockQuery;

    private DataPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameLabel = (TextView) findViewById(R.id.nameLabel);
        dataLabel = (TextView) findViewById(R.id.dataLabel);

        firstNameQuery = (EditText) findViewById(R.id.editTextFirstName);
        lastNameQuery = (EditText) findViewById(R.id.editTextLastName);
        stockQuery = (EditText) findViewById(R.id.editTextItemSold);

        mPresenter = new DataPresenter();
        mPresenter.attachView(this);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    @Override
    public void search() {
        String firstName = firstNameQuery.getText().toString();
        String lastName = lastNameQuery.getText().toString();
        String stockItem = stockQuery.getText().toString();
        String empty = "";

        Log.d("ASD", firstName + lastName + stockItem);

        if ((!firstName.equals(empty) || !lastName.equals(empty)) && !stockItem.equals(empty)) {
            Toast.makeText(this, "Please only enter one query type.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!firstName.equals(empty) && !lastName.equals(empty)) {
            mPresenter.onSearch(firstName, lastName);
        } else if (!stockItem.equals(empty)) {
            mPresenter.onSearch(stockItem);
        } else {
            Toast.makeText(this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateQueryResults(String result) {
        dataLabel.setText(result);
    }

    public void attachPresenter() {
        mPresenter = (DataPresenter) getLastCustomNonConfigurationInstance();
        if (mPresenter == null) {
            mPresenter = new DataPresenter();
        }
        mPresenter.attachView(this);
    }

}
