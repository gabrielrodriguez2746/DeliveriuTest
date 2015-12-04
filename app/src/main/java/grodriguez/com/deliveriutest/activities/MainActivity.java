package grodriguez.com.deliveriutest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.adapters.MenuFragmentAdapter;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.utils.Constants;


public class MainActivity extends FragmentActivity implements View.OnClickListener,
        FindCallback<ParseObject>, OnFragmentInteractionListener {

    /**
     * Variables
     */
    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList; // Category list form Parse
    private ArrayList<Products> productsList;  // Product List base on Categories
    private int searchQuery;

    /**
     * Views
     */
    private ViewPager viewPager; // View Pager Activity

    /**
     * Adapters
     */
    private MenuFragmentAdapter menuFragmentAdapter; // View Pager Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(getString(R.string.main_title));
        categoriesList = new ArrayList<>();
        productsList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.pager_menu);
        menuFragmentAdapter = new MenuFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(menuFragmentAdapter);
        getCategories();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if (e == null) {
            Log.d(LOG_TAG, "Query response " + objects.toString());
            switch (searchQuery) {

                case Constants.CATEGORIES_ID: {
                    for (int category_item = 0; category_item < objects.size(); category_item++) {
                        Log.d(LOG_TAG, objects.get(category_item).toString());
                        Categories categories = new Categories(objects.get(category_item).getObjectId(),
                                objects.get(category_item).getString(Constants.TAG_NAME));
                        categoriesList.add(categories);
                    }
                    notifyAdapterDataChange();
                    Log.d(LOG_TAG, categoriesList.toString());
                    break;
                }

                case Constants.PRODUCTS_ID: {
                    break;
                }
            }
        } else {
            Log.d("score", "Error: " + e.getMessage());
        }
    }

    /**
     * Get Categories from Parse
     */
    private void getCategories() {
        searchQuery = Constants.CATEGORIES_ID;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.TAG_CATEGORY_TABLE);
        query.findInBackground(this);
    }

    @Override
    public void onItemSelected(View view, String id) {

    }

    /**
     * Notify the ViewPager Adapter the data has change
     */
    private void notifyAdapterDataChange() {
        menuFragmentAdapter.setCategoriesList(categoriesList);
        menuFragmentAdapter.setProductsList(productsList);
        menuFragmentAdapter.setSearchQuery(searchQuery);
        menuFragmentAdapter.notifyDataSetChanged();
    }
}
