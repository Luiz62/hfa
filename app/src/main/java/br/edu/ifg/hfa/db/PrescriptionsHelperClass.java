package br.edu.ifg.hfa.db;

import java.io.Serializable;

public class PrescriptionsHelperClass {

    private String nomeHospital;

    private String nomeMedico;

    private String data;

    public PrescriptionsHelperClass() {
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public String getData() {
        return data;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PrescriptionsHelperClass{" +
                "nomeHospital='" + nomeHospital + '\'' +
                ", nomeMedico='" + nomeMedico + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
