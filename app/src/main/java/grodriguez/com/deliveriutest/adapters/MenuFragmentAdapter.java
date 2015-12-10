package grodriguez.com.deliveriutest.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.fragments.CategoriesFragment;
import grodriguez.com.deliveriutest.fragments.ProductFragment;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.models.Products;

/**
 * Create the views for the view pager base on Fragments
 *
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class MenuFragmentAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;
    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList;
    private ArrayList<Products> productsList;


    public void setCategoriesList(ArrayList<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public void setProductsList(ArrayList<Products> productsList) {
        this.productsList = productsList;
    }

    public MenuFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d(LOG_TAG, "Category List :: " + categoriesList);
                fragment = CategoriesFragment.newInstance(categoriesList);
                break;
            case 1:
                Log.d(LOG_TAG, "Product List :: " + productsList);
                fragment = ProductFragment.newInstance(productsList);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
