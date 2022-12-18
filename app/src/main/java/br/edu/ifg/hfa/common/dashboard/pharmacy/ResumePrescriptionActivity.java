package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import br.edu.ifg.hfa.R;

public class ResumePrescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_prescription);
        String idPrescriptions = getIntent().getStringExtra("ID_PRESCRIPTION");

    }
}