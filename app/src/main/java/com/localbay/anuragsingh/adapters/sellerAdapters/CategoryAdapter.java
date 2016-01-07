package com.localbay.anuragsingh.adapters.sellerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.localbay.anuragsingh.R;

/**
 * Created by anuragsingh on 02/01/16.
 */
public class CategoryAdapter extends ArrayAdapter {

    Context context;
    String[] titles;
    String[] subtitles;
    RelativeLayout relativeLayout;

    public CategoryAdapter(Context context, String[] titles, String[] subtitles, RelativeLayout relativeLayout) {
        super(context, R.layout.seller_product_category_item, R.id.category_title, titles);

        this.context = context;
        this.titles = titles;
        this.subtitles = subtitles;
        this.relativeLayout = relativeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.seller_product_category_item, parent, false);

        TextView titles_tv = (TextView) row.findViewById(R.id.category_title);
        TextView subtitles_tv = (TextView) row.findViewById(R.id.category_subtitle);
        TextView category_icon = (TextView) row.findViewById(R.id.category_icon);

        Character category_icon_text = titles[position].charAt(0);

        titles_tv.setText(titles[position]);
        subtitles_tv.setText(subtitles[position]);
        category_icon.setText(category_icon_text.toString());

        StringBuilder color_name = new StringBuilder("color");
        color_name.append(category_icon_text);
        String colorname = color_name.toString();

        try {
            category_icon.setBackgroundColor(R.color.class.getField(colorname).getInt(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return row;
    }
}
