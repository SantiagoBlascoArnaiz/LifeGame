//Blasco Arnaiz, Santiago
//Cabero Mata, Andrés
package practice2;

import static java.lang.Math.pow;

import practice2.Tabla;
/**
 * Implementa los atributos y operaciones necesarios para los cuadrantes del juego de la vida.
 * @author SanBlas, AndCabe
 *
 */
public class Cuadrante {
	
	public final int niv;
	public final long pob;
	public final Cuadrante ne, nw, se, sw;
	public Cuadrante res;
	
	public static Tabla almacen = new Tabla((int)pow(2,10));;
	
	public static final Cuadrante niv00 = new Cuadrante(0);
	public static final  Cuadrante niv01 = new Cuadrante(1);
	/**
	 * Constructor para los cuadrantes de nivel 0.
	 * @param valor Población del cuadrante de nivel 0.
	 */
	protected Cuadrante (int valor) {
		
		niv = 0;
		pob = valor;
		ne = null;
		nw = null;
		se = null;
		sw = null;
	}
	/**
	 * Constructor para los cuadrantes de nivel superior a 0.
	 * @param ne Subcuadrante NE del cuadrante a crear.
	 * @param nw Subcuadrante NW del cuadrante a crear.
	 * @param se Subcuadrante SE del cuadrante a crear.
	 * @param sw Subcuadrante SW del cuadrante a crear.
	 */
	protected Cuadrante (Cuadrante ne,Cuadrante nw,Cuadrante se,Cuadrante sw) {
		
		niv = ne.niv + 1;
		pob = ne.pob + nw.pob + se.pob + sw.pob;
		this.ne = ne;
		this.nw = nw;
		this.se = se;
		this.sw = sw;
	}
	/**
	 * Expande el cuadrante un nivel dejando en su centro el cuadrante inicial
	 * @return Cuadrante de un nivel más.
	 */
	public Cuadrante expandir() {
		
		Cuadrante vacioTemp = crearVacio(niv -1);
		Cuadrante expandidoNE = crear(vacioTemp,vacioTemp,vacioTemp,ne);
		Cuadrante expandidoNW = crear(vacioTemp,vacioTemp,nw,vacioTemp);
		
		Cuadrante expandidoSE = crear(vacioTemp,se,vacioTemp,vacioTemp);
		Cuadrante expandidoSW = crear(sw,vacioTemp,vacioTemp,vacioTemp);
						
		Cuadrante expandido = crear(expandidoNE,expandidoNW,expandidoSE,expandidoSW);
		
		return expandido;
	}
	/**
	 * Crea un cuadrante e células muertas del tamaño que se le indica.
	 * @param nivel Nivel del que se desea el cuadrante vacío.
	 * @return Cuadrante vacío del nivel indicado.
	 */
	public static Cuadrante crearVacio(int nivel) {
		
		Cuadrante vacio;
		if (nivel == 0) {
			vacio = niv00;
		}else {
			Cuadrante vacioTemp = crearVacio(nivel -1);
			vacio = crear(vacioTemp,vacioTemp,vacioTemp,vacioTemp);
		}
		return vacio;
	}
	/**
	 * Devuelve el cuadrante foramdo por los cuadrantes que se indican.
	 * @param ne
	 * @param nw
	 * @param se
	 * @param sw
	 * @return Cuadrante cuyos subucuadrantes son los 4 pasados por parámetros.
	 */
	public static Cuadrante crear(Cuadrante ne,Cuadrante nw,Cuadrante se,Cuadrante sw) {
		
		Cuadrante cuadrante = almacen.buscarCuadrante(ne,nw,se,sw);
		if (cuadrante == null) { //Si no se encuentra en la tabla lo creamos e insertamos
			cuadrante = new Cuadrante(ne,nw,se,sw);
			almacen.insertarCuadrante(cuadrante);
		}
		return cuadrante; // Si se encuentra en la tabla lo devolvemos
	}
	/**
	 * Devuelve la población del pixel indicado mediante coordenadas x e y.
	 * @param x Coordenada x del pixel.
	 * @param y Coordenada y del pixel.
	 * @return Cuadrante de nivel 0 solicitado.
	 */
	public long getPixel(long x, long y) {
		int exponente = ((int)pow(2,niv))/2;
		if (niv == 0) {
			return pob;
		}else {//Dependiendo de en que cuadrante se encuentre el pixel solicitado se ajustan o no las coordendas x e y para buscar en los subcuadrantes
			if (x >= (exponente) && y >= (exponente)) {
				x = (long) (x - (exponente));
				y = (long) (y - (exponente));
				return se.getPixel(x,y);
				
			}else if (x < (exponente) && y < (exponente)) {
				return nw.getPixel(x,y);
				
			}else if (x >= (exponente) && y < (exponente)) {
				x = (long) (x - (exponente));
				return ne.getPixel(x,y);
				
			}else{
				y = (long) (y - (exponente));
				return sw.getPixel(x,y);
			}
		}
	}
	/**
	 * Devuelve el cuadrante con el pixel indicado cambiado al estado solicitado.
	 * @param x Coordenada x del pixel.
	 * @param y Coordenada y del pixel.
	 * @param v Población que tendrá el pixel indicado.
	 * @return Cuadrante con el pixel indicado cambiado al estado solicitado.
	 */
	public Cuadrante setPixel(long x, long y, long v) {
		int exponente = ((int)pow(2,niv));
		if (x > exponente - 1 || y > exponente - 1) {
			throw new IllegalArgumentException(x + "," + y + "Fuera del rango de cuadrante." + ((int)pow(2,niv) - 1) + "," + ((int)pow(2,niv) - 1));
		}else {
			if (niv == 0) {
				if (v == 1) {
					return niv01;
				}else {
					return niv00;
				}
			}else {//Dependiendo de en que cuadrante se encuentre el pixel solicitado se ajustan o no las coordendas x e y para buscar en los subcuadrantes
				exponente = exponente/2;
				if (x >= (exponente) && y >= (exponente)) {
					x = (long) (x - exponente);
					y = (long) (y - exponente);
					return crear(ne,nw,se.setPixel(x,y,v),sw);
					
				}else if (x < (exponente) && y < (exponente)) {
					return crear(ne,nw.setPixel(x,y,v),se,sw);
					
				}else if (x >= (exponente) && y < (exponente)) {
					x = (long) (x - (exponente));
					return crear(ne.setPixel(x,y,v),nw,se,sw);
					
				}else if (x < (exponente) && y >= (exponente)) {
					y = (long) (y - (exponente));
					return crear(ne,nw,se,sw.setPixel(x,y,v));
				}else {
					throw new IllegalArgumentException("Fuera del rango de cuadrante.");
				}
			}
		}
	}
	/**
	 * Devuelve el cuadrante central de un nivel menos del que se le ha pasado y con generación aplicada.
	 * @return Cuadrante al que se le han aplicado 2^(nivel del cuadrante -2) iteraciones.
	 */
	public Cuadrante generacion () {
		if (res == null) {
			if (niv > 2) {
				
				Cuadrante n00 = nw.generacion();
				Cuadrante n01 = crear(ne.nw, nw.ne, ne.sw, nw.se).generacion();
				Cuadrante n02 = ne.generacion();
				
				Cuadrante n10 = crear(nw.se, nw.sw, sw.ne, sw.nw).generacion();
				Cuadrante n11 = crear(ne.sw, nw.se, se.nw, sw.ne).generacion();
				Cuadrante n12 = crear(ne.se, ne.sw, se.ne, se.nw).generacion();
				
				Cuadrante n20 = sw.generacion();
				Cuadrante n21 = crear(se.nw, sw.ne, se.sw, sw.se).generacion();
				Cuadrante n22 = se.generacion();
				
				Cuadrante m00 = crear(n01,n00,n11,n10);
				Cuadrante m01 = crear(n02,n01,n12,n11);
				Cuadrante m10 = crear(n11,n10,n21,n20);
				Cuadrante m11 = crear(n12,n11,n22,n21);
				
				Cuadrante r00 = m00.generacion();
				Cuadrante r01 = m01.generacion();
				Cuadrante r10 = m10.generacion();
				Cuadrante r11 = m11.generacion();
				
				res = crear(r01,r00,r11,r10);
				return res;
			}else{
				res = generacion2();
				return res;
			}
		}else {
			return res;
		}
		
	}
	/**
	 * Aplica las reglas del juego de la vida sobre las células del cuadrante.
	 * @return Cuadrante después de un cilco del algoritmo del juego de la vida.
	 */
	public Cuadrante generacion2 () {
		if (res == null) {
			Cuadrante a;
			Cuadrante b;
			Cuadrante c;
			Cuadrante d;
			//Contamos las células vivas alrededor de cada una de las cuatro células del centro y según su número y estado pasa a viva o muerta
			long contadorA = ne.nw.pob + ne.ne.pob + ne.se.pob + se.ne.pob + se.nw.pob + sw.ne.pob + nw.se.pob + nw.ne.pob;
			if(ne.sw.pob == 1) {
				if(contadorA == 2 || contadorA == 3) {
					a = niv01;
				}else {
					a = niv00;
				}
			}else {
				if(contadorA == 3) {
					a = niv01;
				}else {
					a = niv00;
				}
			}
			
			long contadorB = nw.ne.pob + ne.nw.pob + ne.sw.pob + se.nw.pob + sw.ne.pob + sw.nw.pob + nw.sw.pob + nw.nw.pob;
			if(nw.se.pob == 1) {
				if(contadorB == 2 || contadorB == 3) {
					b = niv01;
				}else {
					b = niv00;
				}
			}else {
				if(contadorB == 3) {
					b = niv01;
				}else {
					b = niv00;
				}
			}
			
			long contadorC = ne.sw.pob + ne.se.pob + se.ne.pob + se.se.pob + se.sw.pob + sw.se.pob + sw.ne.pob + nw.se.pob;
			if(se.nw.pob == 1) {
				if(contadorC == 2 || contadorC == 3) {
					c = niv01;
				}else {
					c = niv00;
				}
			}else {
				if(contadorC == 3) {
					c = niv01;
				}else {
					c = niv00;
				}
			}
			
			long contadorD = nw.se.pob + ne.sw.pob + se.nw.pob + se.sw.pob + sw.se.pob + sw.sw.pob + sw.nw.pob + nw.sw.pob;
			if(sw.ne.pob == 1) {
				if(contadorD == 2 || contadorD == 3) {
					d = niv01;
				}else {
					d = niv00;
				}
			}else {
				if(contadorD == 3) {
					d = niv01;
				}else {
					d = niv00;
				}
			}
			res = crear(a,b,c,d);
			return res;
		}else {
			return res;
		}
		
	}
	/**
	 * Imprime el cuadrante con todas sus células.
	 */
	public void imprimirCuadranteEntero () {
		System.out.println();
		if(pob ==0) {
			System.out.println("Todas las células han muerto.");
		}else {
			for (int j = 0; j < pow(2,niv); j++) {
	
				for (int i = 0; i < pow(2,niv); i++) {
					if (getPixel(i, j) == 1) {
						System.out.print('X');
					}else {
						System.out.print('.');
					}
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	/**
	 * Imprime el tamaño mínimo del cuadrante en el que están contenidas todas las células vivas.
	 */
	public void imprimirCuadrante () {
		System.out.println();
		long minX = (long)pow(2,niv) - 1;
		long maxX = 0;
		long minY = (long)pow(2,niv) - 1;
		long maxY = 0;
		for (int j = 0; j < pow(2,niv); j++) {
	
			for (int i = 0; i < pow(2,niv); i++) {
				if (getPixel(i, j) == 1) {
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
		
		if (maxX == 0) {
			maxX = (long)pow(2,niv) - 1;
			minX = 0;
			maxY = (long)pow(2,niv) - 1;
			minY = 0;
		}
		if(pob ==0) {
			System.out.println("Todas las células han muerto.");
		}else {
			for (long j = minY; j <= maxY; j++) {
		
				for (long i = minX; i <= maxX; i++) {
					if (getPixel(i, j) == 1) {
						System.out.print('X');
					}else {
						System.out.print('.');
					}
				}
				System.out.println();
			}
			System.out.println();
		}
	}	
}
