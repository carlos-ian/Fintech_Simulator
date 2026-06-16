# 🏦 Simulador de Aplicação Bancária

O projeto consiste em uma aplicação bancária simulada desenvolvida em **Java** para a disciplina de **Programação Orientada a Objetos** do curso de **Engenharia de Software**
da **Universidade Federal de Goiás (UFG)**. O sistema permite a criação de clientes, abertura de diferentes tipos de contas, além da realização de operações financeiras, como 
realização de transações, registro e resgate de aplicações/investimentos, visualização de extrato, gestão de cartões etc.

---

## 🚀 Funcionalidades e Requisitos

| ID | Descrição | Módulos |
| :--- | :--- | :--- |
| **RF01** | **Criação de Perfil de Usuário:** O sistema deve permitir o cadastro do usuário com suas informações pessoais, seja cliente ou administrador, ao iniciar a aplicação. | Usuário |
| **RF02** | **Autenticação:** Ao iniciar a aplicação, o sistema deve realizar o login do usuário, seja cliente ou administrador, para autenticá-lo utilizando seu CPF e senha, verificando se as informações estão corretas e se o usuário está autorizado para acessar as funcionalidades daquele perfil. | Usuário |
| **RF03** | **Abertura e Encerramento de Contas:** O sistema deve permitir abrir uma ou mais contas entre os tipos disponíveis (Corrente, Poupança, Investimento e Kids) e excluir/encerrar uma ou mais contas que o cliente possui. | Conta (Cliente) |
| **RF04** | **Visualização da Lista de Contas:** O sistema deve permitir visualizar todas as contas e suas informações que o cliente possui. | Conta (Cliente) |
| **RF05** | **Escolha e Alternação de Contas:** O sistema deve permitir escolher e alternar entre contas que o usuário possui após realizar sua autenticação e antes de entrar no menu principal do cliente. | Conta (Cliente) |
| **RF06** | **Visualização de Dados da Conta:** O sistema deve permitir visualizar os dados completos da conta e o número da conta e saldo no menu principal do cliente. | Conta (Cliente) |
| **RF07** | **Visualização de Perfil:** O sistema deve permitir a exibição dos dados pessoais fornecidos pelo usuário, seja cliente ou administrador, durante o cadastro. | Usuário |
| **RF08** | **Alteração de Perfil:** O sistema deve permitir editar/alterar informações fornecidas pelo usuário, seja cliente ou administrador, durante o cadastro. | Usuário |
| **RF09** | **Desativar/Ativar Perfil:** O sistema deve permitir desativar e ativar o perfil/status do usuário, seja cliente ou administrador, desconectando-o caso desative. | Usuário |
| **RF10** | **Exclusão de Perfil:** O sistema deve permitir excluir o perfil do usuário e suas informações, seja cliente ou administrador, assim como suas contas caso seja cliente e se estiver com saldo igual a 0. | Usuário |
| **RF11** | **Criação e Exclusão de Cartões:** O sistema deve permitir criar e excluir cartões, seja de crédito, débito ou ambos, da lista de cartões do cliente. | Conta |
| **RF12** | **Visualizar Dados do Cartão:** O sistema deve permitir visualizar os dados de um cartão selecionado. | Conta |
| **RF13** | **Bloquear e Desbloquear Cartão:** O sistema deve permitir bloquear e desbloquear um cartão, modificando seu status e controlando suas operações. | Conta |
| **RF14** | **Visualização e Ajuste de Limite:** O sistema deve exibir o limite total e disponível do cartão e deve permitir solicitar o aumento ou diminuição de limite de crédito ao administrador. | Conta, Administrador |
| **RF15** | **Realização de Transações:** O sistema deve processar fluxos de entrada e de saída na conta de clientes utilizando alguma forma de pagamento (Pix, cartão de crédito, cartão de débito) e registrando a transação no extrato de origem e destino. | Conta |
| **RF16** | **Visualização de Extratos:** O sistema deve exibir o extrato da conta mostrando uma lista de transações geral ou filtrada com multi-filtros, como tipo de fluxo (Saída/Entrada), método de pagamento (Pix, Crédito ou Débito), categoria ou períodos (7, 14 ou 30 dias). | Conta |
| **RF17** | **Realização de Investimentos:** O sistema deve permitir realizar um investimento disponível na lista de investimentos disponíveis, inserindo-o na lista de investimentos realizados pelo cliente e realizando seu rendimento em uma data fixa todo mês. | Investimento |
| **RF18** | **Resgate de Investimentos:** O sistema deve permitir resgatar o dinheiro de um investimento realizado anteriormente pelo cliente, recebendo o lucro no saldo. | Investimento |
| **RF19** | **Visualização de Investimentos:** O sistema deve exibir a lista de todos os investimentos disponíveis e suas informações, assim como os investimentos atuais feitos. | Investimento |
| **RF20** | **Gerenciamento de Clientes:** O sistema deve permitir que o administrador gerencie os clientes da aplicação, realizando consultas e podendo ativar/desativar seu perfil. | Administrador |
| **RF21** | **Gerenciamento de Contas:** O sistema deve permitir que o administrador gerencie as contas dos clientes da aplicação, realizando consultas, visualizando o extrato e podendo ativar/desativar sua conta. | Administrador |
| **RF22** | **Relatório Geral:** O sistema deve permitir gerar o relatório geral com informações financeiras, gestão de usuários e outros dados relevantes. | Administrador |
| **RF23** | **Conta Poupança:** O sistema deve impedir realização de transação com método e cartão de crédito em contas poupança, além de realizar o rendimento da conta todo mês em um dia fixo. | Conta, Tipos de Conta |
| **RF24** | **Conta Investimento:** O sistema deve impedir realização de transação de uma conta investimento para uma outra conta que pertença a outro usuário que não seja o titular dessa conta investimento. | Conta, Tipos de Conta |
| **RF25** | **Conta Kids:** O sistema deve impedir que uma transação ocorra se ela passar do limite total do mês, registrando o total gasto no mês. | Conta, Tipos de Conta |

