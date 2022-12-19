package br.edu.ifg.hfa.model.entity;

import br.edu.ifg.hfa.utils.StringUtils;

public class MedicationHelperClass {

    private String id;


    private String nomeMedicamento;

    private String descricaoMedicamento;

    public MedicationHelperClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getDescricaoMedicamento() {
        return descricaoMedicamento;
    }

    public void setDescricaoMedicamento(String descricaoMedicamento) {
        this.descricaoMedicamento = descricaoMedicamento;
    }

    @Override
    public String toString() {
        return "MedicationHelperClass{" +
                "id='" + id + '\'' +
                ", nomeMedicamento='" + nomeMedicamento + '\'' +
                ", descricaoMedicamento='" + descricaoMedicamento + '\'' +
                '}';
    }
}
