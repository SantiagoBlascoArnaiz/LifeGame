//Blasco Arnaiz, Santiago
//Cabero Mata, Andrés
package practice2;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import practice2.Cuadrante;

import static java.lang.Math.pow;
/**
 * Maneja todas las tareas necesarias para la ejecución del algoritmo del juego de la vida de Conway.
 * @author SanBlas, AndCabe
 *
 */
public class HiperVida {
	public static void main(String[] args){
		
		Scanner teclado = new Scanner(System.in);
		Cuadrante Universo = leerFichero(teclado);
		
		long ciclos = validarEntradaTeclado(teclado);
		
		long tiempoInicio, tiempoFin; //Utilizadas para medir el tiempo que tarde el algoritmo
		tiempoInicio = System.nanoTime();
		
		Universo = Ciclos(Universo, ciclos);

		System.out.println("Células vivas: " + Universo.pob);
		tiempoFin = System.nanoTime();
		
		System.out.println("La ejecución ha tardado " + nanoSegundosATiempo(tiempoFin - tiempoInicio) + " (" + (tiempoFin - tiempoInicio) / 1000000000.0 + " s.)");
		
		System.out.print("\nFichero de salida (Pulse enter si quiere mostrarlo por pantalla): ");
		
		String archivo = teclado.nextLine();
		archivo = teclado.nextLine();
	    if (archivo.equals("")) //Si se pulsa enter en vez de introducir un nombre se imprmirá el tablero
	    	Universo.imprimirCuadrante();
	    else {
	    	int terminacion = archivo.indexOf(".txt"); //devuelve -1 si la cadena no está contenida en el string
			if (terminacion == -1) {
				archivo = archivo + ".txt";
			}
	    	escribirFichero(Universo,archivo);
	    }
	}
	/**
	 * Se asegura de que van a caber las células vivass dentro del sub-cuadrante central despuúes de un cálculo de generación y realiza mínomo el número de ciclos que se le indica.
	 * @param cuadrante Al que se realizará la generación y si es necesario la expansión.
	 * @param ciclos Las veces que se repetirá el proceso de generación.
	 * @return El cuadrante despues de realizar la generación.
	 */
	public static Cuadrante Ciclos (Cuadrante cuadrante, long ciclos) {
		int i = 0;
		while (i < ciclos) {
			if (cuadrante.pob == 0) {
				break;
			}
			while (cuadrante.nw.se.se.pob + cuadrante.ne.sw.sw.pob + cuadrante.se.nw.nw.pob + cuadrante.sw.ne.ne.pob < cuadrante.pob) { //Comprobamos que todas las células se encuentran en el sub-sub-cuadrante central
				cuadrante = cuadrante.expandir();
			}
			i = i + (int)pow(2,(cuadrante.niv - 2));
			
			cuadrante = cuadrante.generacion();
		}
		System.out.println("Se han realizado " + i + " iteraciones.");
		return cuadrante;
	}
	/**
	 * Lee el fichero con el formato preestablecido y crea un cuadrante con las carácterísticas especificadas en el archivo.
	 * @param teclado
	 * @return Cuadrante con las especificaciones solicitadas.
	 */
	public static Cuadrante leerFichero(Scanner teclado){
			
		Cuadrante tablero;
		System.out.print("Escriba un nombre de fichero: ");
		int size;
		int nivel = 0;
		String nombre = teclado.nextLine();
		
		int terminacion = nombre.indexOf(".txt"); //devuelve -1 si la cadena no está contenida en el string
		if (terminacion == -1) {
			nombre = nombre + ".txt";
		}
		File fichero = new File(nombre);
		try {
			Scanner entrada = new Scanner(fichero);

			int filas = entrada.nextInt();
			int columnas = entrada.nextInt();
			System.out.println();
			if (filas > columnas) {
				size = filas;
			}else {
				size = columnas;
			}
			
			for (int n = 0; pow(2,n) < size; n++) {
				nivel = n + 1;
			}
			tablero = Cuadrante.crearVacio(nivel);
			
			entrada.nextLine();	//Omitimos el salto de línea

			for (int i = 0; i < filas; i++) {

				String linea = entrada.nextLine(); //Leemos la línea
				char auxiliar[] = linea.toCharArray();	//Pasa la línea a un array de carácteres
				
				for (int x = 0; x < columnas; x++) {
					if (auxiliar[x] == 'X') {
						tablero = tablero.setPixel(x + (long)((pow(2,nivel) - columnas)/2), i + (long)((pow(2,nivel) - filas)/2), 1); //De esta forma conseguimos centrar el patrón en el cuadrante
					}
				}
			}
			entrada.close();
			
			return tablero;
		}catch (Exception e) {
			System.out.println("No existe el fichero " + e.getMessage());
			tablero = leerFichero(teclado);
			
			return tablero;
		}
		
	}
	/**
	 * Se asegura de que el número de iteraciones introducido por el usuario sea un número de tipo long.
	 * @param keyboard
	 * @return Número long introducido por el usuario.
	 */
	public static long validarEntradaTeclado(Scanner keyboard) {
	
		boolean valido;
		long numero;
		do {
			try {
				System.out.print("Excriba el número de iteraciones mínimas: ");
				numero = keyboard.nextLong();
				valido = true;
			} catch (Exception e) {
	
				keyboard.next(); // Para mover el puntero delante del caracter erroneo introducido.
				System.out.print("Eso no es un número entero. ");
				valido = false;
				numero = -1;
			}
		} while (valido == false || numero < 0); //Se solicita un número por teclado hasta que este sea un entero valido
		return numero;
	}
	/**
	 * Pasa los nanosegundos introducidos a una cadena de carácteres que indica el tiempo en unidades de tiempo más grandes.
	 * @param nanosegundos
	 * @return String del tiempo introducido expresado en otras unidades.
	 */
	public static String nanoSegundosATiempo(long nanosegundos) {

		String tiempo;

		long nano = nanosegundos % 1000000;  //Pasamos de nanosegundos a nanosegundos,milisegundos,segundos,minutos y horas
		nanosegundos -= nano;
		nanosegundos /= 1000000;
		long mili = nanosegundos % 1000;
		nanosegundos -= mili;
		nanosegundos /= 1000;
		long segs = nanosegundos % 60;
		nanosegundos -= segs;
		nanosegundos /= 60;
		long mins = nanosegundos % 60;
		nanosegundos -= mins;
		nanosegundos /= 60;
		long horas = nanosegundos;

		if (horas == 0) {  // Si una cantidad de tiempo mayor que la siguiente es 0 no aparecerá en la cadena.
			if (mins == 0) {
				if (segs == 0) {
					if (mili == 0) {
						if (nano == 0) {
							tiempo = "No ha tardado.";
						} else {
							tiempo = nano + " ns.";
						}
					} else {
						tiempo = mili + " ms " + nano + " ns.";
					}
				} else {
					tiempo = segs + " s " + mili + " ms " + nano + " ns.";
				}
			} else {
				tiempo = mins + " mins " + segs + " s " + mili + " ms " + nano + " ns.";
			}
		} else {
			tiempo = horas + " h " + mins + " min " + segs + " s " + mili + " ms " + nano + " ns.";
		}
		return tiempo;
	}
	/**
	 * Escribe en un fichero .txt el resultado de calcular el número de iteraciones introducido por el usuario.
	 * @param tablero Contiene la disposición de células vivas y muertas a escribir.
	 * @param archivo Nombre del archivo donde se desea escribir.
	 */
	public static void escribirFichero(Cuadrante tablero, String archivo) {
		
		long minX = (long)pow(2,tablero.niv) - 1;
		long maxX = 0;
		long minY = (long)pow(2,tablero.niv) - 1;
		long maxY = 0;
		for (int j = 0; j < pow(2,tablero.niv); j++) {
	
			for (int i = 0; i < pow(2,tablero.niv); i++) {
				if (tablero.getPixel(i, j) == 1) {
					if (i < minX) {
						minX = i;
					}
					if (i > maxX) {
						maxX = i;
					}
					if (j < minY) {
						minY = j;
					}
					if (j > maxY) {
						maxY = j;
					}
				}
			}
		}
		
		try {
			PrintWriter escribir = new PrintWriter(archivo);

			if (tablero.pob==0) {
				escribir.println("0");
				escribir.print("0");
			} else {
				escribir.println((maxX - minX + 1)); //Filas
				escribir.print((maxY - minY + 1));	//Columnas
				for (long j = minY; j <= maxY; j++) {
					escribir.println();
					for (long i = minX; i <= maxX; i++) {
						if (tablero.getPixel(i, j) == 1) {
							escribir.print('X');
						}else {
							escribir.print('.');
						}
					}
				}
			}
			System.out.println("\nEl fichero " + archivo + " ha sido escrito con éxito.");
			escribir.close();
		} catch (Exception e) {
			System.out.print(e);
		}
	}
}
