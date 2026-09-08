import java.util.Scanner;
public class Menu{
    public void exibirMenu(){
        Scanner s = new Scanner(System.in);
        int opc;
        do {
            System.out.println("====================");
            System.out.println("CENTRAL DE ATENDIMENTO");
            System.out.println("====================");
            System.out.println("1 - Cadastrar nova solicitação");
            System.out.println("2 - Consultar próxima solicitação");
            System.out.println("3 - Atender próxima solicitação");
            System.out.println("4 - Exibir fila de solicitações");
            System.out.println("5 - Exibir quantidade de solicitações");
            System.out.println("6 - Consultar última operação realizada");
            System.out.println("7 - Exibir histórico de operações");
            System.out.println("8 - Desfazer última operação");
            System.out.println("0 – Encerrar");

        opc = s.nextInt();
        switch(opc){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
        }
        } while (opc != 0);
        s.close();
}
}
