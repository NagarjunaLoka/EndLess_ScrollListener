package com.example.nagarjuna.com.endlessscrolllistener;

import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nagarjuna.com.endlessscrolllistener.adapters.ProductsAdapter;
import com.example.nagarjuna.com.endlessscrolllistener.helpers.EndlessScrollListener;
import com.example.nagarjuna.com.endlessscrolllistener.helpers.Space;
import com.example.nagarjuna.com.endlessscrolllistener.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind RecyclerView from layout to recyclerViewProducts object
        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        //Create new ProductsAdapter
        productsAdapter = new ProductsAdapter(this);
        //Create new GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        //Crete new EndlessScrollListener fo endless recyclerview loading
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!productsAdapter.loading)
                    feedData();
            }
        };
        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (productsAdapter.getItemViewType(position)) {
                    case ProductsAdapter.PRODUCT_ITEM:
                        return 1;
                    case ProductsAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        //add on on Scroll listener
        recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(productsAdapter);
        //load first page of recyclerview
        endlessScrollListener.onLoadMore(0, 0);
    }

    //Load Data from your server here
    // loading data from server will make it very large
    // that's why i created data locally
    private void feedData() {
        //show loading in recyclerview
        productsAdapter.showLoading();
        final List<Product> products = new ArrayList<>();
        int[] imageUrls = {R.drawable.shoes1, R.drawable.shoes3, R.drawable.shoes4, R.drawable.shoes22};
        String[] ProductName = {"Nike", "Adidas", "Roadster", "Chkokko"};
        String[] ProductPrice = {"₹3000", "₹5000", "₹2000", "₹999"};
        boolean[] isNew = {true, false, false, true};
        for (int i = 0; i < imageUrls.length; i++) {
            Product product = new Product(imageUrls[i],
                    ProductName[i],
                    ProductPrice[i],
                    isNew[i]);
            products.add(product);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //hide loading
                productsAdapter.hideLoading();
                //add products to recyclerview
                productsAdapter.addProducts(products);
            }
        }, 2000);

    }
}
