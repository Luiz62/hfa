package br.edu.ifg.hfa.common.dashboard.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.RecyclerViewInterface;
import br.edu.ifg.hfa.adapter.patient.AdapterPrescriptions;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;
import br.edu.ifg.hfa.db.SessionManager;

public class PrescriptionsActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ImageView backBtn;

    private RecyclerView recyclerView;

    private AdapterPrescriptions adapterPrescriptions;

    private List<PrescriptionsHelperClass> prescriptions = new ArrayList<>();

    private String cpf;

    private ValueEventListener valueEventListenerPrescriptions;

    private DatabaseReference prescriptionsRef;

    private SessionManager sessionManager;

    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_prescriptions);

        progressBar = findViewById(R.id.menu_prescriptions_progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        cpf = usersDetails.get(SessionManager.KEY_CPF);

        backBtn = findViewById(R.id.back_pressed_prescriptions);

        recyclerView = findViewById(R.id.recycler_view);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PatientDashboard.class));
                finish();
            }
        };

        adapterPrescriptions = new AdapterPrescriptions(prescriptions, this,
                this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterPrescriptions);

        backBtn.setOnClickListener(onClickListener);
    }

    private void loadList() {
        if (cpf != null) {
            prescriptionsRef = DbConnection.getDatabaseReference().child("patients")
                    .child("prescriptions")
                    .child(cpf);

            valueEventListenerPrescriptions = prescriptionsRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    prescriptions.clear();
                    for (DataSnapshot dados: snapshot.getChildren()) {
                        PrescriptionsHelperClass prescription = dados.getValue(PrescriptionsHelperClass.class);
                        if (prescription != null) {
                            prescription.setId(dados.getKey());
                        }
                        prescriptions.add(prescription);

                    }

                    adapterPrescriptions.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListenerPrescriptions != null)
            prescriptionsRef.removeEventListener(valueEventListenerPrescriptions);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(PrescriptionsActivity.this,
                ResumePrescriptionActivity.class);

        intent.putExtra("ID_PRESCRIPTION", prescriptions.get(position).getId());
        intent.putExtra(SessionManager.KEY_CPF, cpf);

        startActivity(intent);
    }
}