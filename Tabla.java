//Blasco Arnaiz, Santiago
//Cabero Mata, Andr�s
package practice2;

import practice2.Cuadrante;
/**
 * Tabla de dispersi�n cerrada con doble exploraci�n utilizada como estrucutra para almacenar cuadrantes.
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
	 * @param size Tama�o del que se desea la tabla, debe ser un exponente de 2.
	 */
	public Tabla(int size){
		sizeTabla = size;
		numeroDeCuadrantes = 0;
		tabla = new Cuadrante[size];
	}
	/**
	 * Amplia la tabla de dispersi�n al doble de su tama�o y vuelve a posicionar los elementos que ya pose�a en sus nuevas posiciones.
	 */
	private void reestructuraTabla(){
		Cuadrante[] temporal = tabla; //Contiene temporalemnte los elementos de la tabla hasta su reubicaci�n.
		
		numeroDeCuadrantes = 0;
		sizeTabla = 2 * sizeTabla;
		tabla = new Cuadrante[sizeTabla]; //Sustituimos la tabla con una del doble de su tama�o
		
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
		int i = h & (sizeTabla-1); //Mediante la aplicaci�n de funciones hash y operaciones l�gicas sobre los subcuadrantes calculamos el �ndice
		int s = h / sizeTabla; //El salto que da si se produce una colisi�n
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
	 * A�ade a la tabla de dispersi�n el cuadrante que se le introduce.
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
		int i = h & (sizeTabla-1); //Mediante la aplicaci�n de funciones hash y operaciones l�gicas sobre los subcuadrantes calculamos el �ndice
		int s = h / sizeTabla; //El salto que da si se produce una colisi�n
		if (s<0){
			s=-s;
			}
		
		if (s%2==0){
			s++;
			}

		while(tabla[i] != null){
			i = (i+s) % sizeTabla;
		}//S�lo lo inserta i la posici�n de la tabla contiene null
		tabla[i] = nuevoCuadrante;
	}
	/**
	 * Funci�n hash primaria.
	 */
	public static int hashCuad(Cuadrante ne,Cuadrante nw,Cuadrante se,Cuadrante sw) {
		return ( nw.hashCode() + 11 * ne.hashCode() + 101 * sw.hashCode() + 1007 * se.hashCode() );
	}
}
