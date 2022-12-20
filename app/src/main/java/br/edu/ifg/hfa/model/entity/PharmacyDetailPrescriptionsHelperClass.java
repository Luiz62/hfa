package br.edu.ifg.hfa.model.entity;

import java.util.List;

public class PharmacyDetailPrescriptionsHelperClass {

    private String id;

    private String nomeFarmacia;

    private String emailFarmacia;

    private String cnpjFarmacia;

    private List<MedicationHelperClass> medications;

    private String nomeMedico;

    private String crmMedico;

    private String nomePaciente;

    private String dataValidacao;

    private String dataCriacaoReceita;

    private String localConsulta;

    public PharmacyDetailPrescriptionsHelperClass() {
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

    public String getDataCriacaoReceita() {
        return dataCriacaoReceita;
    }

    public void setDataCriacaoReceita(String dataCriacaoReceita) {
        this.dataCriacaoReceita = dataCriacaoReceita;
    }

    public String getNomeFarmacia() {
        return nomeFarmacia;
    }

    public void setNomeFarmacia(String nomeFarmacia) {
        this.nomeFarmacia = nomeFarmacia;
    }

    public String getEmailFarmacia() {
        return emailFarmacia;
    }

    public void setEmailFarmacia(String emailFarmacia) {
        this.emailFarmacia = emailFarmacia;
    }

    public List<MedicationHelperClass> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationHelperClass> medications) {
        this.medications = medications;
    }

    public String getCnpjFarmacia() {
        return cnpjFarmacia;
    }

    public void setCnpjFarmacia(String cnpjFarmacia) {
        this.cnpjFarmacia = cnpjFarmacia;
    }

    public String getCrmMedico() {
        return crmMedico;
    }

    public void setCrmMedico(String crmMedico) {
        this.crmMedico = crmMedico;
    }

    public String getLocalConsulta() {
        return localConsulta;
    }

    public void setLocalConsulta(String localConsulta) {
        this.localConsulta = localConsulta;
    }
}
