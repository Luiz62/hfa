package br.edu.ifg.hfa.common.auth.patient;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.utils.CheckInternet;

public class SignUp3rdClass extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    RelativeLayout progressbar;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    private String _phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3rd_class);

        mAuth = FirebaseAuth.getInstance();
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);
        progressbar = findViewById(R.id.signup_progress_bar);

    }

    public void callVerifyOTPScreen(View view) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        loadFields();

        if (!validatePhoneNumber())
            return;

        final String _name = getIntent().getStringExtra("name");
        final String _email = getIntent().getStringExtra("email");
        final String _cpf = getIntent().getStringExtra("cpf");
        final String _rg = getIntent().getStringExtra("rg");
        final String _date = getIntent().getStringExtra("date");
        final String _gender = getIntent().getStringExtra("gender");

        if (_phoneNumber.charAt(0) == '0')
            _phoneNumber = _phoneNumber.substring(1);

        String _countryCode = countryCodePicker.getSelectedCountryCode();
        String _number = _phoneNumber;
        final String _phoneNo = "+" + _countryCode + _number;

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        intent.putExtra("name", _name);
        intent.putExtra("email", _email);
        intent.putExtra("cpf", _cpf);
        intent.putExtra("rg", _rg);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);
        intent.putExtra("phoneNo", _phoneNo);
        intent.putExtra("whatToDO", "createNewUser");

        progressbar.setVisibility(View.VISIBLE);

        otpSend(intent);
    }

    private void otpSend(Intent intent) {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressbar.setVisibility(View.VISIBLE);
                Toast.makeText(SignUp3rdClass.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressbar.setVisibility(View.VISIBLE);
                Toast.makeText(SignUp3rdClass.this, "OTP is successfully send.",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra("codeBySystem", verificationId);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(scrollView, "transition_OTP_screen");
                 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                progressbar.setVisibility(View.GONE);
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
                        startActivity(new Intent(getApplicationContext(),
                                RetailerStartUpScreen.class));
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validatePhoneNumber() {
        String checkspaces = "\\A\\w{1,20}\\z";
        if (_phoneNumber.isEmpty()) {
            phoneNumber.setError("Informe um número válido");
            return false;
        } else if (!_phoneNumber.matches(checkspaces)) {
            phoneNumber.setError("Não são permitidos espaços em branco!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }

    private void loadFields() {
        _phoneNumber = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();
    }
}