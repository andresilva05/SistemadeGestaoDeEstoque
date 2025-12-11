package util;

// Classe utilitária para validar números de CNPJ (Cadastro Nacional da Pessoa Jurídica)
public class CNPJValidator {

    // Método estático que valida um CNPJ
    public static boolean isValid(String cnpj) {

        // Primeira validação: verifica se o CNPJ não é nulo
        if (cnpj == null) return false;

        // Remove tudo que não for número (pontuação, traços, barras, espaços)
        // \\D é uma expressão regular que significa "qualquer coisa que não seja dígito"
        String cnpjLimpo = cnpj.replaceAll("\\D", "").trim();

        // Segunda validação: CNPJ deve ter exatamente 14 dígitos
        if (cnpjLimpo.length() != 14) {
            return false;
        }

        // Terceira validação: verifica se todos os dígitos são iguais
        // CNPJs como "00.000.000/0000-00" são inválidos
        char primeiro = cnpjLimpo.charAt(0); // Pega o primeiro caractere
        boolean todosIguais = true; // Flag para verificar igualdade

        // Percorre todos os caracteres do CNPJ
        for (int i = 1; i < 14; i++) {
            if (cnpjLimpo.charAt(i) != primeiro) {
                todosIguais = false; // Encontrou um dígito diferente
                break; // Sai do loop
            }
        }
        if (todosIguais) return false; // Se todos forem iguais, CNPJ inválido

        // -----------------------------
        // CÁLCULO DO PRIMEIRO DÍGITO VERIFICADOR (posição 12)
        // O primeiro DV está na 13ª posição (índice 12 em arrays)
        // -----------------------------
        int soma = 0;  // Variável para acumular a soma ponderada
        int peso = 5;  // Peso inicial para o primeiro dígito do cálculo

        // Loop para os primeiros 12 dígitos (posições 0 a 11)
        for (int i = 0; i < 12; i++) {
            // Converte caractere para número inteiro
            // '0' tem valor 48 em ASCII, então subtraímos '0' para obter o valor numérico
            int numero = cnpjLimpo.charAt(i) - '0';

            // Multiplica o dígito pelo peso e soma ao total
            soma += numero * peso;

            // Decrementa o peso seguindo a sequência: 5,4,3,2,9,8,7,6,5,4,3,2
            peso--;
            if (peso < 2) peso = 9; // Quando chega em 1, volta para 9
        }

        // Calcula o primeiro dígito verificador
        int dv1 = soma % 11; // Resto da divisão por 11
        if (dv1 < 2) dv1 = 0; // Se resto for 0 ou 1, dígito verificador é 0
        else dv1 = 11 - dv1; // Senão, 11 menos o resto

        // Obtém o 13º dígito do CNPJ (que é o primeiro dígito verificador informado)
        int dig12 = cnpjLimpo.charAt(12) - '0';

        // Compara o dígito calculado com o dígito informado
        if (dv1 != dig12) {
            return false; // Se não bater, CNPJ inválido
        }

        // -----------------------------
        // CÁLCULO DO SEGUNDO DÍGITO VERIFICADOR (posição 13)
        // O segundo DV está na 14ª posição (índice 13 em arrays)
        // -----------------------------
        int soma2 = 0; // Nova soma para o segundo dígito
        int peso2 = 6; // Peso inicial diferente para o segundo cálculo

        // Loop para os primeiros 13 dígitos (agora incluindo o primeiro DV)
        for (int i = 0; i < 13; i++) {
            int numero = cnpjLimpo.charAt(i) - '0';
            soma2 += numero * peso2;

            // Decrementa o peso seguindo a sequência: 6,5,4,3,2,9,8,7,6,5,4,3,2
            peso2--;
            if (peso2 < 2) peso2 = 9; // Quando chega em 1, volta para 9
        }

        // Calcula o segundo dígito verificador
        int dv2 = soma2 % 11; // Resto da divisão por 11
        if (dv2 < 2) dv2 = 0; // Se resto for 0 ou 1, dígito verificador é 0
        else dv2 = 11 - dv2; // Senão, 11 menos o resto

        // Obtém o 14º dígito do CNPJ (que é o segundo dígito verificador informado)
        int dig13 = cnpjLimpo.charAt(13) - '0';

        // Compara o dígito calculado com o dígito informado
        if (dv2 != dig13) {
            return false; // Se não bater, CNPJ inválido
        }

        // Se passou por todas as validações, CNPJ é válido
        return true;
    }
}