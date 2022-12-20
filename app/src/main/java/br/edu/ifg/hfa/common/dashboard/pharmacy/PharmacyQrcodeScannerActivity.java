package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.AdapterResumePrescriptions;
import br.edu.ifg.hfa.common.auth.patient.LoginPatient;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.MedicationHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;

public class PharmacyQrcodeScannerActivity extends AppCompatActivity {

    private DatabaseReference resumePrescriptionsRef;

    private ValueEventListener valueEventListenerPrescriptions;

    private List<MedicationHelperClass> medications = new ArrayList<>();

    private RecyclerView recyclerView;

    private AdapterResumePrescriptions adapterScannerQrcode;

    private PrescriptionsHelperClass prescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_qrcode_scanner);

        recyclerView = findViewById(R.id.recycler_view_qrcode_scanner);

        String[] values = getIntent().getStringExtra("ID_PRESCRIPTION").split(";");

        String cpf, id;
        cpf = values[0];
        id = values[1];


        resumePrescriptionsRef = DbConnection.getDatabaseReference().child("patients")
                .child("prescriptions")
                .child(cpf)
                .child(id);

        valueEventListenerPrescriptions = resumePrescriptionsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prescriptions = snapshot.
                        getValue(PrescriptionsHelperClass.class);

                DataSnapshot medicamentos = snapshot.child("medicamentos");
                for (DataSnapshot dados: medicamentos.getChildren()) {

                    MedicationHelperClass medication = dados.getValue(MedicationHelperClass.class);
                    if (medication != null) {
                        medication.setId(dados.getKey());
                        medications.add(medication);
                    }

                    adapterScannerQrcode.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapterScannerQrcode = new AdapterResumePrescriptions(medications, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterScannerQrcode);

    }

    public void onClickButton(View view) {


        Intent intent = new Intent(getApplicationContext(), VerificarReceitaActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListenerPrescriptions != null)
            resumePrescriptionsRef.removeEventListener(valueEventListenerPrescriptions);
    }
}