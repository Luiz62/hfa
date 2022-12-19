package br.edu.ifg.hfa.model.entity;

import br.edu.ifg.hfa.utils.StringUtils;

public class PrescriptionsHelperClass {

    private String id;

    private String localConsulta;

    private String nomeMedico;

    private String crmMedico;

    private String nomePaciente;

    private String data;

    public PrescriptionsHelperClass() {
    }


    public String getLocalConsulta() {
        return localConsulta;
    }

    public void setLocalConsulta(String localConsulta) {
        this.localConsulta = localConsulta;
    }

    public String getNomeMedico() {
        return StringUtils.formatarNomeMedico(nomeMedico);
    }

    public String getData() {
        return data;
    }


    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrmMedico() {
        return crmMedico;
    }

    public void setCrmMedico(String crmMedico) {
        this.crmMedico = crmMedico;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    @Override
    public String toString() {
        return "PrescriptionsHelperClass{" +
                "id='" + id + '\'' +
                ", localConsulta='" + localConsulta + '\'' +
                ", nomeMedico='" + nomeMedico + '\'' +
                ", crmMedico='" + crmMedico + '\'' +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
