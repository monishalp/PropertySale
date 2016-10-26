package com.example.monisha.propertymarketing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPropertyFragment.Callbacks {
    TextView usernameText, userIdText;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.view_added_property);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.framentContainer, new ViewPropertyFragment());
        ft.commit();
        getSupportActionBar().setTitle("My Properties");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        usernameText = (TextView) findViewById(R.id.username_text);
//        userIdText = (TextView) findViewById(R.id.user_id_text);
//
//        usernameText.setText("User type: " + getIntent().getStringExtra("usertype"));
//        userIdText.setText("User Id: " + getIntent().getStringExtra("userid"));

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction ft = fragmentManager.beginTransaction();

        if (id == R.id.view_added_property) {
            android.app.Fragment fragment = new ViewPropertyFragment();
            ft.replace(R.id.framentContainer, fragment);
            ft.commit();
        } else if (id == R.id.add_property) {
            android.app.Fragment fragment = new AddPropertyFragment();
            ft.replace(R.id.framentContainer, fragment);
            ft.commit();
        } else if (id == R.id.log_out) {
            sp = getSharedPreferences("rememberme", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
            editor.putString("rememberme", "false");
            editor.apply();
          //  startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void refreshProperty() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        android.app.Fragment fragment = new ViewPropertyFragment();
        ft.replace(R.id.framentContainer, fragment);
        ft.commit();
    }
}