---

## 🧩 Conceitos de POO e Engenharia de Software Aplicados

### 1. Pilares Clássicos de POO
* **Encapsulamento:** Como boa prática de design, todos os atributos sensíveis (como `saldo`, `senha` e `limite`) utilizam o modificador de acesso `private`, sendo expostos controladamente via *getters* e *setters*. Para permitir que as subclasses acessem diretamente os atributos estruturais da árvore de herança sem quebrar o encapsulamento para classes externas, utilizou-se o modificador `protected` nas superclasses.
* **Atributos Estáticos (`static`):** Aplicados nas classes `Usuario`, `Conta`, `Investimento` e `Transacao` para gerenciar contadores globais em memória. Isso garante a geração incremental e automatizada de IDs e códigos identificadores únicos para cada nova instância gerada.
* **Classes Abstratas:** As classes `Usuario` e `Conta` foram definidas com o modificador `abstract`. Elas funcionam estritamente como moldes conceituais e contratos arquiteturais. O sistema impede terminantemente a criação de um usuário ou conta "genéricos", exigindo que eles sejam materializados através de suas especializações reais.
* **Herança:** Organizada em duas grandes ramificações para reaproveitamento de código e padronização:
  * `Usuario` baseia a existência de `Cliente` e `Administrador`.
  * `Conta` estende sua estrutura para `ContaCorrente`, `ContaPoupanca`, `ContaKids` e `ContaInvestimento`.
* **Métodos Abstratos:** A classe `Conta` dita o comportamento obrigatório de movimentação financeira através do método abstrato `public abstract void realizarTransacao(...)`. Como cada modelo de conta possui regras fiscais, limites de segurança e restrições de idade distintas, a implementação do método foi completamente delegada às classes filhas.
* **Polimorfismo:** Explorou-se o polimorfismo de sobreposição de duas formas principais:
  * *Nos Perfis:* Os métodos `visualizarDados()` e `alterarDados()` possuem implementações especializadas em `Cliente` e `Administrador`, permitindo manipular as informações adicionais exclusivas que cada papel exige no sistema.
  * *Nas Contas:* Os métodos `realizarTransacao()` e `visualizarDadosConta()` reagem dinamicamente em tempo de execução dependendo de qual subclasse específica de `Conta` está invocando a ação.

### 2. Manipulação de Listas (Collections)
Uso massivo da API de Collections do Java através de estruturas `ArrayList` para o gerenciamento dinâmico em memória de listas de usuários, transações, contas, cartões e investimentos. A manipulação dessas coleções ocorre através de métodos nativos especializados, tais como .add() e .remove().

### 3. Controle e Tratamento de Exceções
Implementação de uma arquitetura robusta de tratamento de erros orientada a objetos para blindar a aplicação principal contra comportamentos inesperados. Foram criadas três classes de exceções personalizadas (*Custom Exceptions*) que estendem `Exception`:
* `ContaInativaException`: Disparada ao tentar operar contas desativadas pelo administrador.
* `SaldoInsuficienteException`: Lançada ao tentar efetuar saques ou transferências acima do saldo disponível.
* `LimiteInsuficienteException`: Utilizada quando transações em cartões ou contas especiais ultrapassam as barreiras de crédito pré-aprovadas ou os tetos de gastos mensais (como na Conta Kids).

### 4. Persistência de Dados (Banco de Dados)
O armazenamento de longo prazo é realizado em um banco de dados relacional **PostgreSQL**, utilizando a especificação **JDBC** (Java Database Connectivity) pura para garantir o entendimento íntimo da comunicação com o banco. O padrão Repository foi adotado para isolar as queries SQL da lógica de negócio através das classes:
* `ConexaoBanco`: Gerencia o ciclo de vida do driver e a abertura/fechamento das conexões.
* `UsuarioBancoRepository`, `ContaBancoRepository`, `TransacaoBancoRepository`, `CartaoBancoRepository` e `InvestimentoBancoRepository`: Responsáveis pelas instruções de CRUD (Create, Read, Update, Delete) e mapeamento dos resultados SQL para objetos Java.

