	public class CentralAtendimento {
		private FilaCircular<Solicitacao> filaEspera;
		private Pilha<Operacao> historicoOperacoes;

		public CentralAtendimento() {
			this.filaEspera = new FilaCircular<Solicitacao>(100);
			this.historicoOperacoes = new Pilha<Operacao>();
		}

		// 1 - Cadastrar nova solicitação
		public void cadastrarSolicitacao(Solicitacao sol) {
			filaEspera.enqueue(sol);
			historicoOperacoes.push(new Operacao("CADASTRO", sol));
		}

		// 2 - Consultar próxima solicitação
		public void consultarProxima() {
			Solicitacao proxima = filaEspera.front();
                System.out.println("--- PRÓXIMA SOLICITAÇÃO ---");
                System.out.println(proxima);
		}

		// 3 - Atender próxima solicitação
		public void atenderProxima(String responsavel) {
			Solicitacao atendi = filaEspera.dequeue();
                atendi.setStatus("EM_ATENDIMENTO");

                historicoOperacoes.push(new Operacao("ATENDIMENTO", atendi));

			System.out.println("--- SOLICITAÇÃO EM ATENDIMENTO ---");
			System.out.println(atendi);
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
			Operacao ultima = historicoOperacoes.topo();
                System.out.println("--- ÚLTIMA OPERAÇÃO REALIZADA ---");
                System.out.println(ultima);
		}

		// 7 - Exibir histórico de operações
		public void exibirHistorico() {
			//Exibir toda Pilha
			System.out.println(historicoOperacoes);
		}

		// 8 - Desfazer última operação
		public void desfazerUltimaOperacao() {
			
			// Espia a última operação para verificar o tipo
			Operacao ultimaOp = historicoOperacoes.topo();

			if (!ultimaOp.getTipo().equalsIgnoreCase("ATENDIMENTO")) {
				System.out.println("Operação do topo (" + ultimaOp.getTipo() + ") não é do tipo 'ATENDIMENTO'. Não é possível desfazer!");
				return;
			}

			// Remove da pilha
			ultimaOp = historicoOperacoes.pop();
			Solicitacao sol = ultimaOp.getSolicitacao();

			// Altera status de volta para AGUARDANDO
			sol.setStatus("AGUARDANDO");

			// Reorganiza a fila usando FilaAuxiliar para inserir no INÍCIO
			FilaCircular<Solicitacao> filaAuxiliar = new FilaCircular<Solicitacao>(100);

			// 1º: Insere o elemento retornado no início da nova fila
			filaAuxiliar.enqueue(sol);

			// 2º: Transfere os demais elementos da fila original para a auxiliar
			while (!filaEspera.qIsEmpty()) {
				filaAuxiliar.enqueue(filaEspera.dequeue());
			}

			// 3º: Substitui a referência
			this.filaEspera = filaAuxiliar;

			System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
		}
}

