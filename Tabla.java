//Blasco Arnaiz, Santiago
//Cabero Mata, Andrés
package practice2;

import practice2.Cuadrante;
/**
 * Tabla de dispersión cerrada con doble exploración utilizada como estrucutra para almacenar cuadrantes.
 * @author SanBlas, AndCabe
 *
 */
public class Tabla {
	
	public int sizeTabla;
	public int numeroDeCuadrantes;
	public final double factorCarga = 0.6;
	
	public Cuadrante[] tabla;
	/**
	 * Constructor de la estrucutra de datos.
	 * @param size Tamaño del que se desea la tabla, debe ser un exponente de 2.
	 */
	public Tabla(int size){
		sizeTabla = size;
		numeroDeCuadrantes = 0;
		tabla = new Cuadrante[size];
	}
	/**
	 * Amplia la tabla de dispersión al doble de su tamaño y vuelve a posicionar los elementos que ya poseía en sus nuevas posiciones.
	 */
	private void reestructuraTabla(){
		Cuadrante[] temporal = tabla; //Contiene temporalemnte los elementos de la tabla hasta su reubicación.
		
		numeroDeCuadrantes = 0;
		sizeTabla = 2 * sizeTabla;
		tabla = new Cuadrante[sizeTabla]; //Sustituimos la tabla con una del doble de su tamaño
		
		for (int i = 0; i < temporal.length; i++){ //Reinsertamos los elementos no nulos en sus nuevas posiciones
			if (temporal[i] != null){
				insertarCuadrante(temporal[i]);
			}
		}
	}
	/**
	 * Busca el cuadrante dentro de la estructura, si lo encuentra lo devuelve, sino devuelve null.
	 * @param ne Subcuadrante NE que debe estar contenido en el cuadrante que se busca.
	 * @param nw Subcuadrante NW que debe estar contenido en el cuadrante que se busca.
	 * @param se Subcuadrante SE que debe estar contenido en el cuadrante que se busca.
	 * @param sw Subcuadrante SW que debe estar contenido en el cuadrante que se busca.
	 * @return Cuadrante que se buscaba o null.
	 */
	public Cuadrante buscarCuadrante(Cuadrante ne, Cuadrante nw, Cuadrante se, Cuadrante sw){
		int h = hashCuad(ne,nw,se,sw); 
		h = h ^ (h >>> 20) ^ (h >>> 12);
		h = h ^ (h >>> 7) ^ (h >>> 4);
		int i = h & (sizeTabla-1); //Mediante la aplicación de funciones hash y operaciones lógicas sobre los subcuadrantes calculamos el índice
		int s = h / sizeTabla; //El salto que da si se produce una colisión
		if (s<0){
			s=-s;
			}
		
		if (s%2==0){
			s++;
			}
		
		while(tabla[i] != null && ! (tabla[i].ne.equals(ne) && tabla[i].nw.equals(nw) && tabla[i].se.equals(se) && tabla[i].sw.equals(sw) )){
			i = (i+s) % sizeTabla;//Si no es el cuadrante buscado aplica el salto y sigue buscando
		}//Si el encontrado es null o el que se deseaba se devuelve
		return tabla[i];
	}
	/**
	 * Añade a la tabla de dispersión el cuadrante que se le introduce.
	 * @param nuevoCuadrante
	 */
	public void insertarCuadrante(Cuadrante nuevoCuadrante){
		numeroDeCuadrantes++;
		
		if (((double)numeroDeCuadrantes/(double)sizeTabla) > factorCarga){//Si se supera el factor de carga se procede a ampliar y reestructurar la tabla
			reestructuraTabla();
		}
		
		int h = hashCuad(nuevoCuadrante.ne, nuevoCuadrante.nw, nuevoCuadrante.se, nuevoCuadrante.sw);
		h = h ^ (h >>> 20) ^ (h >>> 12);
		h = h ^ (h >>> 7) ^ (h >>> 4);
		int i = h & (sizeTabla-1); //Mediante la aplicación de funciones hash y operaciones lógicas sobre los subcuadrantes calculamos el índice
		int s = h / sizeTabla; //El salto que da si se produce una colisión
		if (s<0){
			s=-s;
			}
		
		if (s%2==0){
			s++;
			}

		while(tabla[i] != null){
			i = (i+s) % sizeTabla;
		}//Sólo lo inserta i la posición de la tabla contiene null
		tabla[i] = nuevoCuadrante;
	}
	/**
	 * Función hash primaria.
	 */
	public static int hashCuad(Cuadrante ne,Cuadrante nw,Cuadrante se,Cuadrante sw) {
		return ( nw.hashCode() + 11 * ne.hashCode() + 101 * sw.hashCode() + 1007 * se.hashCode() );
	}
}
