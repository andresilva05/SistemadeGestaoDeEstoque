# Sistema de Gestão de Estoque

Um sistema completo para controle de estoque, clientes e vendas desenvolvido em Java, seguindo os princípios da Programação Orientada a Objetos.

## 📋 Sobre o Projeto

Este projeto simula um sistema de gestão para uma loja, permitindo o gerenciamento completo de produtos, clientes (pessoa física e jurídica) e vendas. O sistema foi desenvolvido como projeto acadêmico para aplicar conceitos avançados de POO.

## 🚀 Funcionalidades

### 📦 Módulo Produtos
- ✅ Cadastro de produtos com ID automático
- ✅ Validação contra nomes duplicados
- ✅ Controle de estoque com atualizações
- ✅ Busca por ID ou nome parcial
- ✅ Cálculo do valor total do estoque
- ✅ Relatório de produtos com estoque baixo
- ✅ Exclusão lógica (não permite excluir produtos com vendas)

### 👥 Módulo Clientes
- ✅ Cadastro de Clientes PF (CPF validado)
- ✅ Cadastro de Clientes PJ (CNPJ validado)
- ✅ Validação de documentos (CPF/CNPJ)
- ✅ Listagem com contagem de compras
- ✅ Exclusão lógica (não permite excluir clientes com compras)
- ✅ Formatação automática de documentos

### 💰 Módulo Vendas
- ✅ Registro de vendas com validações
- ✅ Verificação de estoque disponível
- ✅ Baixa automática no estoque
- ✅ Cálculo automático de valor total
- ✅ Cancelamento de vendas com estorno de estoque
- ✅ Relatórios por cliente ou produto
- ✅ Controle de histórico de compras

## 🛠️ Tecnologias e Conceitos Utilizados

- **Java 8+**
- **Programação Orientada a Objetos** (POO)
- **Abstração** (Classes abstratas)
- **Encapsulamento** (Getters/Setters)
- **Herança** (ClientePF e ClientePJ)
- **Interfaces** (Padrão Repository)
- **Generics** (Repositórios genéricos)
- **Validações** (CPF/CNPJ)
- **BigDecimal** (Precisão monetária)
- **Stream API** (Manipulação de coleções)
- **Swing** (Interface gráfica simples)

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

📁 Models (Entidades)
├── Cliente (abstrata)
├── ClientePF
├── ClientePJ
├── Produto
└── Venda

📁 Repositories (Persistência)
├── Interfaces
└── Implementações

📁 Services (Lógica de Negócio)
├── ClienteService
├── ProdutoService
└── VendaService

📁 Util (Utilitários)
├── CPFValidator
└── CNPJValidator


## 📦 Instalação e Execução

### Pré-requisitos
- Java JDK 8 ou superior
- IDE Java (Eclipse, IntelliJ, VS Code) ou terminal

### Como executar
1. Clone o repositório ou baixe os arquivos
2. Abra o projeto em sua IDE Java
3. Localize e execute a classe `Main.java`
4. Siga as instruções no menu interativo

## 📝 Funcionalidades Técnicas Implementadas

1. **Validação de Documentos**: Algoritmos oficiais de validação de CPF e CNPJ
2. **Exclusão Lógica**: Registros nunca são apagados, apenas marcados como inativos
3. **ID Automático**: Sistema sequencial de IDs únicos
4. **Integridade Referencial**: Não permite exclusão de registros com dependências
5. **Formatação Automática**: CPF/CNPJ formatados para exibição
6. **Tratamento Monetário**: BigDecimal para evitar problemas de arredondamento

## 👥 Desenvolvimento

Projeto desenvolvido em dupla como parte da disciplina de Programação Orientada a Objetos, aplicando todos os conceitos estudados durante o semestre.

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---
*Sistema desenvolvido como projeto final da disciplina de POO*
