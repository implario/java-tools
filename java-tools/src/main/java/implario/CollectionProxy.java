package implario;

import java.util.Collection;
import java.util.Iterator;

public interface CollectionProxy<E> extends Collection<E> {

	Collection<E> getCollection();

	@Override
	default int size() {
		return this.getCollection().size();
	}

	@Override
	default boolean isEmpty() {
		return this.getCollection().isEmpty();
	}

	@Override
	default boolean contains(Object o) {
		return this.getCollection().contains(o);
	}

	@Override
	default Iterator<E> iterator() {
		return this.getCollection().iterator();
	}

	@Override
	default Object[] toArray() {
		return this.getCollection().toArray();
	}

	@Override
	default boolean add(E o) {
		return this.getCollection().add(o);
	}

	@Override
	default boolean remove(Object o) {
		return this.getCollection().remove(o);
	}

	@Override
	default boolean addAll(Collection<? extends E> collection) {
		return this.getCollection().addAll(collection);
	}

	@Override
	default void clear() {
		this.getCollection().clear();
	}

	@Override
	default boolean retainAll(Collection<?> collection) {
		return this.getCollection().retainAll(collection);
	}

	@Override
	default boolean removeAll(Collection<?> collection) {
		return this.getCollection().removeAll(collection);
	}

	@Override
	default boolean containsAll(Collection<?> collection) {
		return this.getCollection().containsAll(collection);
	}

	@Override
	@SuppressWarnings ("SuspiciousToArrayCall")
	default <T> T[] toArray(T[] array) {
		return this.getCollection().toArray(array);
	}

}
