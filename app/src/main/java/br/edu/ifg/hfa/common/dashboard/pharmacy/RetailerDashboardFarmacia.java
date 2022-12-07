package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.SessionManager;

public class RetailerDashboardFarmacia extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard_pharmacy);

        TextView textView = findViewById(R.id.textViewFarmacia);

        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        String name = usersDetails.get(SessionManager.KEY_NAME);
        String email = usersDetails.get(SessionManager.KEY_EMAIL);
        String password = usersDetails.get(SessionManager.KEY_PASSWORD);
        String cnpj = usersDetails.get(SessionManager.KEY_CNPJ);

        textView.setText(
                "Name: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Password: " + password + "\n" +
                        "Cnpj: " + cnpj + "\n"
        );
    }

    public void logoutTheUserFromSession(View view){
        sessionManager.logoutUserFromSession();
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}