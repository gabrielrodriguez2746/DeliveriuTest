package grodriguez.com.deliveriutest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnFragmentInteractionListener;
import grodriguez.com.deliveriutest.models.Categories;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class CategoriesAdapter extends BaseAdapter {

    private final String LOG_TAG = getClass().getSimpleName();
    private ArrayList<Categories> categoriesList;
    OnFragmentInteractionListener onFragmentInteractionListener;
    private Context context;
    private LayoutInflater layoutInflater;

    public CategoriesAdapter(ArrayList<Categories> categoriesList,
                             OnFragmentInteractionListener onFragmentInteractionListener, Context context) {
        this.categoriesList = categoriesList;
        this.onFragmentInteractionListener = onFragmentInteractionListener;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (categoriesList != null)
            return categoriesList.size();
        else
            return -1;

    }

    @Override
    public Object getItem(int position) {
        if (categoriesList != null)
            return categoriesList.get(position);
        else
            return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (categoriesList == null)
            return view;
        final Categories category = categoriesList.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_category_item, null);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = (TextView) view.findViewById(R.id.category_name);
            viewHolder.categoryImage = (ImageView) view.findViewById(R.id.category_image);
            viewHolder.itemList = view.findViewById(R.id.item_list_category);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.categoryName.setText(category.getName());
        viewHolder.itemList.setClickable(true);
        viewHolder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentInteractionListener.onItemSelected(view, category.getId());
            }
        });
        return view;
    }

    static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        View itemList;
    }
}
