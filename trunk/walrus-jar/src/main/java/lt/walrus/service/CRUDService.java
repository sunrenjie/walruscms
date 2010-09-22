package lt.walrus.service;

/**
 * Standard CRUD service interface
 */
public interface CRUDService<T> {

	public void add(T o);

	public void update(T o);

	public void delete(T o);

	public T load(String id);

}