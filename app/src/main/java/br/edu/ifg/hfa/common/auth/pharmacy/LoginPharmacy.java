package br.edu.ifg.hfa.common.auth.pharmacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.patient.ForgetPassword;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.PharmacyHelperClass;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.user.patient.PatientDashboard;
import br.edu.ifg.hfa.user.pharmacy.PharmacyDashboard;
import br.edu.ifg.hfa.utils.CheckInternet;

public class LoginPharmacy extends AppCompatActivity {
    private TextInputLayout email, password;
    private RelativeLayout progressbar;
    private CheckBox rememberMe;
    private TextInputEditText phoneNumberEditTextFarmacia, passwordEditTextFarmacia;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;

    private String _email, _password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_pharmacy);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();

        email = findViewById(R.id.login_email_farmacia);
        password = findViewById(R.id.login_password_farmacia);
        progressbar = findViewById(R.id.login_progress_bar_farmacia);
        rememberMe = findViewById(R.id.remember_me_farmacia);
        phoneNumberEditTextFarmacia = findViewById(R.id.login_email_number_editText_farmacia);
        passwordEditTextFarmacia = findViewById(R.id.login_password_editText_farmacia);
    }

    /*
    Login the
    user in
    app!
     */
    public void letTheUserLoggedIn(View view) {
        loadFields();

        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateFields()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        if (rememberMe.isChecked()) {
            SessionManager sessionManager = new SessionManager(LoginPharmacy.this,
                    SessionManager.SESSION_REMEMMBERME);
            sessionManager.createRememberMeSession(_email, _password);
        }

        mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    successfulLogin();
            }
        });
    }

    private void successfulLogin() {

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = rootNode.getReference("pharmacies");

            String _name = reference.child(Objects.requireNonNull(mAuth.getUid())).child("name")
                    .getKey();
            String _cnpjPharmacy = reference.child(mAuth.getUid()).child("cnpj")
                    .getKey();

            SessionManager sessionManager = new SessionManager(this,
                    SessionManager.SESSION_USERSESSION);
            sessionManager.createLoginSession(_name, mAuth.getCurrentUser().getEmail(),
                    _cnpjPharmacy);

            startActivity(new Intent(getApplicationContext(), PharmacyDashboard.class));
            finish();
            progressbar.setVisibility(View.GONE);
        }
    }


    /*
    Show
    Internet
    Connection Dialog
     */
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),
                                RetailerStartUpScreen.class));
                        finish();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /*
    Fields
    Validations
     */
    private boolean validateFields() {
        if (_email.isEmpty()) {
            email.setError("Phone number can not be empty");
            email.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Password can not be empty");
            password.requestFocus();
            return false;
        } else {
            email.setError(null);
            password.setError(null);
            email.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void loadFields() {
        _email = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
        _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
    }

    /*
    Function to call
    the Forget Password
    Screen
     */
    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }


    public void callSignUpFromLoginPharmacy(View view) {
        startActivity(new Intent(getApplicationContext(), SingUpPharmacy.class));
        finish();
    }
}