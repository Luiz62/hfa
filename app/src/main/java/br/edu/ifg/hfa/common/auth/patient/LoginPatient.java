package br.edu.ifg.hfa.common.auth.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.db.SessionManager;
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

    }

    public void letTheUserLoggedInPatient(View view) {
        loadFields();

        CheckInternet checkInternet = new CheckInternet();

        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateFields())
            return;

        progressbar.setVisibility(View.VISIBLE);

        if (_phoneNumber.charAt(0) == '0')
            _phoneNumber = _phoneNumber.substring(1);

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

    private boolean validateFields() {

        String checkspaces = "\\A\\w{1,20}\\z";

        if (_phoneNumber.isEmpty()) {
            phoneNumber.setError("Esse número não pode ser vazio!");
            phoneNumber.requestFocus();
            return false;
        } else if (!_phoneNumber.matches(checkspaces)) {
            phoneNumber.setError("Sem espaços entre os números!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private void loadFields() {
        _phoneNumber = Objects.requireNonNull(phoneNumber.getEditText()).getText()
                .toString().trim();
    }

    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }

    public void callSignUpFromLogin(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }
}