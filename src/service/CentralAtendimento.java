package service;

import java.util.Scanner;

import estruturas.FilaEncadeada;
import estruturas.ListaDupla;
import estruturas.ListaSimples;
import estruturas.NoDuplo;
import estruturas.Node;
import estruturas.PilhaEncadeada;
import model.Operacao;
import model.Solicitacao;
import ui.Menu;

public class CentralAtendimento {
    private FilaEncadeada<Solicitacao> filaEsperaP1;
    private FilaEncadeada<Solicitacao> filaEsperaP2;
    private PilhaEncadeada<Operacao> historicoOperacoes;
    private int contadorAtendimentos = 0;
    private Solicitacao solicitacaoAtual;
    private ListaDupla<Solicitacao> historicoAtendimento;
    private ListaSimples<Solicitacao> cadastroGeral;

    public CentralAtendimento() {
        this.filaEsperaP1 = new FilaEncadeada<Solicitacao>();
        this.filaEsperaP2 = new FilaEncadeada<Solicitacao>();
        this.historicoOperacoes = new PilhaEncadeada<Operacao>();
        this.historicoAtendimento = new ListaDupla<Solicitacao>();
        this.cadastroGeral = new ListaSimples<Solicitacao>();
        this.contadorAtendimentos = 0;

    }
    public boolean quemAtender() {
        if (filaEsperaP1.qIsEmpty()) {
            return false;
        }
         else if (filaEsperaP2.qIsEmpty()) {
            return true; // P2 vazia: atende P1 mesmo que o contador indique P2
        }
         else if (contadorAtendimentos == 2) {
            return false;
        }
        return true;
    }
    public void addAtender() {
         contadorAtendimentos++;
         contadorAtendimentos = contadorAtendimentos % 3;
    }

    public void cadastrarSolicitacao(Solicitacao sol) {
        if (sol.getPrioridade() != 1 && sol.getPrioridade() != 2) {
            System.out.println("\nPrioridade inválida (use 1 ou 2). Solicitação não cadastrada.");
            return;
        }
        if (cadastroGeral.search(sol) != null) {
            System.out.println("\nJá existe uma solicitação com o código #" + sol.getCodigo() + ".");
            return;
        }
        try {
            // Nova solicitação -> cadastro geral (lista simples) -> fila de espera
            cadastroGeral.insertTail(sol);
            if (sol.getPrioridade() == 1){
                filaEsperaP1.enqueue(sol);
            } else if (sol.getPrioridade() == 2){
                filaEsperaP2.enqueue(sol);
            }
            historicoOperacoes.push(new Operacao("CADASTRO", sol));
            System.out.println("Solicitação cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar solicitação: " + e.getMessage());
        }
    }

    public void consultarProxima() {
        try {

            if (quemAtender()) {
                Solicitacao proxima = filaEsperaP1.front();
                System.out.println("--- PRÓXIMA SOLICITAÇÃO PRIORIDADE 1 ---");
                System.out.println(proxima);
            } else {
                Solicitacao proxima = filaEsperaP2.front();
                System.out.println("--- PRÓXIMA SOLICITAÇÃO PRIORIDADE 2 ---");
                System.out.println(proxima);
            }
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
        }
    }

    public void atenderProxima(String responsavel) {
        Solicitacao atendi;
        // O ciclo 2:1 só conta quando as duas filas têm solicitações esperando (há disputa).
        // Se uma fila está vazia, atender a outra não consome o ciclo.
        boolean disputa = !filaEsperaP1.qIsEmpty() && !filaEsperaP2.qIsEmpty();
        try {

            if (quemAtender()) {
                atendi = filaEsperaP1.dequeue();
            } else {
                atendi = filaEsperaP2.dequeue();
            }
            if (disputa) {
                addAtender();
            }
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
            return;
        }

        try {
            if (solicitacaoAtual != null) {
                adicionarAoHistoricoAtendimento(solicitacaoAtual);
                historicoOperacoes.push(new Operacao("CONCLUIDO", solicitacaoAtual));
            }
            atendi.setStatus("EM_ATENDIMENTO");
            atendi.setResponsavel(responsavel);
            solicitacaoAtual = atendi;
            historicoOperacoes.push(new Operacao("EM_ATENDIMENTO", atendi));
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico do atendimento: " + e.getMessage());
        }
    }

