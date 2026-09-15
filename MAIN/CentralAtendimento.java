import java.util.Scanner;

	public class CentralAtendimento {
		FilaCircular fila = new FilaCircular());
		Pilha pilha = new Pilha();
		public CentralAtendimento() {
			System.out.println("[TESTE] Central de Atendimento inicializada!");
		}

		// 1 - Cadastrar nova solicitação
		public void cadastrarSolicitacao(Solicitacao sol) {
			fila.enqueue(sol.getCodigo());
			pilha.push(sol);//jogar na operação
			System.out.println("[TESTE] Funcionalidade 1 funcionando! Solicitação #" + sol.getCodigo() + " recebida.");
		}

		// 2 - Consultar próxima solicitação
		public void consultarProxima() {
			fila.front();
			System.out.println("[TESTE] Funcionalidade 2 funcionando! (Consultar próxima)");
		}

		// 3 - Atender próxima solicitação
		public void atenderProxima(String responsavel) {
			//pega da fila arruma, joga na fila e em cada passo vai jogando em pilha
			System.out.println("[TESTE] Funcionalidade 3 funcionando! Atribuído ao responsável: " + responsavel);
		}

		// 4 - Exibir fila de solicitações
		public void exibirFila() {
			//mostrar começo a fim fila
			System.out.println("[TESTE] Funcionalidade 4 funcionando! (Exibir fila)");
		}

		// 5 - Exibir quantidade de solicitações
		public void exibirQuantidadeFila() {
			fila.totalElementos();
			System.out.println("[TESTE] Funcionalidade 5 funcionando! (Exibir quantidade)");
		}

		// 6 - Consultar última operação realizada
		public void consultarUltimaOperacao() {
			//pegar da pilha de historico o topo
			System.out.println("[TESTE] Funcionalidade 6 funcionando! (Consultar última operação)");
		}

		// 7 - Exibir histórico de operações
		public void exibirHistorico() {
			//Exibir toda Pilha
			System.out.println("[TESTE] Funcionalidade 7 funcionando! (Exibir histórico)");
		}

		// 8 - Desfazer última operação
		public void desfazerUltimaOperacao() {
			//olhar o doc, mas tirar na pilha e desfazer oq foi feito na fila, como? com a aux que vamos precisar cirar em todos os outros
			System.out.println("[TESTE] Funcionalidade 8 funcionando! (Desfazer última operação)");
		}
}

