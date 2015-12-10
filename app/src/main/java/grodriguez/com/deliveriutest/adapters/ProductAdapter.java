package grodriguez.com.deliveriutest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.utils.Constants;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ProductAdapter extends BaseAdapter {

    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Products> productList;
    OnFragmentInteractionListener onFragmentInteractionListener;
    private Context context;
    private LayoutInflater layoutInflater;

    public ProductAdapter(ArrayList<Products> productList,
                          OnFragmentInteractionListener onFragmentInteractionListener, Context context) {
        this.productList = productList;
        this.onFragmentInteractionListener = onFragmentInteractionListener;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (productList != null)
            return productList.size();
        else
            return -1;

    }

    @Override
    public Object getItem(int position) {
        if (productList != null)
            return productList.get(position);
        else
            return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (productList == null)
            return view;
        final Products products = productList.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_product_item, null);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) view.findViewById(R.id.product_name);
            viewHolder.productPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.productDescription = (TextView) view.findViewById(R.id.product_description);
            viewHolder.productImage = (ImageView) view.findViewById(R.id.product_image);
            viewHolder.shipButton = (LinearLayout) view.findViewById(R.id.ship_button);
            viewHolder.shipMessage = (TextView) view.findViewById(R.id.product_order);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (ParseUser.getCurrentUser() == null)
            viewHolder.shipButton.setVisibility(View.GONE);

        String price = Integer.toString(products.getPrice());
        viewHolder.productName.setText(products.getName());
        viewHolder.productName.setSelected(true);
        Picasso.with(context).load(products.getImage()).into(viewHolder.productImage);
        viewHolder.productDescription.setText(products.getDescription());
        viewHolder.productPrice.setText(context.getString(R.string.price, price));
        viewHolder.shipButton.setClickable(true);
        viewHolder.shipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentInteractionListener.onShippingSelected(products, Constants.PRODUCTS_ID);
            }
        });
        viewHolder.shipMessage.setSelected(true);
        return view;
    }

    static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        TextView shipMessage;
        LinearLayout shipButton;
    }

}
