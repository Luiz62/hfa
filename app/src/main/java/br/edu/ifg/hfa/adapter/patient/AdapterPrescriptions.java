package br.edu.ifg.hfa.adapter.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifg.hfa.R;
import br.edu.ifg.hfa.adapter.RecyclerViewInterface;
import br.edu.ifg.hfa.model.entity.PrescriptionsHelperClass;


public class AdapterPrescriptions extends RecyclerView.Adapter<AdapterPrescriptions.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    private List<PrescriptionsHelperClass> prescriptions;
    private Context context;

    public AdapterPrescriptions(List<PrescriptionsHelperClass> prescriptions, Context context,
                                RecyclerViewInterface recyclerViewInterface) {
        this.prescriptions = prescriptions;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_prescriptions,
                parent, false);
        return new MyViewHolder(list, recyclerViewInterface);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PrescriptionsHelperClass prescription = prescriptions.get(position);

        holder.id.setText(prescription.getLocalConsulta());
        holder.nomeMedico.setText(prescription.getNomeMedico());
        holder.data.setText(prescription.getData());
    }


    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, nomeMedico, data;

        public MyViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            id = view.findViewById(R.id.textId);
            nomeMedico = view.findViewById(R.id.textNomeMedico);
            data = view.findViewById(R.id.textData);

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
