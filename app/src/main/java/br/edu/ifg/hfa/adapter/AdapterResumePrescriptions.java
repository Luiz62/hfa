package br.edu.ifg.hfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.model.entity.MedicationHelperClass;


public class AdapterResumePrescriptions extends RecyclerView.Adapter<AdapterResumePrescriptions.MyViewHolder> {

    private List<MedicationHelperClass> medications;
    private Context context;

    public AdapterResumePrescriptions(List<MedicationHelperClass> medications, Context context) {
        this.medications = medications;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_resume_prescriptions,
                parent, false);
        return new MyViewHolder(list);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MedicationHelperClass prescription = medications.get(position);

        holder.nomeMedicamento.setText(prescription.getNomeMedicamento());
        holder.descricaoMedicamento.setText(prescription.getDescricaoMedicamento());
    }


    @Override
    public int getItemCount() {
        return medications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeMedicamento, descricaoMedicamento;

        public MyViewHolder(View view) {
            super(view);

            nomeMedicamento = view.findViewById(R.id.textMedication);
            descricaoMedicamento = view.findViewById(R.id.textDescriptionMedication);
        }

    }

}
