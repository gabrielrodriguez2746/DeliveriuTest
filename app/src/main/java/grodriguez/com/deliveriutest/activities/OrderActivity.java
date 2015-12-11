package grodriguez.com.deliveriutest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.adapters.OrderAdapter;
import grodriguez.com.deliveriutest.listeners.OnOrderButtonListener;
import grodriguez.com.deliveriutest.models.Categories;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.utils.Constants;

public class OrderActivity extends FragmentActivity implements View.OnClickListener,
        OnOrderButtonListener {

    private final String LOG_TAG = getClass().getSimpleName();
    /**
     * ActionBar View
     */
    private ImageView mBack; // Action Bar Back Button
    private TextView mTitle; // Action Bar Title
    /**
     * Activity Views
     */
    private ListView orderListView; // List of products added to the order
    /**
     * Variables
     */
    private ArrayList<Products> productsList; // Product List from Main Activity
    private ArrayList<Categories> listCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /**
         * ActionBar views
         */
        mBack = (ImageView) findViewById(R.id.backbtn);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(getString(R.string.generate_order));

        Bundle productInformation = getIntent().getExtras();

        if (productInformation != null) {
            productsList = (ArrayList<Products>) productInformation.
                    getSerializable(Constants.TAG_PRODUCTS_TABLE);
            Log.d(LOG_TAG, "Product List :: " + productsList);

            (findViewById(R.id.notification_message)).setVisibility(View.GONE);
            listCategories = (ArrayList<Categories>) productInformation.
                    getSerializable(Constants.TAG_CATEGORY_TABLE);
            Log.d(LOG_TAG, "Category List :: " + listCategories);
            // Order ListView
            orderListView = (ListView) findViewById(R.id.order_listview);
            orderListView.setVisibility(View.VISIBLE);
            // Header
            View header = getLayoutInflater().inflate(R.layout.list_view_header, null);
            ((TextView) header.findViewById(R.id.header_message)).setText(getString(R.string.header_order_message));
            TextView product_resume_message = (TextView) header.findViewById(R.id.header_resume_message);
            product_resume_message.setText(getString(R.string.header_order_resume_message));
            product_resume_message.setVisibility(View.VISIBLE);
            orderListView.addHeaderView(header, null, false);
            orderListView.setAdapter(new OrderAdapter(productsList, this, this));
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TAG_PRODUCTS_TABLE, productsList);
        bundle.putSerializable(Constants.TAG_CATEGORY_TABLE, listCategories);
        BeginActivity(MainActivity.class, bundle, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbtn:
                onBackPressed();
        }
    }

    @Override
    public void OnOrderButtonSelected(int indicator, int position, int quantity) {
        switch (indicator) {
            case Constants.MINUS_ID:
            case Constants.PLUS_ID:
                productsList.get(position).setQuantity(quantity);
                orderListView.setAdapter(new OrderAdapter(productsList, this, this));
                break;

            case Constants.REMOVE_ID:
                productsList.remove(position);
                if (productsList.isEmpty())
                    BeginActivity(MainActivity.class, null, true);
                orderListView.setAdapter(new OrderAdapter(productsList, this, this));
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
        Intent intent = new Intent(OrderActivity.this, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (finish)
            finish();
    }

}
