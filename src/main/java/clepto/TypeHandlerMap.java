package clepto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings ({"unchecked", "rawtypes"})
public class TypeHandlerMap<T> {

	private final Map<Class<? extends T>, Consumer> map = new HashMap<>();

	public TypeHandlerMap<T> register(Class<? extends T> type, Consumer<? extends T> consumer) {
		this.map.put(type, consumer);
		return this;
	}

	public void execute(T object) {
		Consumer consumer = map.get(object.getClass());
		if (consumer != null) consumer.accept(object);
	}


}
