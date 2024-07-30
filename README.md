# Sistema de Chamados (Help Desk)

Projeto consiste em um Sistema de Help Desk, intuitivo, eficiente e acessível tanto para usuários quanto para administradores. Os requisitos incluíam um serviço de login, gerenciamento de usuários (cliente e administrador), um serviço para gerenciar tickets e trâmites, e um serviço de e-mails para notificar o cliente sobre cada trâmite. Escolhi utilizar uma arquitetura de microsserviços e mais de um banco de dados, tornando este projeto um desafio ideal para aplicar meus estudos pessoais.

### Objetivo

Desenvolver um sistema de chamados simples com login, cliente, administrador,  chamados, historico de chamados e envios de e-mails.

### **Requisitos**

- Serviço de Login
- Serviço de User
- Serviço de Adm
- Serviço de Chamados
- Serviço de Email

### **Requisitos Funcionais**

1. **Autenticação:**
    - Serviço de cadastro de usuários (cliente e administrador).
    - Serviço de login.
2. **Gerenciamento de Usuários:**
    - Serviço para cliente.
    - Serviço para administrador.
3. **Chamados:**
    - Serviço para criar um novo chamado.
    - Serviço para listar todos os chamados.
    - Serviço para listar chamados específicos de um cliente.
    - Serviço para atribuir um chamado a um administrador.
    - Serviço para responder a um chamado (mensagem).
4. **Histórico de Mensagens:**
    - Serviço para adicionar mensagem ao histórico de um chamado.
    - Serviço para recuperar o histórico de mensagens de um chamado.
5. **Envio de E-mails:**
    - Serviço para enviar e-mails de notificação para o cliente em cada atualização do chamado.
6. **Administração:**
    - Interface administrativa para visualizar e gerenciar todos os chamados.
    - Ferramenta para atribuir chamados a administradores.
