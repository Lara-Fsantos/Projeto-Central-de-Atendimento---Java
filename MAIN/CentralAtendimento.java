import java.util.Scanner;

	public class CentralAtendimento {

		public CentralAtendimento() {
			System.out.println("[TESTE] Central de Atendimento inicializada!");
		}

		// 1 - Cadastrar nova solicitação
		public void cadastrarSolicitacao(Solicitacao sol) {
			System.out.println("[TESTE] Funcionalidade 1 funcionando! Solicitação #" + sol.getCodigo() + " recebida.");
		}

		// 2 - Consultar próxima solicitação
		public void consultarProxima() {
			System.out.println("[TESTE] Funcionalidade 2 funcionando! (Consultar próxima)");
		}

		// 3 - Atender próxima solicitação
		public void atenderProxima(String responsavel) {
			System.out.println("[TESTE] Funcionalidade 3 funcionando! Atribuído ao responsável: " + responsavel);
		}

		// 4 - Exibir fila de solicitações
		public void exibirFila() {
			System.out.println("[TESTE] Funcionalidade 4 funcionando! (Exibir fila)");
		}

		// 5 - Exibir quantidade de solicitações
		public void exibirQuantidadeFila() {
			System.out.println("[TESTE] Funcionalidade 5 funcionando! (Exibir quantidade)");
		}

		// 6 - Consultar última operação realizada
		public void consultarUltimaOperacao() {
			System.out.println("[TESTE] Funcionalidade 6 funcionando! (Consultar última operação)");
		}

		// 7 - Exibir histórico de operações
		public void exibirHistorico() {
			System.out.println("[TESTE] Funcionalidade 7 funcionando! (Exibir histórico)");
		}

		// 8 - Desfazer última operação
		public void desfazerUltimaOperacao() {
			System.out.println("[TESTE] Funcionalidade 8 funcionando! (Desfazer última operação)");
		}
}

