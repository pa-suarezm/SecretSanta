package API;

import java.util.Iterator;

import logic.exceptions.*;

/**
 * Interfaz para una tabla de hash generica. Debe cumplir las funciones
 * put(K,V), get(K), delete(K), keys()
 * @author pa.suarezm
 */
public interface IHashTable<K, V>{
	
	/**
	 * Agrega un elemento asociandolo a la llave dada
	 * "Agrega una dupla (K,V) a la tabla. Si la llave K existe, se reemplaza su valor V
	 * asociado. V no puede ser null."
	 * @param key Llave del elemento a agregar
	 * @param value Elemento a agregar
	 * @throws NullPointerException si value es null
	 */
	public void put(K key, V value) throws NullPointerException;
	
	/**
	 * Devuelve el elemento asociado a la llave que entra por parametro
	 * "Obtener el valor V asociado a la llave K. V no puede ser null."
	 * @param key Llave del elemento buscado
	 * @return Elemento asociado a la llave
	 */
	public V get(K key) throws KeyNotFoundException;
	
	/**
	 * Recupera el elemento asociado a la llave y lo elimina de la tabla
	 * "Borrar la dupla asociada a la llave K. Se obtiene el valor V asociado a la llave
	 * K. Se obtiene null si la llave K no existe."
	 * @param key Llave del elemento asociado
	 * @return Elemento eliminado. Devuelve null si la llave no esta asociada a nada dentro de la tabla
	 */
	public V delete(K key);
	
	/**
	 * Recupera el conjunto de todas las llaves
	 * "Conjunto de llaves K presentes en la tabla"
	 * @return Todas las llaves presentes en la tabla
	 */
	public Iterator<K> keys();
	
}

