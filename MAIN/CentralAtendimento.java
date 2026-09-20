	public class CentralAtendimento {
		private FilaCircular<Solicitacao> filaEspera;
		private Pilha<Operacao> historicoOperacoes;

		public CentralAtendimento() {
			this.filaEspera = new FilaCircular<Solicitacao>(100);
			this.historicoOperacoes = new Pilha<Operacao>();
		}

		// 1 - Cadastrar nova solicitação
		public void cadastrarSolicitacao(Solicitacao sol) {
			try{
			filaEspera.enqueue(sol);
			historicoOperacoes.push(new Operacao("CADASTRO", sol));
		}
		catch (Exception e) {
				System.out.println("Não foi possível cadastrar a solicitação: " + e.getMessage());
			}
		}

		// 2 - Consultar próxima solicitação
		public void consultarProxima() {
			try {
				Solicitacao proxima = filaEspera.front();
				System.out.println("--- PRÓXIMA SOLICITAÇÃO ---");
				System.out.println(proxima);
			} catch (Exception e) {
				System.out.println("Não há solicitações na fila.");
			}
		}

		// 3 - Atender próxima solicitação
		public void atenderProxima(String responsavel) {
			try {
			Solicitacao atendi = filaEspera.dequeue();
                atendi.setStatus("EM_ATENDIMENTO");

                historicoOperacoes.push(new Operacao("ATENDIMENTO", atendi));

			System.out.println("--- SOLICITAÇÃO EM ATENDIMENTO ---");
			System.out.println(atendi);
		}
		catch (Exception e) {
				System.out.println("Não há solicitações na fila.");
			}
		}

		// 4 - Exibir fila de solicitações
		public void exibirFila() {
			//mostrar começo a fim fila
			System.out.println(filaEspera);
		}

		// 5 - Exibir quantidade de solicitações
		public void exibirQuantidadeFila() {
			System.out.println("Quantidade de solicitações aguardando na fila: " + filaEspera.totalElementos());
		}

		// 6 - Consultar última operação realizada
		public void consultarUltimaOperacao() {
			//pegar da pilha de historico o topo
			try {
				Operacao ultima = historicoOperacoes.topo();
				System.out.println("--- ÚLTIMA OPERAÇÃO REALIZADA ---");
				System.out.println(ultima);
			} catch (Exception e) {
				System.out.println("Não há operações no histórico.");
			}
		}

		// 7 - Exibir histórico de operações
		public void exibirHistorico() {
			//Exibir toda Pilha
			System.out.println(historicoOperacoes);
		}

		// 8 - Desfazer última operação
		public void desfazerUltimaOperacao() {
			Operacao ultimaOp;
			try{
			ultimaOp = historicoOperacoes.topo();
			}
			catch (Exception e) {
				System.out.println("Não há operações no histórico para desfazer.");
				return;
			}

			if (!ultimaOp.getTipo().equalsIgnoreCase("ATENDIMENTO")) {
				System.out.println("Operação do topo (" + ultimaOp.getTipo() + ") não é do tipo 'ATENDIMENTO'. Não é possível desfazer!");
				return;
			}

			try {
			ultimaOp = historicoOperacoes.pop();
			}
			catch (Exception e) {
				System.out.println("Erro ao remover a última operação do histórico: " + e.getMessage());
				return;
			}
			Solicitacao sol = ultimaOp.getSolicitacao();

			// Altera status de volta para AGUARDANDO
			sol.setStatus("AGUARDANDO");

			// Reorganiza a fila usando FilaAuxiliar para inserir no INÍCIO
			FilaCircular<Solicitacao> filaAuxiliar = new FilaCircular<Solicitacao>(100);

			try{
			filaAuxiliar.enqueue(sol);
			}
			catch (Exception e) {
				System.out.println("Erro ao adicionar a solicitação à fila auxiliar: " + e.getMessage());
				return;
			}
			// 2º: Transfere os demais elementos da fila original para a auxiliar
			try {
				while (!filaEspera.qIsEmpty()) {
					filaAuxiliar.enqueue(filaEspera.dequeue());
				}
			} catch (Exception e) {
				System.out.println("Erro ao transferir elementos para a fila auxiliar: " + e.getMessage());
				return;
			}

			// 3º: Substitui a referência
			this.filaEspera = filaAuxiliar;

			System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
		}
		public void gerarDadosExemplo() {
			
			Solicitacao sol1 = new Solicitacao(1, "Alice", "Problema no computador", "HelpDesk", 0);
			Solicitacao sol2 = new Solicitacao(2, "Bob", "Erro no software", "Suporte", 0);
			Solicitacao sol3 = new Solicitacao(3, "Charlie", "Manutenção do servidor", "Manutenção", 0);
			Solicitacao sol4 = new Solicitacao(4, "Diana", "Problema de rede", "HelpDesk", 0);
			Solicitacao sol5 = new Solicitacao(5, "Eve", "Atualização de sistema", "Suporte", 0);
			Solicitacao sol6 = new Solicitacao(6, "Frank", "Problema de hardware", "Manutenção", 0);
			Solicitacao sol7 = new Solicitacao(7, "Grace", "Configuração de software", "Suporte", 0);
			Solicitacao sol8 = new Solicitacao(8, "Hank", "Problema de impressão", "HelpDesk", 0);
			Solicitacao sol9 = new Solicitacao(9, "Ivy", "Manutenção de rede", "Manutenção", 0);


			cadastrarSolicitacao(sol1);
			cadastrarSolicitacao(sol2);
			cadastrarSolicitacao(sol3);
			cadastrarSolicitacao(sol4);
			cadastrarSolicitacao(sol5);
			cadastrarSolicitacao(sol6);
			cadastrarSolicitacao(sol7);
			cadastrarSolicitacao(sol8);
			cadastrarSolicitacao(sol9);
			System.out.println("Dados de exemplo gerados com sucesso!");
		}
}