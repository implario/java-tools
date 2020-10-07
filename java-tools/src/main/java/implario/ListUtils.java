package implario;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class ListUtils {

	public static final Random random = new Random();

	public static <A> List<A> filter(Iterable<A> list, Predicate<A> predicate) {
		if (list == null) return null;
		List<A> mapped = new ArrayList<>();
		for (A object : list) if (predicate.test(object)) mapped.add(object);
		return mapped;
	}

	public static <A, B> List<B> map(Iterable<A> list, Function<A, B> mapping) {
		if (list == null) return null;
		List<B> mapped = new ArrayList<>();
		for (A object : list) mapped.add(mapping.apply(object));
		return mapped;
	}

	public static <A, B> List<B> mapIgnoringNull(Iterable<A> list, Function<A, B> mapping) {
		if (list == null) return null;
		List<B> mapped = new ArrayList<>();
		for (A objectA : list) {
			if (objectA == null) continue;
			B objectB = mapping.apply(objectA);
			if (objectB == null) continue;
			mapped.add(objectB);
		}
		return mapped;
	}

	@SafeVarargs
	public static <T> List<T> newArrayList(T... content) {
		List<T> list = new ArrayList<>();
		Collections.addAll(list, content);
		return list;
	}

	public static <T> Iterator<T> loop(Iterable<T> iterable) {
		return new IterableLoop<>(iterable);
	}

	public static <T> Iterator<T> loop(T[] array) {
		if (array == null) throw new NullPointerException("array");
		return new ArrayLoop<>(array);
	}

	public static <T> T random(List<T> list) {
		if (list == null || list.isEmpty()) return null;
		int size = list.size();
		return list.get(size == 1 ? 0 : random.nextInt(size));
	}

	public static <T> T random(T[] array) {
		if (array == null || array.length == 0) return null;
		return array[random.nextInt(array.length)];
	}

	private static class IterableLoop<T> implements Iterator<T> {

		private final Iterable<T> collection;
		private final boolean hasElements;
		private Iterator<T> iterator;

		public IterableLoop(Iterable<T> collection) {
			this.collection = collection;
			this.iterator = collection.iterator();
			this.hasElements = iterator.hasNext();

		}

		@Override
		public boolean hasNext() {
			return hasElements;
		}

		@Override
		public T next() {
			return iterator.hasNext() ? iterator.next() : (iterator = collection.iterator()).next();
		}

	}

	private static class ArrayLoop<T> implements Iterator<T> {
		private final T[] array;
		private int pos = 0;

		public ArrayLoop(T[] array) {
			this.array = array;
		}

		public T next() {
			if (this.array.length == this.pos) this.pos = 0;
			return this.array[this.pos++];
		}

		public boolean hasNext() {
			return array.length > 0;
		}
	}


}
