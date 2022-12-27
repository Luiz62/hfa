package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.SplashScreen;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.SessionManager;

public class PharmacyDashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    ImageView menuIcon;
    LinearLayout contentView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private FirebaseAuth mAuth;

    LinearLayout novoRemedio, validarReceita, verificarReceita;
    LinearLayout verificarCrm, relatorios, ajuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_dashboard);

        mAuth = FirebaseAuth.getInstance();

        menuIcon = findViewById(R.id.menu_icon_farmacia);
        contentView = findViewById(R.id.content_farmacia);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_farmacia);

        novoRemedio = findViewById(R.id.novo_remedio);
        validarReceita = findViewById(R.id.validar_receita);
        verificarReceita = findViewById(R.id.verificar_receita);
        verificarCrm = findViewById(R.id.verificar_crm);
        relatorios = findViewById(R.id.relatorios);
        ajuda = findViewById(R.id.ajuda);

        validarReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VerificarReceitaActivity.class));
            }
        });

        navigationDrawer();
    }

    private void navigationDrawer() {

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
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

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
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), PharmacyDashboard.class));
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                break;
            case R.id.nav_profile_pharmacy:
                startActivity(new Intent(getApplicationContext(), RetailerDashboardFarmacia.class));
        }

        return true;
    }

    //Normal Functions
    public void callRetailerScreens(View view) {
        SessionManager sessionManager = new SessionManager(PharmacyDashboard.this, SessionManager.SESSION_USERSESSION);
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