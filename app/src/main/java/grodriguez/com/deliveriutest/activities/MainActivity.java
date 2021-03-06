package grodriguez.com.deliveriutest.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.adapters.DrawerAdapter;
import grodriguez.com.deliveriutest.adapters.MenuFragmentAdapter;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.listeners.OnFindQueryParse;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.models.DrawerItem;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.network.ParseApplication;
import grodriguez.com.deliveriutest.utils.Constants;


public class MainActivity extends FragmentActivity implements View.OnClickListener,
        OnFragmentInteractionListener, OnConfirmationDialogClickListener, OnFindQueryParse,
        AdapterView.OnItemClickListener {

    private ImageView mBack; // Action Bar Back Button
    private TextView mTitle; // Action Bar Title
    private ImageButton mMenu; // Action Bar Menu Button
    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList; // Category list form Parse
    private int pageIndicator; // Id Indicator for the callback
    private ProgressDialog progressDialog;
    private ViewPager viewPager; // View Pager Activity
    private MenuFragmentAdapter menuFragmentAdapter; // View Pager Adapter
    private DrawerLayout mDrawerLayout; // View to show the lateral menu
    private ListView mDrawerList; // List of elements in the lateral menu
    private FrameLayout mFrameLayout; // Container of the list view
    private int index;
    private int actualPage;
    private Products selectedProduct;
    private ArrayList<Products> selectedProductsList; // Products for the order
    /**
     * Shared Preference
     */
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Preference
         */
        pref = getApplicationContext().getSharedPreferences(Constants.TAG_SHARE_PREFERENCE_NAME,
                MODE_PRIVATE);
        editor = pref.edit();
        if (pref.getString(Constants.TAG_LOGIN_STATUS, Constants.TAG_NOT_INITIALIZE).
                equals(Constants.TAG_LOGGED)) {
            setContentView(R.layout.drawer_activity);
            mMenu = (ImageButton) findViewById(R.id.drawer);
            mMenu.setVisibility(View.VISIBLE);
            mMenu.setOnClickListener(this);
            /**
             * Lateral Menu
             */
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mFrameLayout = (FrameLayout) findViewById(R.id.drawer_container);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // To Customize
            ArrayList<DrawerItem> mDrawerItems = new ArrayList<>();
            mDrawerItems.add(new DrawerItem(getString(R.string.generate_order)));
            mDrawerList.setAdapter(new DrawerAdapter(mDrawerItems, this));
            mDrawerList.setOnItemClickListener(this);
        } else {
            setContentView(R.layout.activity_main);
            mMenu = (ImageButton) findViewById(R.id.drawer);
            mMenu.setVisibility(View.GONE);
        }

        /**
         * ActionBar views
         */
        mBack = (ImageView) findViewById(R.id.backbtn);
        mBack.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(getString(R.string.main_title));


        /**
         * Progress Dialog
         */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        viewPager = (ViewPager) findViewById(R.id.pager_menu);
        menuFragmentAdapter = new MenuFragmentAdapter(getSupportFragmentManager());
        actualPage = 0;
        pageIndicator = Constants.CATEGORIES_ID;
        /**
         * Getting List of Products from other activity
         */
        if (getIntent().getExtras() != null) {
            selectedProductsList = (ArrayList<Products>) getIntent().getExtras().
                    getSerializable(Constants.TAG_PRODUCTS_TABLE);
            categoriesList = (ArrayList<Categories>) getIntent().getExtras().
                    getSerializable(Constants.TAG_CATEGORY_TABLE);

            menuFragmentAdapter.setCategoriesList(categoriesList);
            menuFragmentAdapter.notifyDataSetChanged();
            viewPager.setAdapter(menuFragmentAdapter);
        } else {
            selectedProductsList = new ArrayList<>();
            categoriesList = new ArrayList<>();
            index = 0; // Necessary information for the Category List Object
            progressDialog.show();
            getCategories();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                onBackPressed();
                break;
            case R.id.drawer:
                openDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if (actualPage == 1) {
            actualPage = 0;
            if (pref.getString(Constants.TAG_LOGIN_STATUS, Constants.TAG_NOT_INITIALIZE).
                    equals(Constants.TAG_LOGGED))
                mMenu.setVisibility(View.VISIBLE);
            pageIndicator = Constants.CATEGORIES_ID;
            mTitle.setText(getString(R.string.main_title));
            mBack.setVisibility(View.GONE);
            viewPager.setCurrentItem(actualPage);
        } else {
            String message;
            if ((pref.getString(Constants.TAG_LOGIN_STATUS, Constants.TAG_NOT_INITIALIZE).
                    equals(Constants.TAG_LOGGED)))
                message = getString(R.string.exit_message_user);
            else
                message = getString(R.string.exit_message);
            new ConfirmationDialog(this, message, this);
        }


    }


    /**
     * Get Categories from Parse
     */
    private void getCategories() {
        ParseApplication.findSimpleParse(this, Constants.TAG_CATEGORY_TABLE, null, null);
    }

    @Override
    public void onItemSelected(View view, int id, int tag) {
        pageIndicator = tag;
        switch (tag) {
            case Constants.CATEGORIES_ID: {
                mBack.setVisibility(View.VISIBLE);
                mMenu.setVisibility(View.GONE);
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
    public void onShippingSelected(Products products, int tag) {
        pageIndicator = tag;
        boolean inArray = false;
        Log.d(LOG_TAG, "Product :: " + products);
        for (Products testProduct : selectedProductsList) {
            if (testProduct.getId().equals(products.getId())) {
                inArray = true;
            }
        }
        if (!inArray) {
            selectedProduct = products;
            new ConfirmationDialog(this, getString(R.string.order_confirmation), this);
        } else {
            Toast.makeText(this, getString(R.string.order_already_added_confirmation),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Get products from Parse base on Category id
     */
    private void getProducts() {
        ParseApplication.findInnerParse(this, Constants.TAG_CATEGORY_TABLE, Constants.TAG_NAME,
                categoriesList.get(index).getName(), Constants.TAG_PRODUCTS_TABLE,
                Constants.TAG_PRODUCTS_CATEGORIES);

    }


    @Override
    public void onConfirm() {
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }
        if (pageIndicator == Constants.CATEGORIES_ID) {
            editor.clear();
            editor.commit();
            finish();
        } else {
            selectedProductsList.add(selectedProduct);
            Toast.makeText(this, getString(R.string.order_added_confirmation), Toast.LENGTH_SHORT).show();
        }
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
                        objects.get(i).getInt(Constants.TAG_PRICE),
                        1
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
                progressDialog.dismiss();
                menuFragmentAdapter.setCategoriesList(categoriesList);
                menuFragmentAdapter.notifyDataSetChanged();
                viewPager.setAdapter(menuFragmentAdapter);
            }
        } else {
            Log.d(LOG_TAG, "Error: " + e.getMessage());
        }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFrameLayout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mDrawerLayout.closeDrawers();
                if (!selectedProductsList.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TAG_PRODUCTS_TABLE, selectedProductsList);
                    bundle.putSerializable(Constants.TAG_CATEGORY_TABLE, categoriesList);
                    BeginActivity(OrderActivity.class, bundle, false);
                } else
                    BeginActivity(OrderActivity.class, null, false);
                break;
        }
    }

    /**
     * Start a new activity
     *
     * @param activity Activity lo start
     * @param bundle   Information to pass to next Activity
     */
    private void BeginActivity(Class activity, Bundle bundle, boolean finish) {
        Intent intent = new Intent(MainActivity.this, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

}
