package br.edu.ifg.hfa.common.dashboard.paciente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.paciente.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.SessionManager;

public class RetailerDashboard extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard);

        TextView textView = findViewById(R.id.textView);

        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        String email = usersDetails.get(SessionManager.KEY_EMAIL);
        String phoneNumber = usersDetails.get(SessionManager.KEY_PHONENUMBER);
        String password = usersDetails.get(SessionManager.KEY_PASSWORD);
        String cpf = usersDetails.get(SessionManager.KEY_CPF);
        String age = usersDetails.get(SessionManager.KEY_DATE);
        String gender = usersDetails.get(SessionManager.KEY_GENDER);

        textView.setText(
                "Full Name: " + fullName + "\n" +
                        "Email: " + email + "\n" +
                        "Phone Number: " + phoneNumber + "\n" +
                        "Password: " + password + "\n" +
                        "Cpf: " + cpf + "\n" +
                        "Date of Birth: " + age + "\n" +
                        "Gender: " + gender + "\n"
        );
    }

    public void logoutTheUserFromSession(View view){
        sessionManager.logoutUserFromSession();
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}