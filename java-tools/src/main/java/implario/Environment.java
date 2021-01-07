package implario;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

@UtilityClass
public class Environment {

	public static String get(String env) {
		return System.getenv(env);
	}

	public static String get(String env, String defaultValue) {
		String value = System.getenv(env);
		return value != null ? value : defaultValue;
	}

	public static String require(String env) {
		String value = System.getenv(env);
		if (value == null) throw new MissingVariableException(env);
		return value;
	}

	public static int getInt(String env, int defaultValue) {
		try {
			return Integer.parseInt(get(env));
		} catch (NullPointerException | NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static int requireInt(String env) {
		return requireInt(env, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public static int requireInt(String env, int min, int max) {
		String value = require(env);
		try {
			int intValue = Integer.parseInt(value);
			if (intValue < min || intValue > max)
				throw new InvalidValueException("Value of " + env + " (" + intValue + ") is out of range " + min + "-" + max);
			return intValue;
		} catch (NumberFormatException ex) {
			throw new InvalidValueException("Value of " + env + " (" + value + ") is not a number");
		}
	}

	public static class MissingVariableException extends RuntimeException {

		public MissingVariableException(String variable) {
			super("Environment variable '" + variable + "' is missing!");
		}

	}

	public static class InvalidValueException extends RuntimeException {

		public InvalidValueException(String message) {
			super(message);
		}

	}

	@SuppressWarnings ({"unchecked", "rawtypes"})
	public static void setEnv(String key, String value) throws Exception {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.put(key, value);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.put(key, value);
		} catch (NoSuchFieldException e) {
			Class[] classes = Collections.class.getDeclaredClasses();
			Map<String, String> env = System.getenv();
			for (Class cl : classes) {
				if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
					Field field = cl.getDeclaredField("m");
					field.setAccessible(true);
					Object obj = field.get(env);
					Map<String, String> map = (Map<String, String>) obj;
					map.clear();
					map.put(key, value);
				}
			}
		}
	}

}
