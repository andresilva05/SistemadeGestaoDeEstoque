package util;

// Classe utilitária para validar números de CPF (Cadastro de Pessoa Física)
public class CPFValidator {

    // Método estático que valida um CPF
    public static boolean isValid(String cpf) {
        // Remove tudo que não for número (pontuação, traços, espaços)
        // \\D é uma expressão regular que significa "qualquer coisa que não seja dígito"
        String cpfLimpo = cpf.replaceAll("\\D", "").trim();

        // Primeira validação: verifica se o CPF não é nulo
        if (cpf == null) return false;

        // Valida quantidade de números informados: CPF deve ter exatamente 11 dígitos
        if (cpfLimpo.length() != 11) {
            return false;
        }

        // Valida dígitos iguais: CPFs como "111.111.111-11" são inválidos
        char primeiro = cpfLimpo.charAt(0); // Pega o primeiro caractere
        boolean todosIguais = true; // Flag para verificar igualdade

        // Percorre todos os caracteres do CPF
        for (int i = 1; i < cpfLimpo.length(); i++) {
            if (cpfLimpo.charAt(i) != primeiro) {
                todosIguais = false; // Encontrou um dígito diferente
                break; // Sai do loop
            }
        }
        if (todosIguais == true) {
            return false; // Se todos forem iguais, CPF inválido
        }

        // -----------------------------
        // CÁLCULO DO PRIMEIRO DÍGITO VERIFICADOR (DV1)
        // O primeiro DV está na 10ª posição (índice 9 em arrays)
        // -----------------------------
        int soma = 0; // Variável para acumular a soma ponderada

        // Loop para os primeiros 9 dígitos (posições 0 a 8)
        for (int i = 0; i <= 8; i++) {
            // Converte caractere para número inteiro
            // '0' tem valor 48 em ASCII, então subtraímos '0' para obter o valor numérico
            int numero = cpfLimpo.charAt(i) - '0';

            // Peso para cada posição: 10, 9, 8, 7, 6, 5, 4, 3, 2
            int peso = 10 - i;

            // Multiplica o dígito pelo peso
            int multDigito = numero * peso;

            // Soma ao total
            soma += multDigito;
        }

        // Calcula o primeiro dígito verificador
        int dv1;
        dv1 = (soma * 10) % 11; // Multiplica soma por 10 e pega o resto da divisão por 11

        if (dv1 == 10) {
            dv1 = 0; // Se resultado for 10, dígito verificador é 0
        }

        // Obtém o 10º dígito do CPF (que é o primeiro dígito verificador informado)
        int dig9 = cpfLimpo.charAt(9) - '0';

        // Compara o dígito calculado com o dígito informado
        if (dv1 != dig9) {
            return false; // Se não bater, CPF inválido
        }

        // -----------------------------
        // CÁLCULO DO SEGUNDO DÍGITO VERIFICADOR (DV2)
        // O segundo DV está na 11ª posição (índice 10 em arrays)
        // -----------------------------
        int soma2 = 0; // Nova soma para o segundo dígito

        // Loop para os primeiros 10 dígitos (agora incluindo o primeiro DV)
        for (int i = 0; i <= 9; i++) {
            int numero = cpfLimpo.charAt(i) - '0';

            // Peso para cada posição: 11, 10, 9, 8, 7, 6, 5, 4, 3, 2
            int peso = 11 - i;

            int multDigito = numero * peso;

            soma2 += multDigito;
        }

        // Calcula o segundo dígito verificador
        int dv2;
        dv2 = (soma2 * 10) % 11; // Multiplica soma por 10 e pega o resto da divisão por 11

        if (dv2 == 10) {
            dv2 = 0; // Se resultado for 10, dígito verificador é 0
        }

        // Obtém o 11º dígito do CPF (que é o segundo dígito verificador informado)
        int dig10 = cpfLimpo.charAt(10) - '0';

        // Compara o dígito calculado com o dígito informado
        if (dv2 != dig10) {
            return false; // Se não bater, CPF inválido
        }

        // Se passou por todas as validações, CPF é válido
        return true;
    }
}