package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.db.SessionManager;

public class RetailerDashboardFarmacia extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard_pharmacy);

        TextView textView = findViewById(R.id.textViewFarmacia);

        sessionManager = new SessionManager(this, SessionManager.SESSION_PHARMACYSESSION);
        HashMap<String, String> pharmacyDetails = sessionManager.getPharmacyDetailFromSession();

        String name = pharmacyDetails.get(SessionManager.KEY_NAME);
        String email = pharmacyDetails.get(SessionManager.KEY_EMAIL);
        String cnpj = pharmacyDetails.get(SessionManager.KEY_CNPJ);

        textView.setText(
                "Nome: " + name + "\n" +
                        "E-mail: " + email + "\n" +
                        "Cnpj: " + cnpj + "\n"
        );
    }

    public void logoutTheUserFromSession(View view){
        sessionManager.logoutUserFromSession();
        DbConnection.getAuth().signOut();
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}