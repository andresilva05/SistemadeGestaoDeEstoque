package util;

public class CPFValidator {

    public static boolean isValid(String cpf) {
        String cpfLimpo = cpf.replaceAll("\\D", "").trim();

        if (cpf == null) return false;

        //Valida quantidade de numeros informados
        if (cpfLimpo.length() != 11) {
            return false;
        }

        //Valida digitos iguais
        char primeiro = cpfLimpo.charAt(0);
        boolean todosIguais = true;
        for (int i = 1; i < cpfLimpo.length(); i++) {

            if (cpfLimpo.charAt(i) != primeiro) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais == true) {
            return false;
        }

        //Calcula o primeiro digito verificador (DV1)
        int soma = 0;

        for (int i = 0; i <= 8; i++) {

            int numero = cpfLimpo.charAt(i) - '0';

            int peso = 10 - i;

            int multDigito = numero * peso;

            soma += multDigito;

        }
        int dv1;

        dv1 = (soma * 10) % 11;

        if (dv1 == 10) {
            dv1 = 0;
        }
        int dig9 = cpfLimpo.charAt(9) - '0';

        if (dv1 != dig9) {
            return false;
        }

        //Calcular o SEGUNDO dígito verificador (DV2)
        int soma2 = 0;
        for (int i = 0; i <= 9; i++) {

            int numero = cpfLimpo.charAt(i) - '0';

            int peso = 11 - i;

            int multDigito = numero * peso;

            soma2 += multDigito;

        }
        int dv2;

        dv2 = (soma2 * 10) % 11;

        if (dv2 == 10) {
            dv2 = 0;
        }
        int dig10 = cpfLimpo.charAt(10) - '0';

        if (dv2 != dig10) {
            return false;
        }
        return true;
    }
}
