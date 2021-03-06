package logic.data_structures;
import java.util.Arrays;
import java.util.Iterator;

import API.IHashTable;
import logic.exceptions.KeyNotFoundException;

/**
 * Clase que representa una tabla de hash que usa Linear Probing para la resolucion de conflictos
 * @param <K> Llave asociada a un objeto V
 * @param <V> Tipo de elementos que se encuentran en la tabla
 * @author pa.suarezm
 */
public class LPHashTable<K, V>  implements IHashTable<K,V>{

	//------------------------------------------
	//Constantes
	//------------------------------------------
	/**
	 * Factor de carga maximo para la tabla. Para Linear Probing debe ser 0.75
	 */
	public static final double FACTOR_DE_CARGA_MAX = 0.75;
	
	//------------------------------------------
	//Metodos
	//------------------------------------------
	
	/**
	 * Tama�o de la tabla = values.length = keys.length
	 * Deberia ser un numero primo
	 */
	private int sizeM;
	
	/**
	 * Tama�o de los datos que se van a agregar
	 */
	private int sizeN;
	
	/**
	 * Valores usados para calcular el factor de carga
	 */
	private double N,M;
	
	/**
	 * Arreglo que contiene todos los valores de la tabla
	 */
	private V[] values;
	
	/**
	 * Arreglo que contiene todas las llaves de la tabla
	 */
	private K[] keys;
	
	//-------------------------------------------------------------------
	//Constructor
	//-------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	public LPHashTable(int M){
		/* 1. Inicializar los tama�os N y M
		 * 2. Inicializar los arreglos values y keys
		 */
		
		//1.
		sizeN = 0;
		sizeM = M;
		N = 0;
		this.M = M;
		//2.
		this.values = (V[]) new Object[sizeM];
		this.keys = (K[]) new Object[sizeM];
		
