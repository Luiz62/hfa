package br.edu.ifg.hfa.common.dashboard.patient;

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

public class RetailerDashboardPatient extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard_patient);

        TextView textView = findViewById(R.id.textView);

        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        String name = usersDetails.get(SessionManager.KEY_NAME);
        String email = usersDetails.get(SessionManager.KEY_EMAIL);
        String phoneNumber = usersDetails.get(SessionManager.KEY_PHONENUMBER);
        String cpf = usersDetails.get(SessionManager.KEY_CPF);
        String age = usersDetails.get(SessionManager.KEY_DATE);
        String gender = usersDetails.get(SessionManager.KEY_GENDER);

        textView.setText(
                "Nome: " + name + "\n" +
                        "E-mail: " + email + "\n" +
                        "Celular: " + phoneNumber + "\n" +
                        "Cpf: " + cpf + "\n" +
                        "Data de aniversário: " + age + "\n" +
                        "Sexo: " + gender + "\n"
        );
    }

    public void logoutTheUserFromSession(View view){
        sessionManager.logoutUserFromSession();
        DbConnection.getAuth().signOut();
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}