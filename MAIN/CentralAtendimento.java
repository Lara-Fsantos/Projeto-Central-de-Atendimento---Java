import java.util.Scanner;

public class _AGenerics_PilhaSequencial {

	public static void main(String[] args) {
		try {
		  Pilha<Integer> p = new Pilha<Integer> ();
		  int n, resto, nSalvo;
		  Scanner s = new Scanner(System.in);

		  System.out.print("\nDigite um numero inteiro em decimal: ");
		  n = s.nextInt();
		  nSalvo = n;
		  System.out.println(p);			  
		  do{
		    resto = n % 2;
		    p.push( resto );
		    n = n / 2;
		  }while ( n != 0);
		  
		  System.out.println("\nTotal de Elementos na pilha: " + p.sizeElements() );
		  System.out.println(p);
		  
		  System.out.print("\nO correspondente binario do valor " + nSalvo + " e: ");
		  
		  while (! p.isEmpty( )) System.out.print( p.pop( )); 
		}catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}
}
