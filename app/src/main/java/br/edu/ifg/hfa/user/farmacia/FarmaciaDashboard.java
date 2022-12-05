package br.edu.ifg.hfa.user.farmacia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.home.CategoriesAdapter;
import br.edu.ifg.hfa.adapter.home.CategoriesHelperClass;
import br.edu.ifg.hfa.adapter.home.FeaturedAdpater;
import br.edu.ifg.hfa.adapter.home.FeaturedHelperClass;
import br.edu.ifg.hfa.adapter.home.MostViewedAdapter;
import br.edu.ifg.hfa.adapter.home.MostViewedHelperClass;
import br.edu.ifg.hfa.common.auth.paciente.RetailerStartUpScreen;
import br.edu.ifg.hfa.common.dashboard.farmacia.RetailerDashboardFarmacia;
import br.edu.ifg.hfa.common.dashboard.paciente.RetailerDashboard;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.user.AllCategories;
import br.edu.ifg.hfa.user.paciente.UserDashboard;

public class FarmaciaDashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    ImageView menuIcon;
    LinearLayout contentView;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacia_dashboard);

        menuIcon = findViewById(R.id.menu_icon_farmacia);
        contentView = findViewById(R.id.content_farmacia);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_farmacia);

        //call navigation drawer
        navigationDrawer();
    }

    //Navigation Drawer Functions
    private void navigationDrawer() {

        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        drawerLayout.setDrawerElevation(0);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_all_categories:
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
                break;
        }

        return true;
    }

    //Normal Functions
    public void callRetailerScreens(View view) {
        SessionManager sessionManager = new SessionManager(FarmaciaDashboard.this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin())
            startActivity(new Intent(getApplicationContext(), RetailerDashboardFarmacia.class));
        else
            startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
}