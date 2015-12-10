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
import grodriguez.com.deliveriutest.adapters.ProductAdapter;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.utils.Constants;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ProductFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Products> productsList;

    public static ProductFragment newInstance(ArrayList<Products> productsList) {
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_PRODUCTS_LIST, productsList);
        productFragment.setArguments(bundle);
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsList = (ArrayList<Products>) getArguments().
                getSerializable(Constants.BUNDLE_PRODUCTS_LIST);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.menu_listview);
        View header = getActivity().getLayoutInflater().inflate(R.layout.list_view_header, null);
        ((TextView) header.findViewById(R.id.header_message)).setText(getActivity().
                getString(R.string.product_message));
        TextView product_resume_message = (TextView) header.findViewById(R.id.header_resume_message);
        product_resume_message.setText(getActivity().getString(R.string.product_resume_message));
        product_resume_message.setVisibility(View.VISIBLE);
        listView.addHeaderView(header, null, false);
        listView.setAdapter(new ProductAdapter(productsList, (OnFragmentInteractionListener) getActivity(),
                getContext()));
        Log.d(LOG_TAG, "Creating the Fragment");
        if (productsList != null)
            Log.d(LOG_TAG, "Product List " + productsList.toString());
        return view;
    }
}
