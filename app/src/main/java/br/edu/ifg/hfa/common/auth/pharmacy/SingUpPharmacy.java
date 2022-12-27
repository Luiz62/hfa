package br.edu.ifg.hfa.common.auth.pharmacy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.model.entity.PharmacyHelperClass;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.common.dashboard.pharmacy.PharmacyDashboard;

public class SingUpPharmacy extends AppCompatActivity {

    private ImageView backBtn;
    private Button next;
    private TextView titleText;

    private TextInputLayout namePharmacy, cnpjPharmacy, emailPharmacy, passwordPharmacy;

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;

    private String _name, _email, _password, _cnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_sign_up_pharmacy);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();

        backBtn = findViewById(R.id.signup_back_button_pharmacy);
        next = findViewById(R.id.signup_next_button_pharmacy);
        titleText = findViewById(R.id.signup_title_text_pharmacy);

        namePharmacy = findViewById(R.id.signup_name_pharmacy);
        emailPharmacy = findViewById(R.id.signup_email_pharmacy);
        cnpjPharmacy = findViewById(R.id.signup_cnpj_pharmacy);
        passwordPharmacy = findViewById(R.id.signup_password_pharmacy);

    }

    public void callNextSigupScreen(View view) {
        fields();

        if (!validateName() | !validateCnpj() | !validateEmail() | !validatePassword())
            return;


        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            storeNewPharmacyData();
                        else
                            Toast.makeText(getApplicationContext(), "Falha ao realizar o cadastro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeNewPharmacyData() {

        Intent intent = new Intent(getApplicationContext(), PharmacyDashboard.class);

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = rootNode.getReference("users");

            PharmacyHelperClass addNewUser = new PharmacyHelperClass(_name, _email, _cnpj);
            reference.child(Objects.requireNonNull(mAuth.getUid())).setValue(addNewUser);

            SessionManager sessionManager = new SessionManager(this,
                    SessionManager.SESSION_PHARMACYSESSION);
            sessionManager.createLoginSessionPharmacy(_name, _email, _cnpj);

            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();

            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View, String>(next, "transition_next_btn");
            pairs[2] = new Pair<View, String>(titleText, "transition_title_text");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(SingUpPharmacy.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    private boolean validateName() {
        if (_name.isEmpty()) {
            namePharmacy.setError("Esse campo não pode ser vazio!");
            return false;
        } else {
            namePharmacy.setError(null);
            namePharmacy.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateCnpj() {
        if (_cnpj.isEmpty()) {
            cnpjPharmacy.setError("Esse campo não pode ser vazio!");
            return false;
        } else if (_cnpj.length() > 14) {
            cnpjPharmacy.setError("Informe apenas 14 digitos!");
            return false;
        } else {
            cnpjPharmacy.setError(null);
            cnpjPharmacy.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (_email.isEmpty()) {
            emailPharmacy.setError("Esse campo não pode ser vazio!");
            return false;
        } else if (!_email.matches(checkEmail)) {
            emailPharmacy.setError("E-mail inválido!");
            return false;
        } else {
            emailPharmacy.setError(null);
            emailPharmacy.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 4 characters
                "$";

        if (_password.isEmpty()) {
            passwordPharmacy.setError("Esse campo não pode ser vazio!");
            return false;
        } else if (!_password.matches(checkPassword)) {
            passwordPharmacy.setError("Informe uma senha com no minino 6 digitos!");
            return false;
        } else {
            passwordPharmacy.setError(null);
            passwordPharmacy.setErrorEnabled(false);
            return true;
        }

    }

    private void fields() {
        _name = Objects.requireNonNull(namePharmacy.getEditText()).getText().toString().trim();
        _cnpj = Objects.requireNonNull(cnpjPharmacy.getEditText()).getText().toString().trim();
        _email = Objects.requireNonNull(emailPharmacy.getEditText()).getText().toString().trim();
        _password = Objects.requireNonNull(passwordPharmacy.getEditText()).getText().toString().trim();
    }

    public void callLoginFromSignUp(View view) {
        startActivity(new Intent(getApplicationContext(), LoginPharmacy.class));
        finish();
    }
}