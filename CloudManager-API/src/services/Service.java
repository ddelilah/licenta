package services;

import java.util.List;

import exceptions.ServiceCenterAccessException;

import util.ResponseMessage;

/**
 * Performs operations related to
 * @param <T>
 */
public abstract class Service<T> {

	
	/**Returns element by id
	 * @return T - element returned by id
	 */
	public abstract T getById(int id) throws ServiceCenterAccessException;
	
	/**Returns all T elements
	 * @return List<T> - list containing all T elements
	 */
	public abstract List<T> getAll() throws ServiceCenterAccessException ;
	
	/**Deletes element T
	 * @param t - element to delete
	 * @return response message from the cloud manager 
	 */
	public abstract ResponseMessage delete(T t) throws ServiceCenterAccessException ;
	
	/** Checks if element t is contained
	 * @param t - element to check if contained
	 * @return true if found, false otherwise
	 */
	public abstract boolean contains(T t);
	
}