    public void exibirFila() {
        System.out.println("\n========================================");
        System.out.println("FILA DE SOLICITAÇÕES EM ESPERA");
        System.out.println("========================================");

        System.out.println("\n--- PRIORIDADE 1 ---");
        if (filaEsperaP1.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 1.");
        } else {
            System.out.println(filaEsperaP1);
        }

        System.out.println("\n--- PRIORIDADE 2 ---");
        if (filaEsperaP2.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 2.");
        } else {
            System.out.println(filaEsperaP2);
        }
    }

    public void exibirQuantidadeFila() {    
        try {
        int total = filaEsperaP1.totalElementos() + filaEsperaP2.totalElementos();
        System.out.println("Quantidade total de solicitações aguardando na fila: " + total);
        System.out.println("Quantidade de solicitações na fila de prioridade 1: " + filaEsperaP1.totalElementos());
        System.out.println("Quantidade de solicitações na fila de prioridade 2: " + filaEsperaP2.totalElementos());
        } catch (Exception e) {
            System.out.println("Erro ao exibir quantidade de solicitações na fila: " + e.getMessage());
        }
    }

    public void consultarUltimaOperacao() {
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico.");
            return;
        }
        try {
            Operacao ultima = historicoOperacoes.topo();
            System.out.println("--- ÚLTIMA OPERAÇÃO REALIZADA ---");
            System.out.println(ultima);
        } catch (Exception e) {
            System.out.println("Erro ao consultar histórico: " + e.getMessage());
        }
    }

    public void conluirAtendimento() {
        if (solicitacaoAtual == null) {
            System.out.println("Não há atendimento em andamento.");
            return;
        }
        try {
            adicionarAoHistoricoAtendimento(solicitacaoAtual);
            historicoOperacoes.push(new Operacao("CONCLUIDO", solicitacaoAtual));
            solicitacaoAtual = null;
            System.out.println("Atendimento concluído com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico do atendimento concluído: " + e.getMessage());
        }
    }

    private void adicionarAoHistoricoAtendimento(Solicitacao solicitacao) {
        solicitacao.setStatus("CONCLUIDA");
        if (!historicoAtendimento.insertAscending(solicitacao)) {
            throw new IllegalStateException(
                "Não foi possível adicionar a solicitação ao histórico de atendimentos.");
        }
    }

    public void exibirHistoricoRecente() {
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico.");
            return;
        }
        PilhaEncadeada<Operacao> pilhaAux = new PilhaEncadeada<Operacao>();
        int totalNaPilha = historicoOperacoes.sizeElements();
        System.out.println("--- HISTÓRICO RECENTE (do topo para a base: mais recente primeiro) ---");
        System.out.println("Exibindo " + Math.min(10, totalNaPilha) + " de " + totalNaPilha + " operações registradas.");
        // Cada pop devolve a operação mais recente que ainda não foi exibida;
        // a pilha auxiliar guarda as operações para devolvê-las à pilha original.
        for (int i=0; i<10 && !historicoOperacoes.isEmpty();i++){
            try {
                Operacao op = historicoOperacoes.pop();
                System.out.print((i == 0 ? "Topo -> " : "        ") + op);
                pilhaAux.push(op);
            } catch (Exception e) {
                break;
            }
        }
        while (!pilhaAux.isEmpty()) {
            try {
                historicoOperacoes.push(pilhaAux.pop());
            } catch (Exception e) {
                break;
            }
        }
    }

    public void exibirHistoricoAtendimentos(boolean ordemCrescente) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        if (ordemCrescente) {
            System.out.println("--- ATENDIMENTOS CONCLUÍDOS: PRIMEIRO AO ÚLTIMO ---");
            historicoAtendimento.showAscending();
        } else {
            System.out.println("--- ATENDIMENTOS CONCLUÍDOS: ÚLTIMO AO PRIMEIRO ---");
            historicoAtendimento.showDescending();
        }
        System.out.println();
    }
    public void navegarHistoricoAtendimentos(Scanner scanner) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        NoDuplo<Solicitacao> atual = historicoAtendimento.getHead();
        while (true) {
            Solicitacao solicitacao = atual.getData();
            System.out.println("\nAtendimento atual: " + solicitacao.getCodigo());
            System.out.println(solicitacao);
            System.out.println("[A] Anterior");
            System.out.println("[P] Próximo");
            System.out.println("[S] Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                return;
            }

            String opcao = scanner.nextLine().trim();
            if (opcao.equalsIgnoreCase("S")) {
                return;
            } else if (opcao.equalsIgnoreCase("A")) {
                if (atual.getLeft() == null) {
                    System.out.println("Este é o primeiro atendimento do histórico.");
                } else {
                    atual = atual.getLeft();
                }
            } else if (opcao.equalsIgnoreCase("P")) {
                if (atual.getRight() == null) {
                    System.out.println("Este é o último atendimento do histórico.");
                } else {
                    atual = atual.getRight();
                }
            } else {
                System.out.println("Opção inválida. Digite A, P ou S.");
            }
        }
    }

    public void pesquisarAtendimento(int codigo) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        Solicitacao busca = new Solicitacao();
        busca.setCodigo(codigo);
        int posicao = historicoAtendimento.searchAscending(busca);
        if (posicao == -1) {
            System.out.println("Atendimento #" + codigo + " não encontrado no histórico.");
            return;
        }

        System.out.println("--- ATENDIMENTO ENCONTRADO ---");
        System.out.println(historicoAtendimento.get(posicao).getData());
    }
        
    public void desfazerUltimaOperacao() {
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico para desfazer.");
            return;
        }

        Operacao ultimaOp;
        try {
            ultimaOp = historicoOperacoes.topo();
        } catch (Exception e) {
            System.out.println("Não há operações no histórico para desfazer.");
            return;
        }
        if (!ultimaOp.getTipo().equalsIgnoreCase("EM_ATENDIMENTO")) {
            System.out.println("Operação do topo (" + ultimaOp.getTipo() + ") não é do tipo 'EM_ATENDIMENTO'. Não é possível desfazer!");
            return;
        }
        Solicitacao sol = ultimaOp.getSolicitacao();
        FilaEncadeada<Solicitacao> filaPrioridade;
        // A fila de destino é a da prioridade da própria solicitação
        if (sol.getPrioridade() == 1) {
            filaPrioridade = filaEsperaP1;
        } else if (sol.getPrioridade() == 2) {
            filaPrioridade = filaEsperaP2;
        } else {
            System.out.println("Não foi possível desfazer: prioridade da solicitação inválida.");
            return;
        }

        try {
            int elementosExistentes = filaPrioridade.totalElementos();
            sol.setStatus("AGUARDANDO");
            sol.setResponsavel("Não atribuído"); // volta ao valor inicial: ninguém atende mais
            filaPrioridade.enqueue(sol);
            for (int i = 0; i < elementosExistentes; i++) {
                filaPrioridade.enqueue(filaPrioridade.dequeue());
            }
        } catch (Exception e) {
            System.out.println("Erro ao recolocar a solicitação na fila: " + e.getMessage());
            return;
        }

        try {
            historicoOperacoes.push(new Operacao("AGUARDANDO", sol));
        } catch (Exception e) {
            System.out.println("Erro ao registrar a volta da solicitação para 'AGUARDANDO': " + e.getMessage());
            return;
        }

        if (solicitacaoAtual == sol) {
            solicitacaoAtual = null;
        }
        // Só volta o contador se aquele atendimento o avançou, ou seja, se havia disputa.
        // Com a solicitação de volta na fila, as duas filas só têm gente se havia disputa.
        if (!filaEsperaP1.qIsEmpty() && !filaEsperaP2.qIsEmpty()) {
            contadorAtendimentos = (contadorAtendimentos + 2) % 3;
        }
        System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
    }

    private Solicitacao codigoBusca(int codigo) {
        Solicitacao busca = new Solicitacao();
        busca.setCodigo(codigo);
        return busca;
    }

    public void exibirTodasSolicitacoes() {
        if (cadastroGeral.isEmpty()) {
            System.out.println("Não há solicitações cadastradas.");
            return;
        }
        System.out.println("--- CADASTRO GERAL (" + cadastroGeral.getSize() + " solicitações) ---");
        Node<Solicitacao> atual = cadastroGeral.getHead();
        while (atual != null) {
            System.out.println(atual.getDado());
            atual = atual.getProx();
        }
    }

    public void pesquisarSolicitacao(int codigo) {
        if (cadastroGeral.isEmpty()) {
            System.out.println("Não há solicitações cadastradas.");
            return;
        }
        Node<Solicitacao> no = cadastroGeral.search(codigoBusca(codigo));
        if (no == null) {
            System.out.println("Solicitação #" + codigo + " não encontrada no cadastro geral.");
            return;
        }
        System.out.println("--- SOLICITAÇÃO ENCONTRADA ---");
        System.out.println(no.getDado());
    }

    private FilaEncadeada<Solicitacao> removerDaFila(FilaEncadeada<Solicitacao> fila, Solicitacao solicitacao) {
        if (fila == null || fila.qIsEmpty()) {
            return fila;
        }

        FilaEncadeada<Solicitacao> filaAtualizada = new FilaEncadeada<Solicitacao>();
        try {
            while (!fila.qIsEmpty()) {
                Solicitacao atual = fila.dequeue();
                if (!atual.equals(solicitacao)) {
                    filaAtualizada.enqueue(atual);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover a solicitação da fila: " + e.getMessage());
            return fila;
        }
        return filaAtualizada;
    }

    public void removerSolicitacao(int codigo) {
        if (cadastroGeral.isEmpty()) {
            System.out.println("Não há solicitações cadastradas.");
            return;
        }

        Node<Solicitacao> no = cadastroGeral.search(codigoBusca(codigo));
        if (no == null) {
            System.out.println("Solicitação #" + codigo + " não encontrada no cadastro geral.");
            return;
        }

        Solicitacao solicitacao = no.getDado();
        if (solicitacao.getStatus().equalsIgnoreCase("CONCLUIDA")) {
            System.out.println("Não foi possível remover a solicitação #" + codigo + " porque ela já está concluída.");
            return;
        }

        if (!cadastroGeral.remove(solicitacao)) {
            System.out.println("Não foi possível remover a solicitação #" + codigo + " do cadastro geral.");
            return;
        }

        filaEsperaP1 = removerDaFila(filaEsperaP1, solicitacao);
        filaEsperaP2 = removerDaFila(filaEsperaP2, solicitacao);

        if (solicitacaoAtual != null && solicitacaoAtual.equals(solicitacao)) {
            solicitacaoAtual = null;
        }

        solicitacao.setStatus("CANCELADA");
        try {
            historicoOperacoes.push(new Operacao("REMOVIDO", solicitacao));
        } catch (Exception e) {
            System.out.println("Erro ao registrar a remoção no histórico: " + e.getMessage());
        }
        System.out.println("Solicitação #" + codigo + " removida com sucesso.");
    }

    public void exibirPorStatus(String status) {
        if (cadastroGeral.isEmpty()) {
            System.out.println("Não há solicitações cadastradas.");
            return;
        }
        System.out.println("--- SOLICITAÇÕES COM STATUS " + status.toUpperCase() + " ---");
        int encontradas = 0;
        Node<Solicitacao> atual = cadastroGeral.getHead();
        while (atual != null) {
            if (atual.getDado().getStatus().equalsIgnoreCase(status)) {
                System.out.println(atual.getDado());
                encontradas++;
            }
            atual = atual.getProx();
        }
        if (encontradas == 0) {
            System.out.println("Nenhuma solicitação com esse status.");
        }
    }

    public void exibirPorCategoria(String categoria) {
        if (cadastroGeral.isEmpty()) {
            System.out.println("Não há solicitações cadastradas.");
            return;
        }
        System.out.println("--- SOLICITAÇÕES DA CATEGORIA " + categoria.toUpperCase() + " ---");
        int encontradas = 0;
        Node<Solicitacao> atual = cadastroGeral.getHead();
        while (atual != null) {
            if (atual.getDado().getCategoria().equalsIgnoreCase(categoria)) {
                System.out.println(atual.getDado());
                encontradas++;
            }
            atual = atual.getProx();
        }
        if (encontradas == 0) {
            System.out.println("Nenhuma solicitação nessa categoria.");
        }
    }

    public void exibirResumoSistema() {
        int aguardando = 0, emAtendimento = 0, concluidas = 0;
        Node<Solicitacao> atual = cadastroGeral.getHead();
        while (atual != null) {
            String st = atual.getDado().getStatus();
            if (st.equals("AGUARDANDO")) aguardando++;
            else if (st.equals("EM_ATENDIMENTO")) emAtendimento++;
            else if (st.equals("CONCLUIDA")) concluidas++;
            atual = atual.getProx();
        }
        System.out.println("========== RESUMO DO SISTEMA ==========");
        System.out.println("Cadastro geral: " + cadastroGeral.getSize() + " solicitações");
        System.out.println("  Aguardando: " + aguardando + " | Em atendimento: " + emAtendimento
                + " | Concluídas: " + concluidas);
        System.out.println("Fila P1: " + filaEsperaP1.totalElementos() + " | Fila P2: " + filaEsperaP2.totalElementos()
                + " | Total aguardando: " + (filaEsperaP1.totalElementos() + filaEsperaP2.totalElementos()));
        if (solicitacaoAtual == null) {
            System.out.println("Atendimento em andamento: nenhum");
        } else {
            System.out.println("Atendimento em andamento: #" + solicitacaoAtual.getCodigo()
                    + " (responsável: " + solicitacaoAtual.getResponsavel() + ")");
        }
        System.out.println("Atendimentos concluídos (histórico): " + historicoAtendimento.getSize());
        System.out.println("Operações registradas (pilha): " + historicoOperacoes.sizeElements());
        System.out.println("=======================================");
    }

    public void gerarDadosExemplo(Menu menu) {
        String[] nomes = {
            "João", "Maria", "Pedro", "Ana",
            "Lucas", "Beatriz", "Rafael", "Camila"
        };
        String[] descricoes = {
            "Problema de internet", "Problema de hardware",
            "Falha na impressora", "Erro no sistema",
            "Acesso bloqueado", "Computador lento",
            "Configuração de rede", "Solicitação de manutenção"
        };
        String[] categorias = {
            "HelpDesk", "Suporte", "HelpDesk", "Suporte",
            "Manutenção", "HelpDesk", "Suporte", "Manutenção"
        };
        int[] prioridades = {1, 2, 1, 2, 1, 2, 1, 2};

        for (int i = 0; i < nomes.length; i++) {
            Solicitacao solicitacao = new Solicitacao(
                menu.proximoCodigoGerador(),
                nomes[i],
                descricoes[i],
                categorias[i],
                prioridades[i]);
            cadastrarSolicitacao(solicitacao);
        }

        for (int i = 0; i < 4; i++) {
            String responsavel = "Responsável de teste " + (i + 1);
            atenderProxima(responsavel);
            if (solicitacaoAtual != null) {
                solicitacaoAtual.setStatus("EM_ATENDIMENTO");
                solicitacaoAtual.setResponsavel(responsavel);
            }
        }
        System.out.println(
            "Dados de exemplo gerados: filas preenchidas, operações registradas, "
            + "atendimentos concluídos e um atendimento em andamento.");
    }
}