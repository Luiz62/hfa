package br.edu.ifg.hfa.utils;

public class StringUtils {

    public static String formatarNome(String nome) {
        String nomeFinal = "";
        String[] nomes = nome.split(" ");
        if (nomes.length > 0)
            nomeFinal += nomes[0];

        nomeFinal += " " + nomes[nomes.length - 1];
        return nomeFinal;
    }

    public static String formatarNomeMedico(String nomeMedico) {
        String nome = "Dr. ";
        String[] nomes = nomeMedico.split(" ");
        if (nomes.length > 0)
            nome += nomes[0];

        nome += " " + nomes[nomes.length - 1];
        return nome;
    }
}
