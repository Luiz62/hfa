package br.edu.ifg.hfa.adapter.pharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.RecyclerViewInterface;
import br.edu.ifg.hfa.model.entity.PharmacyPrescriptionsHelperClass;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;


public class AdapterVerificarReceita extends RecyclerView.Adapter<AdapterVerificarReceita.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    private List<PharmacyPrescriptionsHelperClass> prescriptions;
    private Context context;

    public AdapterVerificarReceita(List<PharmacyPrescriptionsHelperClass> prescriptions, Context context,
                                   RecyclerViewInterface recyclerViewInterface) {
        this.prescriptions = prescriptions;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pharmacy_prescriptions,
                parent, false);
        return new MyViewHolder(list, recyclerViewInterface);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PharmacyPrescriptionsHelperClass prescription = prescriptions.get(position);

        holder.nomePaciente.setText(prescription.getNomePaciente());
        holder.nomeMedico.setText(prescription.getNomeMedico());
        holder.data.setText(prescription.getDataValidacao());
    }


    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomePaciente, nomeMedico, data;

        public MyViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            nomePaciente = view.findViewById(R.id.nome_paciente_pharmacy_prescriptions);
            nomeMedico = view.findViewById(R.id.nome_medico_pharmacy_prescriptions);
            data = view.findViewById(R.id.data_pharmacy_prescriptions);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }

}
