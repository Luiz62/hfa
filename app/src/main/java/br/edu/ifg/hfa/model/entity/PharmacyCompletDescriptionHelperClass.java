package br.edu.ifg.hfa.model.entity;

public class PharmacyCompletDescriptionHelperClass {

    private String nomeMedico, crmMedico, localConsulta, nomePaciente,
    dataValidacao, dataCriacaoReceita, cnpjFarmacia, emailFarmacia, nomeFarmacia;

    public PharmacyCompletDescriptionHelperClass() {
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
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

    public String getCnpjFarmacia() {
        return cnpjFarmacia;
    }

    public void setCnpjFarmacia(String cnpjFarmacia) {
        this.cnpjFarmacia = cnpjFarmacia;
    }

    public String getEmailFarmacia() {
        return emailFarmacia;
    }

    public void setEmailFarmacia(String emailFarmacia) {
        this.emailFarmacia = emailFarmacia;
    }

    public String getNomeFarmacia() {
        return nomeFarmacia;
    }

    public void setNomeFarmacia(String nomeFarmacia) {
        this.nomeFarmacia = nomeFarmacia;
    }

    @Override
    public String toString() {
        return "PharmacyCompletDescriptionHelperClass{" +
                "nomeMedico='" + nomeMedico + '\'' +
                ", crmMedico='" + crmMedico + '\'' +
                ", localConsulta='" + localConsulta + '\'' +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", dataValidacao='" + dataValidacao + '\'' +
                ", dataCriacaoReceita='" + dataCriacaoReceita + '\'' +
                ", cnpjFarmacia='" + cnpjFarmacia + '\'' +
                ", emailFarmacia='" + emailFarmacia + '\'' +
                ", nomeFarmacia='" + nomeFarmacia + '\'' +
                '}';
    }
}
