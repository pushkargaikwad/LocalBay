package com.localbay.anuragsingh.adapters.sellerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.models.CatalogModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsingh on 03/01/16.
 */
public class ProductSearchAdapter extends BaseAdapter implements Filterable {

    private List<CatalogModel> originalData = null;
    private List<CatalogModel> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    private Context context;

    private ImageView productIcon;
    private TextView productTitle;
    private TextView productCategory;

    public ProductSearchAdapter(Context context, List<CatalogModel> data) {
        this.originalData = data;
        this.filteredData = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = mInflater.inflate(R.layout.seller_product_search_item, null);
        productIcon = (ImageView) view.findViewById(R.id.productImage);
        productTitle = (TextView) view.findViewById(R.id.productTitle);
        productCategory = (TextView) view.findViewById(R.id.productCategory);

        String productUrl = filteredData.get(position).getImageUrl();

        Ion.with(productIcon)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        productTitle.setText(filteredData.get(position).getTitle());
        productCategory.setText(filteredData.get(position).getCategory());


        return view;
    }

    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            final List<CatalogModel> list = originalData;

            if (constraint != null && constraint != "") {

                String filterString = constraint.toString().toLowerCase();

                int count = list.size();
                final ArrayList<CatalogModel> nlist = new ArrayList<CatalogModel>(count);
                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getTitle();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            } else {
                results.values = list;
                results.count = list.size();
                return results;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<CatalogModel>) results.values;
            notifyDataSetChanged();
        }
    }
}