		reset();
	}
	
	//--------------------------------------------------------------------
	//Metodos (La respectiva documentacion se encuentra en IHashTable)
	//--------------------------------------------------------------------

	public void put(K key, V value) throws NullPointerException {
		
		/* 1. Si value es null, lanzar NullPointerException
		 * 2. Si el factor de carga es superado, hace un rehash
		 * 3. Busca una posicion en la cual agregar el valor. Si encuentra en el arreglo de llaves una
		 *    llave igual a key, reemplaza el valor en values por value
		 * 4. Si no encontro una llave igual en 3., va a estar parado en una posicion vacia
		 *    Si llego a este punto entonces pone la llave y el valor en la posicion encontrada en
		 *    keys y values respectivamente
		 */
		
		//1.
		if(value == null)
			throw new NullPointerException("No puede agregar elementos nulos a la tabla");
		
		//2.
		if(((N+1)/M) >= FACTOR_DE_CARGA_MAX){
			rehash();
		}
		
		//3.
		int i = hash(key);
		for(; (this.keys[i]!=null) && (values[i]!=null) ; i = (i+1)%sizeM){
			if(this.keys[i].equals(key)){
				values[i] = value;
				return;
			}
		}
		
		//4.	
		this.keys[i] = key;
		this.values[i] = value;
		sizeN++;
		N = sizeN;
	}
	
	/**
	 * Funcion de rehash para la tabla en caso de que el factor de carga maximo sea superado
	 */
	@SuppressWarnings("unchecked")
	private void rehash(){
		/* 1. Va a guardar una copia de los valores en values y de las llaves en keys y del tama�o actual M
		 * 2. Usando el tama�o N actual se calcula el siguiente primo mayor a 2N el cual sera el nuevo M
		 * 3. Los arreglos values y de keys se vuelven a inicializar, esta vez con el tama�o M
		 *    calculado en 3.
		 * 4. Reinicia el conteo de objetos N. Usando la copia de los valores guardados en 1., se 
		 *    agregan de nuevo a la tabla
		 */
		
		//1.
		V[] copiaValores = values;
		K[] copiaLlaves = keys;
		int antiguoM = sizeM;
		
		//2.
		sizeM = calcularSiguientePrimo((2*sizeN));
		M = sizeM;
		
		//3.
		values = (V[]) new Object[sizeM];
		keys = (K[]) new Object[sizeM];
		
		//4.
		sizeN = 0;
		N = sizeN;
		
		for(int i=0; i<antiguoM; i++){
			if(copiaLlaves[i]!=null)
				put(copiaLlaves[i], copiaValores[i]);	
		}
	}

	/**
	 * Calcula el valor de la llave de un objeto determinado
	 * @param value El objeto al cual se le quiere calcular la llave
	 * @return La llave generada usando los numeros primos 8087 y 4013
	 * (fuente de numeros primos: https://mathcs.clarku.edu/~djoyce/numbers/primes.html)
	 */
	private int hash(K key){
		return ((key.hashCode() & 0x7fffffff) % sizeM);
	}
	
	
	public V get(K key) throws KeyNotFoundException{
		/* 1. Buscar a que indice corresponde la llave que entra por parametro
		 * 2. Si no se encuentra el valor, lanzar exception
		 */
		
		//1.
		
		for(int i = hash(key); this.keys[i] != null; i = ((i+1)%sizeM)){			
			if(keys[i].equals(key))
				return values[i];
		}		
		
		//2.
		throw new KeyNotFoundException(key.toString());
	}
	
	
	public V delete(K key) {
		/* 1. Buscar a que indice corresponde la llave que entra por parametro
		 * 2. Guardar el valor encontrado
		 * 3. Borrar el valor de values
		 * 4. Actualiza la cantidad N de elementos
		 * 5. Retorna el valor guardado en 2.
		 */
		
		 V buscado = null;
		
		//1.
		for(int i = hash(key); keys[i] != null && buscado == null; i = ((i+1)%sizeM)){
			if(keys[i].equals(key)){
				buscado = values[i]; //2.
				values[i] = null; //3.
			}
		}
		
		//4.
		if(buscado != null){
			sizeN--;
			N=sizeN;
		}
		
		//5.
		return buscado;
	}

	
	public Iterator<K> keys() {
		return Arrays.asList(keys).iterator();
	}
	
	/**
	 * Usando el postulado de Bertrand (https://en.wikipedia.org/wiki/Bertrand%27s_postulate)
	 * calcula el menor numero primo menor que n.
	 * @param n Entero positivo
	 * @return Entero positivo entre n y 2n-2. Devuelve -1 si fall� (que no deberia)
	 */
	private static int calcularSiguientePrimo(int n){
		int p = -1;
		
		if(n%2==0)  //Si n es par, va a sumarle 1 para que la iteracion empiece en un numero impar y
			n++;    //al sumarle 2 el siguiente tambien sera impar
		if(esPrimo(n)) //Si n es primo, el ciclo va a hacer que el primo que encuentre sea
			n+=2;        //el mismo n y eso no es lo que quiero que haga el algoritmo	
		
		for(int i=n; i<((2*n)-2) && p==-1; i+=2){
			if(esPrimo(i))
				p = i;
		}
		
		return p;
	}
	
	/**
	 * Verifica si un entero p es primo o no
	 * @param p Entero positivo impar
	 * @return True si es primo. False si no lo es o si se sale del rango (0<p<100000000)
	 */
	private static boolean esPrimo(int p){
		if(p >= 100000000 || p <= 0 || p%2==0)
			return false;
		
		for(int i=3; i<p; i+=2){
			if(p%i==0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Devuelve el tama�o total de la tabla (contando posiciones vacias)
	 * @return Atributo sizeM de la tabla
	 */
	public int getM(){
		return sizeM;
	}
	
	/**
	 * Devuelve la cantidad de duplas que hay en la tabla
	 */
	public int getN(){
		return sizeN;
	}
	
	/**
	 * Devuelve el arreglo de llaves
	 */
	public K[] keysAsArray(){
		return keys.clone();
	}
	
	public boolean isEmpty(){
		return (sizeN == 0);
	}
	
	public void reset(){
		for(int i=0; i<keys.length; i++){
			keys[i] = null;
			values[i] = null;
		}
	}

}
