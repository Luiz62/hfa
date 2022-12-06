package br.edu.ifg.hfa.common.auth.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.dashboard.patient.RetailerDashboard;
import br.edu.ifg.hfa.db.PatientHelperClass;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.user.patient.PatientDashboard;

public class VerifyOTP extends AppCompatActivity {

    private PinView pinFromUser;
    private String codeBySystem;
    public TextView otpDescriptionText;
    private String name, phoneNo, email, cpf, password, date, gender, whatToDO;

    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        pinFromUser = findViewById(R.id.pin_view);
        otpDescriptionText = findViewById(R.id.otp_description_text);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        cpf = getIntent().getStringExtra("cpf");
        password = getIntent().getStringExtra("password");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");
        codeBySystem = getIntent().getStringExtra("codeBySystem");
        whatToDO = getIntent().getStringExtra("whatToDO");

        String _otpDescriptionText = otpDescriptionText.getText().toString();
        otpDescriptionText.setText(String.format("%s %s", _otpDescriptionText, phoneNo));
    }

    private void verifyCode(String code) {
        if (code.isEmpty()) {
            Toast.makeText(VerifyOTP.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
        } else {
            if (codeBySystem != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
                FirebaseAuth
                        .getInstance()
                        .signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (whatToDO.equals("updateData"))
                                    updateOldUsersData();
                                else if (whatToDO.equals("createNewUser"))
                                    storeNewUsersData();

                                Toast.makeText(VerifyOTP.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyOTP.this, PatientDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VerifyOTP.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }


    /*
    Functions To Update
    OR Create New User
     */

    private void updateOldUsersData() {

        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();

    }

    private void storeNewUsersData() {
        if (this.mAuth.getCurrentUser() != null) {
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("users");

            PatientHelperClass addNewUser;
            addNewUser = new PatientHelperClass(name, cpf, email, password, phoneNo, date, gender);
            reference.child(Objects.requireNonNull(this.mAuth.getUid()))
                    .setValue(addNewUser);

            SessionManager sessionManager = new SessionManager(this,
                    SessionManager.SESSION_USERSESSION);
            sessionManager.createLoginSession(name, cpf, email, password, phoneNo, date, gender);

            startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));
            finish();
        } else {
            Toast.makeText(this, "Faça login antes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void callNextScreenFromOTP(View view) {
        String code = Objects.requireNonNull(pinFromUser.getText()).toString();
        if (!code.isEmpty())
            verifyCode(code);
    }

    public void goToHomeFromOTP(View view) {
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}