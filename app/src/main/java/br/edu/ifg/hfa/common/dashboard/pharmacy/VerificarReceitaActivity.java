package br.edu.ifg.hfa.common.dashboard.pharmacy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.RecyclerViewInterface;
import br.edu.ifg.hfa.adapter.patient.AdapterPrescriptions;
import br.edu.ifg.hfa.adapter.pharmacy.AdapterVerificarReceita;
import br.edu.ifg.hfa.common.dashboard.patient.PrescriptionsActivity;
import br.edu.ifg.hfa.common.dashboard.patient.ResumePrescriptionActivity;
import br.edu.ifg.hfa.common.dashboard.pharmacy.PharmacyDashboard;
import br.edu.ifg.hfa.db.DbConnection;
import br.edu.ifg.hfa.db.SessionManager;
import br.edu.ifg.hfa.model.entity.PharmacyPrescriptionsHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;

public class VerificarReceitaActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ImageView backBtn;

    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private AdapterVerificarReceita adapterVerificarReceita;

    private List<PharmacyPrescriptionsHelperClass> prescriptions = new ArrayList<>();

    private ValueEventListener valueEventListener;

    private DatabaseReference databaseReference;

    private RelativeLayout progressBar;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_receita);

        progressBar = findViewById(R.id.verificar_receita_progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        backBtn = findViewById(R.id.back_pressed_verificar_receita);

        recyclerView = findViewById(R.id.recycler_view_verificar_receita);

        floatingActionButton = findViewById(R.id.floating_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PharmacyDashboard.class));
                finish();
            }
        });

        adapterVerificarReceita = new AdapterVerificarReceita(prescriptions, this,
                this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterVerificarReceita);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0)
                    floatingActionButton.hide();
                else
                    floatingActionButton.show();

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }

    @Override
    protected void onStart() {
        user = DbConnection.getAuth().getCurrentUser();
        if (user != null) {
            if (!user.getUid().isEmpty()) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("pharmacy")
                        .child("prescriptions")
                        .child(user.getUid());

                valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prescriptions.clear();
                        for (DataSnapshot dados: snapshot.getChildren()) {
                            PharmacyPrescriptionsHelperClass prescription = dados.getValue(PharmacyPrescriptionsHelperClass.class);
                            if (prescription != null) {
                                prescription.setId(dados.getKey());
                                prescriptions.add(prescription);
                                adapterVerificarReceita.notifyDataSetChanged();
                            }

                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }

                });
            }
        }
        super.onStart();
    }

    private void scanCode() {

        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to flash on");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(scanOptions);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(VerificarReceitaActivity.this,
                PharmacyResumePrescriptionActivity.class);

        intent.putExtra("PHARMACY_PRESCRIPTIONS_ID", user.getUid());
        intent.putExtra("PHARMACY_HASH", prescriptions.get(position).getId());
        startActivity(intent);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Intent intent = new Intent(getApplicationContext(),
                    PharmacyQrcodeScannerActivity.class);
            intent.putExtra("ID_PRESCRIPTION", result.getContents());

            startActivity(intent);
        }
    });

    @Override
    protected void onStop() {
        if (valueEventListener != null)
            databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }
}