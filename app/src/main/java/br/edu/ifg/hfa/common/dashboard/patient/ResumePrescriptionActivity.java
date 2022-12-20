package br.edu.ifg.hfa.common.dashboard.patient;

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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.AdapterResumePrescriptions;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.MedicationHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;
import br.edu.ifg.hfa.db.SessionManager;

public class ResumePrescriptionActivity extends AppCompatActivity {

    private TextView nomeMedico, crmMedico, localConsulta, nomePaciente, data;

    private ImageView qrcode, backBtn;

    private String cpf;

    private AdapterResumePrescriptions adapterResumePrescriptions;

    private List<MedicationHelperClass> medications = new ArrayList<>();

    private RecyclerView recyclerView;

    private ValueEventListener valueEventListenerResumeePrescriptions;

    private DatabaseReference resumePrescriptionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_prescription);
        String idPrescriptions = getIntent().getStringExtra("ID_PRESCRIPTION");
        cpf = getIntent().getStringExtra(SessionManager.KEY_CPF);

        nomeMedico = findViewById(R.id.nome_medico);
        crmMedico = findViewById(R.id.crm_medico);
        localConsulta = findViewById(R.id.local_consulta);
        nomePaciente = findViewById(R.id.nome_paciente);
        data = findViewById(R.id.data_prescription_validation);
        qrcode = findViewById(R.id.qrcode_imagem);
        recyclerView = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.back_pressed_resume);

        if (!idPrescriptions.isEmpty()) {

            QRGEncoder encoder = new QRGEncoder(String.format("%s;%s", cpf, idPrescriptions), null,
                    QRGContents.Type.TEXT, 800);

            encoder.setColorBlack(0xFFE5E5E5);
            encoder.setColorWhite(0xFF000000);

            qrcode.setImageBitmap(encoder.getBitmap());

            resumePrescriptionsRef = DbConnection.getDatabaseReference().child("patients")
                    .child("prescriptions")
                    .child(cpf)
                    .child(idPrescriptions);

            valueEventListenerResumeePrescriptions = resumePrescriptionsRef
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            PrescriptionsHelperClass prescriptions = snapshot.
                                    getValue(PrescriptionsHelperClass.class);


                            if (prescriptions != null) {
                                if (prescriptions.getCrmMedico() != null) {
                                    crmMedico.setText(prescriptions.getCrmMedico());
                                }

                                if (prescriptions.getLocalConsulta() != null) {
                                    localConsulta.setText(prescriptions.getLocalConsulta());
                                }

                                if (prescriptions.getNomePaciente() != null) {
                                    nomePaciente.setText(prescriptions.getNomePaciente());
                                }

                                if (prescriptions.getNomeMedico() != null) {
                                    nomeMedico.setText(prescriptions.getNomeMedico());
                                }

                                if (prescriptions.getData() != null) {
                                    data.setText(prescriptions.getData());
                                }
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


        adapterResumePrescriptions = new AdapterResumePrescriptions(medications, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterResumePrescriptions);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PrescriptionsActivity.class));
                finish();
            }
        });
    }


}