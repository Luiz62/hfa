package br.edu.ifg.hfa.adapter.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.db.PrescriptionsHelperClass;


public class AdapterPrescriptions extends RecyclerView.Adapter<AdapterPrescriptions.MyViewHolder> {

    private List<PrescriptionsHelperClass> prescriptions;
    private Context context;

    public AdapterPrescriptions(List<PrescriptionsHelperClass> prescriptions, Context context) {
        this.prescriptions = prescriptions;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_prescriptions,
                parent, false);
        return new MyViewHolder(list);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PrescriptionsHelperClass prescription = prescriptions.get(position);

        holder.id.setText(prescription.getNomeHospital());
        holder.nomeMedico.setText(prescription.getNomeMedico());
        holder.data.setText(prescription.getData());
    }


    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, nomeMedico, data;

        public MyViewHolder(View view) {
            super(view);

            id = view.findViewById(R.id.textId);
            nomeMedico = view.findViewById(R.id.textNomeMedico);
            data = view.findViewById(R.id.textData);
        }

    }

}
