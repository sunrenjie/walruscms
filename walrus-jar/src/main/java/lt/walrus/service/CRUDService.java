package lt.walrus.service;

/**
 * Standard CRUD service interface
 */
public interface CRUDService<T> extends SaveService<T> {

	public void add(T o);

	public void delete(T o);

	public T get(long id);
}