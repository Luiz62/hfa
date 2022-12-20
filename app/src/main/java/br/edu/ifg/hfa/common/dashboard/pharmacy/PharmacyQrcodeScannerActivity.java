package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.AdapterResumePrescriptions;
import br.edu.ifg.hfa.common.auth.patient.LoginPatient;
import br.edu.ifg.hfa.common.auth.patient.RetailerStartUpScreen;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.MedicationHelperClass;
import br.edu.ifg.hfa.model.entity.PharmacyDetailPrescriptionsHelperClass;
import br.edu.ifg.hfa.model.entity.PharmacyHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;
import br.edu.ifg.hfa.utils.DateUtil;

public class PharmacyQrcodeScannerActivity extends AppCompatActivity {

    private DatabaseReference resumePrescriptionsRef;

    private ValueEventListener valueEventListenerPrescriptions;

    private List<MedicationHelperClass> medications = new ArrayList<>();

    private RecyclerView recyclerView;

    private AdapterResumePrescriptions adapterScannerQrcode;

    private PrescriptionsHelperClass prescriptions;

    private PharmacyHelperClass pharmacyHelperClass;

    private final PharmacyDetailPrescriptionsHelperClass helperClass = new PharmacyDetailPrescriptionsHelperClass();

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

        FirebaseDatabase rootNode = DbConnection.getInstance();

        if (!medications.isEmpty())
            helperClass.setMedications(medications);

        if (prescriptions != null) {
            helperClass.setNomePaciente(prescriptions.getNomePaciente());
            helperClass.setNomeMedico(prescriptions.getNomeMedico());
            helperClass.setCrmMedico(prescriptions.getCrmMedico());
            helperClass.setDataValidacao(DateUtil
                    .dateToString(new Date(), DateUtil.PATTERN_DEFAULT));
            helperClass.setDataCriacaoReceita(prescriptions.getData());
            helperClass.setLocalConsulta(prescriptions.getLocalConsulta());
        }

        FirebaseUser firebaseUser = DbConnection.getAuth().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference userRef = rootNode.getReference("users")
                    .child(firebaseUser.getUid());

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pharmacyHelperClass = snapshot.getValue(PharmacyHelperClass.class);
                    if (pharmacyHelperClass != null) {
                        if (pharmacyHelperClass.getEmail() != null)
                            helperClass.setEmailFarmacia(pharmacyHelperClass.getEmail());

                        if (pharmacyHelperClass.getCnpj() != null)
                            helperClass.setCnpjFarmacia(pharmacyHelperClass.getCnpj());

                        if (pharmacyHelperClass.getName() != null)
                            helperClass.setNomeFarmacia(pharmacyHelperClass.getName());
                    }

                    DatabaseReference reference = rootNode.getReference("pharmacy")
                            .child("prescriptions")
                            .child(firebaseUser.getUid());

                    reference.setValue(helperClass);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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