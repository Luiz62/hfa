package br.edu.ifg.hfa.model.entity;

public class PatientHelperClass {

    private String name, rg, cpf, email, phoneNo, date, gender;

    public PatientHelperClass() {
    }

    public PatientHelperClass(String name, String rg, String cpf, String email, String phoneNo,
                              String date, String gender) {
        this.name = name;
        this.rg = rg;
        this.cpf = cpf;
        this.email = email;
        this.phoneNo = phoneNo;
        this.date = date;
        this.gender = gender;
    }

    public String getRg() {
        return rg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

