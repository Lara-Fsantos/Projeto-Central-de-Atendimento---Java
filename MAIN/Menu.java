import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CentralAtendimento central = new CentralAtendimento();
        int codigoGerador = 1; // Gerador sequencial de IDs

        int opcao = -1;

        while (opcao != 0) {
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
                System.out.println("Por favor, digite um número válido!");
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Nome do solicitante: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição do problema: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Categoria (HelpDesk/Suporte/Manutenção): ");
                    String categoria = scanner.nextLine();
                    //System.out.print("Prioridade (1 - Alta, 2 - Média, 3 - Baixa): ");

                    int prioridade = 0;
                    boolean prioridadeValida = false;

                    // Repete até o usuário digitar um número entre 1 e 3
                    while (!prioridadeValida) {
                        System.out.print("Prioridade (1 - Alta, 2 - Média, 3 - Baixa): ");
                        try {
                            prioridade = Integer.parseInt(scanner.nextLine());
                            if (prioridade >= 1 && prioridade <= 3) {
                                prioridadeValida = true;
                            } else {
                                System.out.println("Opção inválida! Digite apenas 1, 2 ou 3.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida! Digite um número inteiro.");
                        }
                    }

                    Solicitacao nova = new Solicitacao(codigoGerador++, nome, descricao, categoria, prioridade);
                    central.cadastrarSolicitacao(nova);
                    break;

                case 2:
                    central.consultarProxima();
                    break;

                case 3:
                    System.out.print("Informe o nome do responsável pelo atendimento: ");
                    String responsavel = scanner.nextLine();
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
}
