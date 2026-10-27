package ui;

import java.util.Scanner;

import model.Solicitacao;
import service.CentralAtendimento;

public class Menu {
    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        CentralAtendimento central = new CentralAtendimento();
        int codigoGerador = 1;
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n========================================");
            System.out.println("CENTRAL DE ATENDIMENTO");
            System.out.println("========================================\n");
            System.out.println("ATENDIMENTO");
            System.out.println("1 - Cadastrar nova solicitação");
            System.out.println("2 - Consultar próxima solicitação");
            System.out.println("3 - Atender próxima solicitação");
            System.out.println("4 - Exibir fila de solicitações");
            System.out.println("5 - Exibir quantidade de solicitações aguardando atendimento\n");
            System.out.println("OPERAÇÕES");
            System.out.println("6 - Consultar última operação realizada");
            System.out.println("7 - Concluir atendimento");
            System.out.println("8 - Exibir histórico recente");
            System.out.println("9 - Desfazer última operação\n");
            System.out.println("CADASTRO GERAL");
            System.out.println("10 - Exibir todas as solicitações");
            System.out.println("11 - Pesquisar solicitação por código");
            System.out.println("12 - Remover solicitação");
            System.out.println("13 - Exibir solicitações por status");
            System.out.println("14 –  Exibir solicitações por categoria\n");
            System.out.println("HISTÓRICO DE ATENDIMENTOS");
            System.out.println("15 - Exibir do primeiro ao último");
            System.out.println("16 - Exibir do último ao primeiro");
            System.out.println("17 - Navegar pelo histórico");
            System.out.println("18 - Pesquisar atendimento\n");
            System.out.println("RELATÓRIOS");
            System.out.println("19 - Exibir resumo do sistema\n");
            System.out.println("0 - Encerrar");
            System.out.println("-1 - Gerar dados de exemplo (para testes)\n");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextLine()) {
                System.out.println("\nEntrada encerrada. Finalizando o sistema...");
                break;
            }

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor, digite uma opção válida!");
                continue;
            }

            switch (opcao) {
                case 1: {
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

                    Solicitacao nova = new Solicitacao(codigoGerador, nome, descricao, categoria);
                    central.cadastrarSolicitacao(nova);
                    codigoGerador++;
                    break;
                }
                case 2:
                    central.consultarProxima();
                    break;
                case 3: {
                    String responsavel = lerCampoObrigatorio(scanner,
                            "\nInforme o nome do responsável pelo atendimento: ");
                    if (responsavel == null) {
                        break;
                    }
                    central.atenderProxima(responsavel);
                    break;
                }
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
                    System.out.println("Teste, funcionou!");
                    break;
                case 10:
                    System.out.println("Teste, funcionou!");
                    break;
                case 11:
                    System.out.println("Teste, funcionou!");
                    break;
                case 12:
                    System.out.println("Teste, funcionou!");
                    break;
                case 13:
                    System.out.println("Teste, funcionou!");
                    break;
                case 14:
                    System.out.println("Teste, funcionou!");
                    break;
                case 15:
                    System.out.println("Teste, funcionou!");
                    break;
                case 16:
                    System.out.println("Teste, funcionou!");
                    break;
                case 17:
                    System.out.println("Teste, funcionou!");
                    break;
                case 18:
                    System.out.println("Teste, funcionou!");
                    break;
                case 19:
                    System.out.println("Teste, funcionou!");
                    break;
                case -1:
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
        while (true) {
            System.out.print(mensagem);
            if (!scanner.hasNextLine()) {
                return null;
            }
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Entrada obrigatória. Tente novamente.");
        }
    }
}
