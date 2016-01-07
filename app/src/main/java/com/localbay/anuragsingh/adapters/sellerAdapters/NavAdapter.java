package com.localbay.anuragsingh.adapters.sellerAdapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.Seller.SellerProductListingFragment;
import com.localbay.anuragsingh.Seller.SellerShopProfileFragment;

/**
 * Created by anuragsingh on 02/01/16.
 */
public class NavAdapter extends ArrayAdapter<String> {
    Context context;
    Integer[] images;
    String[] titles;

    DrawerLayout drawerLayout;
    Fragment fragment;

    public NavAdapter(Context context, String[] titles, Integer[] images, DrawerLayout drawerLayout) {
        super(context, R.layout.navlist_item, R.id.navItemText, titles);
        this.context = context;
        this.images = images;
        this.titles = titles;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.navlist_item, parent, false);

        ImageView navIcon = (ImageView) row.findViewById(R.id.navItemIcon);
        TextView navDesc = (TextView) row.findViewById(R.id.navItemText);

        navIcon.setImageResource(images[position]);
        navDesc.setText(titles[position]);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        fragment = new SellerProductListingFragment();
                        break;
                    case 1:
                        fragment = new SellerShopProfileFragment();
                        break;
                    default:
                        fragment = new SellerProductListingFragment();
                        break;
                }

                FragmentManager fm = ((Activity) context).getFragmentManager();
                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("NavbarFragments").commit();
                drawerLayout.closeDrawers();
            }
        });


        return row;
    }
}

