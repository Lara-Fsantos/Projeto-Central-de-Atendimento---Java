import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CentralAtendimento central = new CentralAtendimento();
        int codigoGerador = 1; // Gerador sequencial de IDs

        int opcao = -1;

        while (opcao != 0 && scanner.hasNextLine()) {
            System.out.println("\n========================================");
            System.out.println("CENTRAL DE ATENDIMENTO");
            System.out.println("========================================");
            System.out.println("1 - Cadastrar nova solicitação");
            System.out.println("2 - Consultar próxima solicitação");
            System.out.println("3 - Atender próxima solicitação");
            System.out.println("4 - Exibir fila de solicitações");
            System.out.println("5 - Exibir quantidade de solicitações");
            System.out.println("6 - Consultar última operação realizada");
            System.out.println("7 - Exibir histórico de operações");
            System.out.println("8 - Desfazer última operação");
            System.out.println("0 – Encerrar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor, digite um número válido!");
                continue;
            }

            switch (opcao) {
                case 1:
                    String nome = lerCampoObrigatorio(scanner, "\nNome do solicitante: ");
                    if (nome == null) {
                        break;
                    }
                    String descricao = lerCampoObrigatorio(scanner, "Descrição do problema: ");
                    if (descricao == null) {
                        break;
                    }
                    String categoria = lerCampoObrigatorio(scanner, "Categoria (HelpDesk/Suporte/Manutenção): ");
                    if (categoria == null) {
                        break;
                    }

                    Solicitacao nova = new Solicitacao(codigoGerador++, nome, descricao, categoria);
                    central.cadastrarSolicitacao(nova);
                    break;

                case 2:
                    central.consultarProxima();
                    break;

                case 3:
                    String responsavel = lerCampoObrigatorio(scanner,
                            "\nInforme o nome do responsável pelo atendimento: ");
                    if (responsavel == null) {
                        break;
                    }
                    central.atenderProxima(responsavel);
                    break;

                case 4:
                    central.exibirFila();
                    break;

                case 5:
                    central.exibirQuantidadeFila();
                    break;

                case 6:
                    central.consultarUltimaOperacao();
                    break;

                case 7:
                    central.exibirHistorico();
                    break;

                case 8:
                    central.desfazerUltimaOperacao();
                    break;
                case 9:
                    central.gerarDadosExemplo();
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }

        scanner.close();
    }

    private static String lerCampoObrigatorio(Scanner scanner, String mensagem) {
        while (scanner.hasNextLine()) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Entrada obrigatória. Tente novamente.");
        }
        return null;
    }
}
