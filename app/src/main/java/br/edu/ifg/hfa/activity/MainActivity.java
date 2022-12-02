package br.edu.ifg.hfa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.common.login.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}