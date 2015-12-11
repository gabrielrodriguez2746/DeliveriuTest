package grodriguez.com.deliveriutest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnOrderButtonListener;
import grodriguez.com.deliveriutest.models.Products;
import grodriguez.com.deliveriutest.utils.Constants;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class OrderAdapter extends BaseAdapter {

    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Products> productList;
    OnOrderButtonListener onOrderButtonListener;
    private Context context;
    private LayoutInflater layoutInflater;

    public OrderAdapter(ArrayList<Products> productList,
                        OnOrderButtonListener onOrderButtonListener, Context context) {
        this.productList = productList;
        this.onOrderButtonListener = onOrderButtonListener;
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
        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_order_item, null);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) view.findViewById(R.id.product_name);
            viewHolder.productPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.productImage = (ImageView) view.findViewById(R.id.product_image);
            viewHolder.quantityCount = (TextView) view.findViewById(R.id.quantity);
            viewHolder.plusButton = (Button) view.findViewById(R.id.plus);
            viewHolder.minusButton = (Button) view.findViewById(R.id.minus);
            viewHolder.removeButton = (ImageView) view.findViewById(R.id.remove);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final int quantity = products.getQuantity();
        String price = Integer.toString(products.getPrice());
        viewHolder.productName.setText(products.getName());
        viewHolder.productName.setSelected(true);
        Picasso.with(context).load(products.getImage()).into(viewHolder.productImage);
        viewHolder.productPrice.setText(context.getString(R.string.price, price));
        viewHolder.quantityCount.setText(context.getString(R.string.quantity, quantity));
        viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int increment =  quantity;
                increment++;
                viewHolder.quantityCount.setText(context.getString(R.string.quantity, increment));
                onOrderButtonListener.OnOrderButtonSelected(Constants.PLUS_ID, position, increment);
            }
        });
        viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int decrement =  quantity;
                if (decrement > 1){
                    decrement--;
                }
                viewHolder.quantityCount.setText(context.getString(R.string.quantity, decrement));
                onOrderButtonListener.OnOrderButtonSelected(Constants.MINUS_ID, position, decrement);
            }
        });
        viewHolder.removeButton.setClickable(true);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderButtonListener.OnOrderButtonSelected(Constants.REMOVE_ID, position, quantity);
            }
        });
        return view;
    }

    static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityCount;
        Button minusButton;
        Button plusButton;
        ImageView removeButton;
    }

}
