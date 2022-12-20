package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.AdapterResumePrescriptions;
import br.edu.ifg.hfa.common.dashboard.patient.PrescriptionsActivity;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.MedicationHelperClass;
import br.edu.ifg.hfa.model.entity.PharmacyCompletDescriptionHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;

public class PharmacyResumePrescriptionActivity extends AppCompatActivity {
    private TextView nomeMedico, crmMedico, localConsulta, nomePaciente,
            dataValidacao, dataCriacaoReceita, cnpjFarmacia, emailFarmacia, nomeFarmacia;

    private ImageView backBtn;

    private AdapterResumePrescriptions adapterResumePrescriptions;

    private List<MedicationHelperClass> medications = new ArrayList<>();

    private PharmacyCompletDescriptionHelperClass pharmacy;

    private RecyclerView recyclerView;

    private String pharmacyHash, pharmacyPrescriptionsId;

    private ValueEventListener valueEventListenerResumeePrescriptions;

    private DatabaseReference resumePrescriptionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_resume_prescription);
        pharmacyPrescriptionsId = getIntent().getStringExtra("PHARMACY_PRESCRIPTIONS_ID");
        pharmacyHash = getIntent().getStringExtra("PHARMACY_HASH");
        backBtn = findViewById(R.id.back_pressed_resume);
        recyclerView = findViewById(R.id.recycler_view);

        nomeFarmacia = findViewById(R.id.nome_farmacia);
        emailFarmacia = findViewById(R.id.email_farmacia);
        cnpjFarmacia = findViewById(R.id.cnpj_farmacia);
        crmMedico = findViewById(R.id.crm_medico);
        dataValidacao = findViewById(R.id.data_prescription_validation);
        dataCriacaoReceita = findViewById(R.id.data_prescription_create);
        localConsulta = findViewById(R.id.local_consulta);
        nomeMedico = findViewById(R.id.nome_medico);
        nomePaciente = findViewById(R.id.nome_paciente);
        adapterResumePrescriptions = new AdapterResumePrescriptions(medications, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterResumePrescriptions);

        loadRecyclerView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VerificarReceitaActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        loadRecyclerView();
        super.onStart();
    }

    private void loadRecyclerView() {

        if (!pharmacyPrescriptionsId.isEmpty()) {
            if (!pharmacyHash.isEmpty()) {
                resumePrescriptionsRef = DbConnection.getDatabaseReference().child("pharmacy")
                        .child("prescriptions")
                        .child(pharmacyPrescriptionsId)
                        .child(pharmacyHash);

                valueEventListenerResumeePrescriptions = resumePrescriptionsRef
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                PharmacyCompletDescriptionHelperClass prescriptions = snapshot.
                                        getValue(PharmacyCompletDescriptionHelperClass.class);


                                if (prescriptions != null) {
                                    if (prescriptions.getNomeFarmacia() != null)
                                        nomeFarmacia.setText(prescriptions.getNomeFarmacia());

                                    if (prescriptions.getEmailFarmacia() != null)
                                        emailFarmacia.setText(prescriptions.getEmailFarmacia());

                                    if (prescriptions.getCnpjFarmacia() != null)
                                        cnpjFarmacia.setText(prescriptions.getCnpjFarmacia());

                                    if (prescriptions.getCrmMedico() != null)
                                        crmMedico.setText(prescriptions.getCrmMedico());

                                    if (prescriptions.getLocalConsulta() != null)
                                        localConsulta.setText(prescriptions.getLocalConsulta());

                                    if (prescriptions.getNomePaciente() != null)
                                        nomePaciente.setText(prescriptions.getNomePaciente());

                                    if (prescriptions.getNomeMedico() != null)
                                        nomeMedico.setText(prescriptions.getNomeMedico());

                                    if (prescriptions.getDataValidacao() != null)
                                        dataValidacao.setText(prescriptions.getDataValidacao());

                                    if (prescriptions.getDataCriacaoReceita() != null)
                                        dataCriacaoReceita.setText(prescriptions.getDataCriacaoReceita());
                                }

                                DataSnapshot medicamentos = snapshot.child("medications");
                                for (DataSnapshot dados: medicamentos.getChildren()) {

                                    MedicationHelperClass medication = dados.getValue(MedicationHelperClass.class);
                                    if (medication != null) {
                                        medication.setId(dados.getKey());
                                        medications.add(medication);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
    }

    @Override
    protected void onStop() {
        if (valueEventListenerResumeePrescriptions != null)
            resumePrescriptionsRef.removeEventListener(valueEventListenerResumeePrescriptions);
        super.onStop();
    }
}