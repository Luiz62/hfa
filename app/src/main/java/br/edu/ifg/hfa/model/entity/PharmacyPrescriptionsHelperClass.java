package br.edu.ifg.hfa.model.entity;

public class PharmacyPrescriptionsHelperClass {

    private String id;

    private String nomeMedico;

    private String nomePaciente;

    private String dataValidacao;

    public PharmacyPrescriptionsHelperClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getDataValidacao() {
        return dataValidacao;
    }

    public void setDataValidacao(String dataValidacao) {
        this.dataValidacao = dataValidacao;
    }
}
