package br.edu.ifg.hfa.model.entity;

public class PharmacyHelperClass {

    private String name, cnpj, email, password;

    public PharmacyHelperClass() {
    }

    public PharmacyHelperClass(String name, String cnpj, String email, String password) {
        this.name = name;
        this.cnpj = cnpj;
        this.email = email;
        this.password = password;
    }

    public PharmacyHelperClass(String name, String email, String cnpj) {
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
