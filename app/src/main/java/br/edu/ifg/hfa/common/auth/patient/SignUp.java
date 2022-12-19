package br.edu.ifg.hfa.common.auth.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import br.edu.ifg.hfa.R;

public class SignUp extends AppCompatActivity {

    ImageView backBtn;
    Button next;
    TextView titleText, slideText;

    TextInputLayout name, cpf, email, rg;
    private String _name, _cpf, _email, _rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_sign_up_patient);

        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        cpf = findViewById(R.id.signup_cpf);
        rg = findViewById(R.id.signup_rg);
    }


    public void callNextSigupScreen(View view) {
        loadFields();

        if (!validateName() | !validateCpf() | !validateEmail() | !validateRg())
            return;

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);

        intent.putExtra("name", _name);
        intent.putExtra("email", _email);
        intent.putExtra("cpf", _cpf);
        intent.putExtra("rg", _rg);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[3] = new Pair<View, String>(slideText, "transition_slide_text");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    private boolean validateName() {
        if (_name.isEmpty()) {
            name.setError("Esse campo não pode ser vazio");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCpf() {
        if (_cpf.isEmpty()) {
            cpf.setError("Esse campo não pode ser vazio!");
            return false;
        } else if (_cpf.length() > 11) {
            cpf.setError("Informe apenas 11 digitos!");
            return false;
        } else {
            cpf.setError(null);
            cpf.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (_email.isEmpty()) {
            email.setError("Esse campo não pode ser vazio!");
            return false;
        } else if (!_email.matches(checkEmail)) {
            email.setError("E-mail inválido!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRg() {
        if (_rg.isEmpty()) {
            rg.setError("Esse campo não pode ser vazio!");
            return false;
        } else {
            rg.setError(null);
            rg.setErrorEnabled(false);
            return true;
        }
    }

    private void loadFields() {
        _name = Objects.requireNonNull(name.getEditText()).getText().toString().trim();
        _email = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
        _cpf = Objects.requireNonNull(cpf.getEditText()).getText().toString().trim();
        _rg = Objects.requireNonNull(rg.getEditText()).getText().toString().trim();
    }

    public void callLoginFromSignUp(View view) {
        startActivity(new Intent(getApplicationContext(), LoginPatient.class));
        finish();
    }

}