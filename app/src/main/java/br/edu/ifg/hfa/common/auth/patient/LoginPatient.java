package br.edu.ifg.hfa.common.auth.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.user.patient.PatientDashboard;
import br.edu.ifg.hfa.utils.CheckInternet;

public class LoginPatient extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber;
    RelativeLayout progressbar;
    TextInputEditText phoneNumberEditText;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String _phoneNumber;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_login);

        mAuth = FirebaseAuth.getInstance();

        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        progressbar = findViewById(R.id.login_progress_bar);
        phoneNumberEditText = findViewById(R.id.login_phone_number_editText);


        SessionManager sessionManager = new SessionManager(LoginPatient.this, SessionManager.SESSION_REMEMMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRemeberMeDetailsFromSession();
            phoneNumberEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
        }

    }

    /*
    Login the
    user in
    app!
     */
    public void letTheUserLoggedInPatient(View view) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateFields()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        loadFields();
        //Get values from fields

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        } //remove 0 at the start if entered by the user
        final String _completePhoneNumber = "+" + countryCodePicker
                .getSelectedCountryCode() + _phoneNumber;

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("phoneNo", _completePhoneNumber);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressbar.setVisibility(View.VISIBLE);
                Toast.makeText(LoginPatient.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressbar.setVisibility(View.VISIBLE);
                Toast.makeText(LoginPatient.this, "OTP is successfully send.",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra("codeBySystem", verificationId);
                intent.putExtra("whatToDO", "loginPatient");

                progressbar.setVisibility(View.GONE);

                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(intent.getStringExtra("phoneNo"))
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
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

        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (_phoneNumber.isEmpty()) {
            phoneNumber.setError("Phone number can not be empty");
            phoneNumber.requestFocus();
            return false;
        } else if (!_phoneNumber.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private void loadFields() {
        _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
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