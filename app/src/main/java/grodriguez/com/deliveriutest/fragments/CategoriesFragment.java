package grodriguez.com.deliveriutest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.adapters.CategoriesAdapter;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.utils.Constants;

/**
 * @author Gabriel Rodríguez.
 * @version 1.0
 */
public class CategoriesFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList;

    /**
     * Instance of Fragment
     *
     * @param categoriesList List of Parse Categories
     * @return Fragment instance
     */
    public static CategoriesFragment newInstance(ArrayList<Categories> categoriesList) {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_CATEGORIES_LIST, categoriesList);
        categoriesFragment.setArguments(bundle);
        return categoriesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesList = (ArrayList<Categories>) getArguments().
                getSerializable(Constants.BUNDLE_CATEGORIES_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.menu_listview);
        View header = getActivity().getLayoutInflater().inflate(R.layout.list_view_header, null);
        ((TextView) header.findViewById(R.id.header_message)).setText(getActivity().
                getString(R.string.category_message));
        listView.addHeaderView(header);
        listView.setAdapter(new CategoriesAdapter(categoriesList, ((OnFragmentInteractionListener) getActivity()),
                view.getContext()));
        Log.d(LOG_TAG, "Creating the Fragment");
        if (categoriesList != null)
            Log.d(LOG_TAG, "Product List " + categoriesList.toString());
        return view;
    }
}
