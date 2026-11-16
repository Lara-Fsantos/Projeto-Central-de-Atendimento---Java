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
        System.out.println("--- FILA DE ESPERA PRIORIDADE 1 ---");
        System.out.println(filaEsperaP1);
        if (filaEsperaP1.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 1.");
        }

        System.out.println("--- FILA DE ESPERA PRIORIDADE 2 ---");
        System.out.println(filaEsperaP2);
        if (filaEsperaP2.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 2.");
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
            historicoOperacoes.pop();
        } catch (Exception e) {
            System.out.println("Erro ao remover a última operação do histórico: " + e.getMessage());
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

    // Retira a solicitação da fila (ou do atendimento atual), marca como CANCELADA
    // e registra a operação na pilha. A solicitação continua no cadastro geral.
    private void executarCancelamento(Solicitacao sol) throws Exception {
        if (sol.getStatus().equals("EM_ATENDIMENTO")) {
            if (solicitacaoAtual != null && solicitacaoAtual.equals(sol)) {
                solicitacaoAtual = null;
            }
        } else {
            FilaEncadeada<Solicitacao> fila = (sol.getPrioridade() == 1) ? filaEsperaP1 : filaEsperaP2;
            // Recria a fila com dequeue/enqueue, sem o elemento cancelado (mantém o FIFO)
            int total = fila.totalElementos();
            boolean achou = false;
            for (int i = 0; i < total; i++) {
                Solicitacao s = fila.dequeue();
                if (!achou && s.equals(sol)) {
                    achou = true;
                } else {
                    fila.enqueue(s);
                }
            }
            if (!achou) {
                throw new Exception("solicitação #" + sol.getCodigo() + " não encontrada na fila.");
            }
        }
        sol.setStatus("CANCELADA");
        historicoOperacoes.push(new Operacao("CANCELAMENTO", sol));
    }

    public void cancelarSolicitacao(int codigo) {
        Node<Solicitacao> no = cadastroGeral.search(codigoBusca(codigo));
        if (no == null) {
            System.out.println("Solicitação #" + codigo + " não encontrada no cadastro geral.");
            return;
        }
        Solicitacao sol = no.getDado();
        if (sol.getStatus().equals("CONCLUIDA")) {
            System.out.println("Solicitação #" + codigo + " já foi concluída e não pode ser cancelada.");
            return;
        }
        if (sol.getStatus().equals("CANCELADA")) {
            System.out.println("Solicitação #" + codigo + " já está cancelada.");
            return;
        }
        try {
            executarCancelamento(sol);
            System.out.println("Solicitação #" + codigo + " cancelada com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao cancelar solicitação: " + e.getMessage());
        }
    }

    // Remove do cadastro geral. Se ainda estiver ativa, é cancelada antes
    // (saindo da fila ou do atendimento). Concluídas pertencem ao histórico: não são removidas.
    public void removerSolicitacao(int codigo) {
        Node<Solicitacao> no = cadastroGeral.search(codigoBusca(codigo));
        if (no == null) {
            System.out.println("Solicitação #" + codigo + " não encontrada no cadastro geral.");
            return;
        }
        Solicitacao sol = no.getDado();
        if (sol.getStatus().equals("CONCLUIDA")) {
            System.out.println("Solicitação #" + codigo + " já foi concluída: faz parte do histórico e não pode ser removida.");
            return;
        }
        try {
            if (!sol.getStatus().equals("CANCELADA")) {
                executarCancelamento(sol);
            }
            cadastroGeral.remove(sol);
            historicoOperacoes.push(new Operacao("REMOCAO", sol));
            System.out.println("Solicitação #" + codigo + " removida do cadastro geral.");
        } catch (Exception e) {
            System.out.println("Erro ao remover solicitação: " + e.getMessage());
        }
    }

    // Solicitação usada apenas como chave de busca (equals/compareTo comparam o código).
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

    // Copia a fila para um vetor usando apenas dequeue/enqueue (a fila não muda).
    private Solicitacao[] copiarFila(FilaEncadeada<Solicitacao> fila) throws Exception {
        int total = fila.totalElementos();
        Solicitacao[] copia = new Solicitacao[total];
        for (int i = 0; i < total; i++) {
            copia[i] = fila.dequeue();
            fila.enqueue(copia[i]);
        }
        return copia;
    }

    public void exibirPorPrioridade(int prioridade) {
        FilaEncadeada<Solicitacao> fila;
        if (prioridade == 1) {
            fila = filaEsperaP1;
        } else if (prioridade == 2) {
            fila = filaEsperaP2;
        } else {
            System.out.println("Prioridade inválida. Use 1 (alta) ou 2 (baixa).");
            return;
        }
        System.out.println("--- SOLICITAÇÕES AGUARDANDO - PRIORIDADE " + prioridade + " ---");
        if (fila.qIsEmpty()) {
            System.out.println("Não há solicitações aguardando na fila de prioridade " + prioridade + ".");
            return;
        }
        try {
            Solicitacao[] v = copiarFila(fila);
            for (int i = 0; i < v.length; i++) {
                System.out.println(v[i]);
            }
        } catch (Exception e) {
            System.out.println("Erro ao exibir solicitações: " + e.getMessage());
        }
    }

    public void exibirQuantidadesPorPrioridade() {
        int p1 = filaEsperaP1.totalElementos();
        int p2 = filaEsperaP2.totalElementos();
        System.out.println("--- QUANTIDADES POR PRIORIDADE ---");
        System.out.println("Prioridade 1 (alta): " + p1);
        System.out.println("Prioridade 2 (baixa): " + p2);
        System.out.println("Total aguardando atendimento: " + (p1 + p2));
    }

    // Mostra a ordem em que as solicitações seriam atendidas usando a mesma regra
    // de quemAtender(), sem alterar filas nem contador.
    public void demonstrarSelecao() {
        System.out.println("--- DEMONSTRAÇÃO DA SELEÇÃO DA PRÓXIMA SOLICITAÇÃO ---");
        System.out.println("Regra: com as duas filas esperando, a cada 3 atendimentos 2 saem da fila P1 e 1 da fila P2 (FIFO em cada fila).");
        System.out.println("Se uma das filas estiver vazia, atende-se a outra, sem consumir o ciclo.");
        try {
            Solicitacao[] v1 = copiarFila(filaEsperaP1);
            Solicitacao[] v2 = copiarFila(filaEsperaP2);
            if (v1.length == 0 && v2.length == 0) {
                System.out.println("Não há solicitações aguardando atendimento.");
                return;
            }
            int i1 = 0, i2 = 0, contador = contadorAtendimentos, ordem = 1;
            while (i1 < v1.length || i2 < v2.length) {
                boolean disputa = i1 < v1.length && i2 < v2.length;
                boolean daP1;
                if (i1 >= v1.length) {
                    daP1 = false;
                } else if (i2 >= v2.length) {
                    daP1 = true;
                } else {
                    daP1 = (contador != 2);
                }
                Solicitacao s = daP1 ? v1[i1++] : v2[i2++];
                System.out.println(ordem + "º -> #" + s.getCodigo() + " (P" + s.getPrioridade() + ") - " + s.getSolicitante());
                if (disputa) {
                    contador = (contador + 1) % 3;
                }
                ordem++;
            }
        } catch (Exception e) {
            System.out.println("Erro ao demonstrar a seleção: " + e.getMessage());
        }
    }

    public void exibirResumoSistema() {
        int aguardando = 0, emAtendimento = 0, concluidas = 0, canceladas = 0;
        Node<Solicitacao> atual = cadastroGeral.getHead();
        while (atual != null) {
            String st = atual.getDado().getStatus();
            if (st.equals("AGUARDANDO")) aguardando++;
            else if (st.equals("EM_ATENDIMENTO")) emAtendimento++;
            else if (st.equals("CONCLUIDA")) concluidas++;
            else if (st.equals("CANCELADA")) canceladas++;
            atual = atual.getProx();
        }
        System.out.println("========== RESUMO DO SISTEMA ==========");
        System.out.println("Cadastro geral: " + cadastroGeral.getSize() + " solicitações");
        System.out.println("  Aguardando: " + aguardando + " | Em atendimento: " + emAtendimento
                + " | Concluídas: " + concluidas + " | Canceladas: " + canceladas);
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