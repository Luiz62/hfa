package br.edu.ifg.hfa.common.auth.patient;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import br.edu.ifg.hfa.R;

public class SignUp2ndClass extends AppCompatActivity {

    ImageView backBtn;
    Button next;
    TextView titleText, slideText;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2nd_class);

        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);
    }

    public void call3rdSigupScreen(View view) {

        if (!validateGender() | !validateAge())
            return;

        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String _gender = selectedGender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String _date = day + "/" + month + "/" + year;

        String _name = getIntent().getStringExtra("name");
        String _email = getIntent().getStringExtra("email");
        String _cpf = getIntent().getStringExtra("cpf");
        String _rg = getIntent().getStringExtra("rg");


        Intent intent = new Intent(getApplicationContext(), SignUp3rdClass.class);

        intent.putExtra("name", _name);
        intent.putExtra("email", _email);
        intent.putExtra("cpf", _cpf);
        intent.putExtra("rg", _rg);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[3] = new Pair<View, String>(slideText, "transition_slide_text");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 13) {
            Toast.makeText(this, "You are not eligible to apply",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}