package br.edu.ifg.hfa.common.auth.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.dashboard.patient.RetailerDashboardPatient;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.PatientHelperClass;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.common.dashboard.patient.PatientDashboard;

public class VerifyOTP extends AppCompatActivity {

    private PinView pinFromUser;
    private String codeBySystem;
    public TextView otpDescriptionText;
    private String name, phoneNo, email, cpf, rg, date, gender, whatToDO;
    RelativeLayout progressbar;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        mAuth = FirebaseAuth.getInstance();

        pinFromUser = findViewById(R.id.pin_view);

        otpDescriptionText = findViewById(R.id.otp_description_text);
        progressbar = findViewById(R.id.login_progress_bar);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        cpf = getIntent().getStringExtra("cpf");
        rg = getIntent().getStringExtra("rg");
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
            Toast.makeText(VerifyOTP.this, "OTP is not Valid!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (codeBySystem != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
                FirebaseAuth
                        .getInstance()
                        .signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (whatToDO.equals("updateData"))
                                        updateOldUsersData();
                                    else if (whatToDO.equals("createNewUser"))
                                        storeNewUsersData();
                                    else if (whatToDO.equals("loginPatient"))
                                        loginPatient();

                                    Intent intent = new Intent(VerifyOTP.this,
                                            PatientDashboard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(VerifyOTP.this, "OTP is not Valid!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void updateOldUsersData() {

        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();

    }

    private void storeNewUsersData() {
        if (this.mAuth.getCurrentUser() != null) {
            FirebaseDatabase rootNode = DbConnection.getInstance();
            DatabaseReference reference = rootNode.getReference("users");

            PatientHelperClass addNewUser;
            addNewUser = new PatientHelperClass(name, rg, cpf, email, phoneNo, date, gender);
            reference.child(Objects.requireNonNull(this.mAuth.getUid()))
                    .setValue(addNewUser);

            SessionManager sessionManager = new SessionManager(this,
                    SessionManager.SESSION_USERSESSION);
            sessionManager.createLoginSession(name, cpf, email, phoneNo, rg, date, gender);

            Toast.makeText(VerifyOTP.this, "Conta criada com sucesso!",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), RetailerDashboardPatient.class));
            finish();
        } else {
            Toast.makeText(this, "Faça login antes!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginPatient() {

        Query checkUser = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild(Objects.requireNonNull(mAuth.getUid()));
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String _name = dataSnapshot.child(mAuth.getUid()).child("name")
                            .getValue(String.class);
                    String _cpf = dataSnapshot.child(mAuth.getUid()).child("cpf")
                            .getValue(String.class);
                    String _email = dataSnapshot.child(mAuth.getUid()).child("email")
                            .getValue(String.class);
                    String _phoneNo = dataSnapshot.child(mAuth.getUid()).child("phoneNo")
                            .getValue(String.class);
                    String _rg = dataSnapshot.child(mAuth.getUid()).child("rg")
                            .getValue(String.class);
                    String _dateOfBirth = dataSnapshot.child(mAuth.getUid()).child("date")
                            .getValue(String.class);
                    String _gender = dataSnapshot.child(mAuth.getUid()).child("gender")
                            .getValue(String.class);

                    SessionManager sessionManager = new SessionManager(VerifyOTP.this,
                            SessionManager.SESSION_USERSESSION);
                    sessionManager.createLoginSession(_name, _cpf, _email, _phoneNo, _rg,
                            _dateOfBirth, _gender);

                    progressbar.setVisibility(View.GONE);

                    Toast.makeText(VerifyOTP.this, "Sucesso no login!",
                            Toast.LENGTH_SHORT).show();


                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(VerifyOTP.this, "Falha no login!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(VerifyOTP.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
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