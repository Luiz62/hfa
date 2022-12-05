package br.edu.ifg.hfa.common.auth.farmacia;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.auth.paciente.ForgetPassword;
import br.edu.ifg.hfa.common.auth.paciente.RetailerStartUpScreen;
import br.edu.ifg.hfa.common.auth.paciente.SignUp;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.user.paciente.UserDashboard;
import br.edu.ifg.hfa.utils.CheckInternet;

public class LoginFarmacia extends AppCompatActivity {
    TextInputLayout email, password;
    RelativeLayout progressbar;
    CheckBox rememberMe;
    TextInputEditText phoneNumberEditTextFarmacia, passwordEditTextFarmacia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_farmacia);

        //hooks
        email = findViewById(R.id.login_email_farmacia);
        password = findViewById(R.id.login_password_farmacia);
        progressbar = findViewById(R.id.login_progress_bar_farmacia);
        rememberMe = findViewById(R.id.remember_me_farmacia);
        phoneNumberEditTextFarmacia = findViewById(R.id.login_email_number_editText_farmacia);
        passwordEditTextFarmacia = findViewById(R.id.login_password_editText_farmacia);


        //Check weather phone number and password is already saved in Shared Preferences or not
        SessionManager sessionManager = new SessionManager(LoginFarmacia.this,
                SessionManager.SESSION_REMEMMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager
                    .getRemeberMeDetailsFromSession();
            phoneNumberEditTextFarmacia.setText(rememberMeDetails
                    .get(SessionManager.KEY_EMAIL));
            passwordEditTextFarmacia.setText(rememberMeDetails
                    .get(SessionManager.KEY_SESSIONPASSWORD));
        }

    }


    /*
    Login the
    user in
    app!
     */
    public void letTheUserLoggedIn(View view) {

        //Check Internet Connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }


        //validate phone Number and Password
        if (!validateFields()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);


        //Get values from fields
        String _email = email.getEditText().getText().toString().trim();
        final String _password = password.getEditText().getText().toString().trim();


        //Check Remember Me Button to create it's session
        if (rememberMe.isChecked()) {
            SessionManager sessionManager = new SessionManager(LoginFarmacia.this,
                    SessionManager.SESSION_REMEMMBERME);
            sessionManager.createRememberMeSession(_email, _password);
        }

        //Check weather User exists or not in database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Pharmacies")
                .orderByChild("email").equalTo(_email);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //If Phone Number exists then get password
                if (dataSnapshot.exists()) {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    String systemPassword = dataSnapshot.child(_email).child("password").getValue(String.class);
                    //if password exists and matches with users password then get other fields from database
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        //Get users data from firebase database
                        String _name = dataSnapshot.child(_email)
                                .child("fullName").getValue(String.class);
                        String _cnpj = dataSnapshot.child(_email)
                                .child("cpf").getValue(String.class);
                        String _phoneNo = dataSnapshot.child(_email)
                                .child("phoneNo").getValue(String.class);
                        String _password = dataSnapshot.child(_email)
                                .child("password").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager = new SessionManager(LoginFarmacia.this,
                                SessionManager.SESSION_USERSESSION);

                        startActivity(new Intent(getApplicationContext(),
                                UserDashboard.class));
                        finish();
                        progressbar.setVisibility(View.GONE);

                    } else {
                        progressbar.setVisibility(View.GONE);
                        password.setError("Password does not match!");
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    email.setError("No such user exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(LoginFarmacia.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

        String _email = email.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (_email.isEmpty()) {
            email.setError("Phone number can not be empty");
            email.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Password can not be empty");
            password.requestFocus();
            return false;
        } else if (!_email.matches(checkspaces)) {
            email.setError("No White spaces are allowed!");
            return false;
        } else {
            email.setError(null);
            password.setError(null);
            email.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return true;
        }
    }


    /*
    Function to call
    the Forget Password
    Screen
     */
    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }


    public void callSignUpFromLogin(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }
}