### 5. Manipulação de Arquivos e Relatórios
O sistema consolida as informações financeiras de gestão armazenadas para exportar o **Relatório Geral da Fintech** diretamente em arquivos de formato texto, permitindo auditoria externa pelo módulo do Administrador.

### 6. Interface Gráfica (UI)
* **JOptionPane:** Utilizado de forma rápida e direta na classe de aplicação principal para caixas de diálogos simples, alertas de erro e confirmações rápidas do sistema.
* **Java Swing & SwingUtil:** Para telas mais densas (como formulários de cadastro, menus de transação e painéis de controle do Administrador), utilizou-se o framework Swing. Foi desenvolvida uma classe utilitária chamada `SwingUtil` com métodos estáticos de padronização, responsável por injetar propriedades visuais idênticas, regras de dimensões e templates pré-configurados para menus de seleção e caixas de formulários, garantindo consistência visual em toda a experiência do usuário.

### 7. Testes Automatizados e Qualidade
* **JUnit:** Criação de uma suite abrangente de casos de testes funcionais automatizados para garantir o funcionamento correto das regras mais críticas do sistema (cálculos de rendimento da poupança, validações de limite de conta Kids, e fluxos de exceção de saldo).
* **Javadoc:** Todo o código-fonte foi rigorosamente documentado utilizando as tags do padrão Javadoc (`@param`, `@return`, `@throws`), permitindo a geração automática da documentação do sistema em páginas HTML.
* **Artefato Executável (.jar):** Configuração do ciclo de build para a compilação do projeto em um arquivo compilado independente de extensão `.jar`, facilitando a portabilidade e distribuição do simulador.

---

## 📁 Estrutura de Pastas do Projeto

O projeto segue estritamente a seguinte distribuição de diretórios e pacotes estruturados no ambiente de desenvolvimento:

```text
Fintech_Simulator/
├── Classes/                      # Diretório raíz do código-fonte Java
│   ├── Exceptions/               # Classes de exceções customizadas do sistema
│   │   ├── ContaInativaException.java
│   │   ├── LimiteInsuficienteException.java
│   │   └── SaldoInsuficienteException.java
│   ├── Model/                    # Núcleo do domínio e regras de negócio de POO
│   │   ├── Conta/                # Subclasses e regras para Conta, Corrente, Poupança, etc.
│   │   ├── Operacoes/            # Classes vinculadas a Transações, Investimentos, Cartões etc.
│   │   └── Usuario/              # Classes para perfis de Usuário (Clientes e Administradores)
│   ├── Util/                     # Classes de utilitários, auxílio à interface gráfica e banco de dados
│   └── AplicacaoBancaria.java    # Classe Executável Principal (Ponto de entrada do sistema)
│
├── Diagramas/                    # Modelagens e documentação visual arquitetural
    ├── Diagrama de Classes Conceituais
    ├── Diagramas de Casos de Uso (DCdu)
    └── Diagramas de Classes (DCla)
```

## 🛠️ Tecnologias Utilizadas
O projeto foi construído utilizando um ecossistema nativo Java focado em performance, robustez e isolamento de responsabilidades:

Linguagem Principal: Java 17, explorando recursos modernos da linguagem.

Interface Gráfica Desktop: Java Swing (via SwingUtil) e JOptionPane.

Persistência de Dados Relacional: PostgreSQL e JDBC API pura (padrão Repository).

Automação de Build e Dependências: Maven (pom.xml).

Testes Automatizados: JUnit 5.

Segurança e Configuração: Arquivo .env para isolamento de credenciais do banco.

Documentação: Javadoc.

## ⚙️ Como Executar o Projeto
Siga os passos abaixo para configurar o ambiente e rodar o simulador bancário na sua máquina local:

1. Pré-requisitos
Antes de começar, você precisará ter instalado em sua máquina:

Java Development Kit (JDK) 17 ou superior.

IDE IntelliJ IDEA ou Eclipse com suporte a projetos Maven.

Banco de dados PostgreSQL ativo e rodando.

2. Configuração do Banco de Dados
Abra o seu gerenciador do PostgreSQL e crie um novo banco de dados chamado fintech_simulator.

Execute os scripts SQL de criação de tabelas.

Na raiz do projeto, certifique-se de configurar o arquivo .env com as suas credenciais locais:

DB_URL=jdbc:postgresql://localhost:5432/fintech_simulator

DB_USER=seu_usuario

DB_PASSWORD=sua_senha

3. Compilação e Instalação de Dependências (Maven)
Abra o IntelliJ IDEA e importe a pasta raiz Fintech_Simulator como um projeto Maven.

Aguarde a IDE baixar todas as dependências contidas no pom.xml.

Se preferir rodar via terminal, execute: mvn clean install

4. Inicializando a Aplicação
No painel de navegação do seu projeto, expanda a pasta Classes.

Localize o arquivo AplicacaoBancaria.java.

Clique com o botão direito sobre ele e selecione a opção Run 'AplicacaoBancaria.main()'.

## ✒️ Autores
Brenno Soares de Aguiar
Davi Alves Mugayar
Ian Carlos Lima Tavares
Joel Antônio Rezende Espíndola
