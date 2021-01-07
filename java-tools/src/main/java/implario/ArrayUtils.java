package implario;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtils {

	public static int referenceIndexOf(Object[] array, Object element) {
		for (int i = 0; i < array.length; i++) if (array[i] == element) return i;
		return -1;
	}

}
