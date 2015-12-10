package grodriguez.com.deliveriutest.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.adapters.MenuFragmentAdapter;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.listeners.OnFindQueryParse;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.network.ParseApplication;
import grodriguez.com.deliveriutest.utils.Constants;


public class MainActivity extends FragmentActivity implements View.OnClickListener,
        OnFragmentInteractionListener, OnConfirmationDialogClickListener, OnFindQueryParse {

    private ImageView mBack; // Action Bar View
    private TextView mTitle; // Action Bar View
    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList; // Category list form Parse
    private int searchQuery; // Id Indicator for the callback
    private ProgressDialog progressDialog;
    private ViewPager viewPager; // View Pager Activity
    private MenuFragmentAdapter menuFragmentAdapter; // View Pager Adapter
    private int index;
    private int actualPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBack = (ImageView) findViewById(R.id.backbtn);
        mBack.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(getString(R.string.main_title));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        categoriesList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.pager_menu);
        menuFragmentAdapter = new MenuFragmentAdapter(getSupportFragmentManager());
        index = 0; // Necessary information for the Category List Object
        progressDialog.show();
        actualPage = 0;
        getCategories();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn: {
                onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (actualPage == 1) {
            actualPage = 0;
            mTitle.setText(getString(R.string.main_title));
            mBack.setVisibility(View.GONE);
            viewPager.setCurrentItem(actualPage);
        } else
            new ConfirmationDialog(this, getString(R.string.exit_message), this);
    }


    /**
     * Get Categories from Parse
     */
    private void getCategories() {
        searchQuery = Constants.CATEGORIES_ID;
        ParseApplication.findSimpleParse(this, Constants.TAG_CATEGORY_TABLE, null, null);
    }

    @Override
    public void onItemSelected(View view, int id, int tag) {
        switch (tag) {
            case Constants.CATEGORIES_ID: {
                mBack.setVisibility(View.VISIBLE);
                menuFragmentAdapter.setProductsList((ArrayList<Products>) categoriesList.get(id)
                        .getProducts());
                menuFragmentAdapter.notifyDataSetChanged();
                mTitle.setText(getString(R.string.product_title));
                actualPage = 1;
                viewPager.setCurrentItem(actualPage);
                break;
            }
            case Constants.PRODUCTS_ID: {
                break;
            }
        }

    }

    @Override
    public void onShippingSelected(Products products) {

    }


    /**
     * Get products from Parse base on Category id
     */
    private void getProducts() {
        searchQuery = Constants.PRODUCTS_ID;
        ParseApplication.findInnerParse(this, Constants.TAG_CATEGORY_TABLE, Constants.TAG_NAME,
                categoriesList.get(index).getName(), Constants.TAG_PRODUCTS_TABLE,
                Constants.TAG_PRODUCTS_CATEGORIES);
        progressDialog.dismiss();
        menuFragmentAdapter.setCategoriesList(categoriesList);
        menuFragmentAdapter.notifyDataSetChanged();
        viewPager.setAdapter(menuFragmentAdapter);
    }


    @Override
    public void onConfirm() {
        if (ParseUser.getCurrentUser() != null)
            ParseUser.logOut();
        finish();
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void OnParseSimpleFindResult(List<ParseObject> objects, Exception e) {
        if (e == null) {
            for (int category_item = 0; category_item < objects.size(); category_item++) {
                Categories categories = new Categories(
                        category_item,
                        objects.get(category_item).getObjectId(),
                        objects.get(category_item).getString(Constants.TAG_NAME));
                categoriesList.add(categories);
            }
            Log.d(LOG_TAG, categoriesList.toString());
            getProducts();
        } else {
            Log.d(LOG_TAG, "Error: " + e.getMessage());
        }
    }

    @Override
    public void OnParseInnerFindResult(List<ParseObject> objects, Exception e) {
        if (e == null) {
            ArrayList<Products> productsList = new ArrayList<>();
            for (int i = 0; i < objects.size(); i++) {
                Products products = new Products(
                        objects.get(i).getObjectId(),
                        objects.get(i).getString(Constants.TAG_NAME),
                        objects.get(i).getString(Constants.TAG_DESCRIPTION),
                        objects.get(i).getParseFile(Constants.TAG_IMAGE).getUrl(),
                        objects.get(i).getInt(Constants.TAG_PRICE)
                );
                productsList.add(products);
            }
            categoriesList.get(index).setProducts(productsList);
            Log.d(LOG_TAG, "Category List after Query " + index + " :: " + categoriesList.get(index));
            index++;
            if (index < categoriesList.size()) {
                getProducts();
            } else {
                index = 0;
            }
        } else {
            Log.d(LOG_TAG, "Error: " + e.getMessage());
        }
    }
}
