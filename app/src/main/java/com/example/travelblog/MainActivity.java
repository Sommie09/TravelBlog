package com.example.travelblog;

import android.content.Intent;
import android.os.Bundle;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.*;

import com.google.android.material.appbar.*;
import com.google.android.material.dialog.*;
import com.google.android.material.snackbar.*;
import com.travelblog.adapter.*;
import com.travelblog.http.*;
import com.travelblog.repository.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private BlogRepository repository;

    private static final int SORT_TITLE = 0;
    private static final int SORT_DATE = 1;
    private int currentSort = SORT_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new BlogRepository(getApplicationContext());

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked();
            }
            return false;
        });



        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search); //Bind search menu item
        SearchView searchView = (SearchView) searchItem.getActionView(); // Reference the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });


        adapter = new MainAdapter(blog ->
                BlogDetailsActivity.startBlogDetailsActivity(this, blog));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this::loadDataFromNetwork);

        loadDataFromDatabase();
        loadDataFromNetwork();

    }

    private void onSortClicked(){
        String[] items = {"Title", "Date"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Sort order")
                .setSingleChoiceItems(items, currentSort, (dialog, which) -> {
                    dialog.dismiss();
                    currentSort = which;
                    sortData();
                }).show();
    }

    private void sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle();
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate();
        }
    }

    private void loadDataFromDatabase() {
        repository.loadDataFromDatabase(new DataFromDatabaseCallback() {
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> {
                    adapter.setData(blogList);
                    sortData();
                });
            }
            @Override
            public void onError() {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    showErrorSnackbar();
                });
            }

        });
    }


        private void loadDataFromNetwork() {
        refreshLayout.setRefreshing(true);

        repository.loadDataFromNetwork(new DataFromNetworkCallback() {
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> {
                    adapter.setData(blogList);
                    sortData();
                    refreshLayout.setRefreshing(false);
                });
            }


            @Override
            public void onError() {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    showErrorSnackbar();
                });
            }
        });
    }


    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.setAction("Retry", v -> {
            loadDataFromNetwork();
            snackbar.dismiss();
        });
        snackbar.show();
    }




}

