package br.edu.ifg.hfa.common.signup;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import br.edu.ifg.hfa.R;

public class SignUp3 extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        //Hooks
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);
        progressbar = findViewById(R.id.signup_progress_bar);
    }
}