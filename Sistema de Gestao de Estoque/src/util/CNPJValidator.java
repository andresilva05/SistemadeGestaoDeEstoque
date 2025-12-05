package util;

public class CNPJValidator {

    public static boolean isValid(String cnpj) {

        if (cnpj == null) return false;

        // Remove tudo que não for número
        String cnpjLimpo = cnpj.replaceAll("\\D", "").trim();

        // Deve ter 14 dígitos
        if (cnpjLimpo.length() != 14) {
            return false;
        }

        // Verifica se todos dígitos são iguais
        char primeiro = cnpjLimpo.charAt(0);
        boolean todosIguais = true;

        for (int i = 1; i < 14; i++) {
            if (cnpjLimpo.charAt(i) != primeiro) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) return false;

        // -----------------------------
        // CÁLCULO DO PRIMEIRO DV (posição 12)
        // -----------------------------
        int soma = 0;
        int peso = 5;

        for (int i = 0; i < 12; i++) {
            int numero = cnpjLimpo.charAt(i) - '0';
            soma += numero * peso;

            peso--;
            if (peso < 2) peso = 9;
        }

        int dv1 = soma % 11;
        if (dv1 < 2) dv1 = 0;
        else dv1 = 11 - dv1;

        int dig12 = cnpjLimpo.charAt(12) - '0';
        if (dv1 != dig12) {
            return false;
        }

        // -----------------------------
        // CÁLCULO DO SEGUNDO DV (posição 13)
        // -----------------------------
        int soma2 = 0;
        int peso2 = 6;

        for (int i = 0; i < 13; i++) {
            int numero = cnpjLimpo.charAt(i) - '0';
            soma2 += numero * peso2;

            peso2--;
            if (peso2 < 2) peso2 = 9;
        }

        int dv2 = soma2 % 11;
        if (dv2 < 2) dv2 = 0;
        else dv2 = 11 - dv2;

        int dig13 = cnpjLimpo.charAt(13) - '0';
        if (dv2 != dig13) {
            return false;
        }

        return true;
    }
}
