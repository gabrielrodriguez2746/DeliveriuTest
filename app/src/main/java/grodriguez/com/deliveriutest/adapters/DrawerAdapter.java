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
import grodriguez.com.deliveriutest.models.DrawerItem;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class DrawerAdapter extends BaseAdapter {

    private ArrayList<DrawerItem> drawerItems;
    private Context context;
    private LayoutInflater layoutInflater;

    public DrawerAdapter(ArrayList<DrawerItem> drawerItems, Context context) {
        this.drawerItems = drawerItems;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (drawerItems == null)
            return convertView;
        DrawerItem item = drawerItems.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.menuTitle = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.menuImage = (ImageView) convertView.findViewById(R.id.drawer_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.menuTitle.setText(item.getTitle());
        viewHolder.menuImage.setImageResource(R.drawable.ic_delivery);

        return convertView;

    }

    static class ViewHolder {

        TextView menuTitle;
        ImageView menuImage;
    }

}
