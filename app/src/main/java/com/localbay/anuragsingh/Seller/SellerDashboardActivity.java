package com.localbay.anuragsingh.Seller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.SplashActivity;
import com.localbay.anuragsingh.adapters.sellerAdapters.NavAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SellerDashboardActivity extends AppCompatActivity {

    ListView navList;
    String[] titles;
    Integer[] list_icons = {
            R.drawable.ic_local_mall_black_18dp,
            R.drawable.ic_store_black_18dp
    };
    DrawerLayout drawerLayout;
    Fragment fragment;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        setTitle("Localbay Seller");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.shop_drawer);
        titles = getResources().getStringArray(R.array.nav_drawer_items);
        navList = (ListView) findViewById(R.id.navList);

        NavAdapter navAdapter = new NavAdapter(this, titles, list_icons, drawerLayout);
        navList.setAdapter(navAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_menu_white_18dp,
                R.string.app_name,
                R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_seller);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                getSupportActionBar().setTitle(R.string.app_seller);
                supportInvalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);

        fragment = new SellerProductListingFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("default").commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void edit_userDetails(View view) {
        setTitle("Edit Shop Details");
        fragment = new SellerEditShopProfileFragment();
        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("EditProfile").commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void logoutUser(View view) {
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    public void addProductListing(View view) {
        fragment = new SellerAddProductListingFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("addProductListing").commit();
    }

    public void setActionBarTitle(String title) {
        setTitle(title);
    }
}
