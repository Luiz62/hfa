package br.edu.ifg.hfa.common.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.dashboard.RetailerDashboard;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.db.UserHelperClass;
import br.edu.ifg.hfa.user.UserDashboard;

public class VerifyOTP extends AppCompatActivity {

    PinView pinFromUser;
    String codeBySystem;
    TextView otpDescriptionText;
    String fullName, phoneNo, email, cpf, password, date, gender, whatToDO;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        pinFromUser = findViewById(R.id.pin_view);
        otpDescriptionText = findViewById(R.id.otp_description_text);

        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        cpf = getIntent().getStringExtra("cpf");
        password = getIntent().getStringExtra("password");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");
        codeBySystem = getIntent().getStringExtra("codeBySystem");
        whatToDO = getIntent().getStringExtra("whatToDO");

        otpDescriptionText.setText(otpDescriptionText.getText().toString() + phoneNo);
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
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    if (whatToDO.equals("updateData"))
                                        updateOldUsersData();
                                    else if (whatToDO.equals("createNewUser"))
                                        storeNewUsersData();

                                    Toast.makeText(VerifyOTP.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VerifyOTP.this, UserDashboard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(VerifyOTP.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                }
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

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        //Create helperclass reference and store data using firebase
        UserHelperClass addNewUser = new UserHelperClass(fullName, cpf, email, password, phoneNo, date, gender);
        reference.child(phoneNo).setValue(addNewUser);

        //Create a Session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        sessionManager.createLoginSession(fullName, cpf, email, password, phoneNo, date, gender);

        startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));
        finish();
    }



    /*
      First check the call and then redirect
      user accordingly to the Profile
      or to Set New Password Screen
     */
    public void callNextScreenFromOTP(View view) {

        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }

    }

    public void goToHomeFromOTP(View view) {
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        finish();
    }
}