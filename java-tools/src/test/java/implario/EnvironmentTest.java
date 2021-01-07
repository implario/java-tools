package implario;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnvironmentTest {

	public static final String textKey = "text";
	public static final String textValue = "hello";

	public static final String numericKey = "numeric";
	public static final int numericValue = 123;

	public static final String missingKey = "missing";

	@BeforeAll
	public static void setup() throws Exception {
		Environment.setEnv(textKey, textValue);
		Environment.setEnv(numericKey, String.valueOf(numericValue));
	}

	@Test
	public void testGet() {

		String defaultValue = "default";

		assertEquals(textValue, Environment.get(textKey));

		assertNull(Environment.get(missingKey));

		assertEquals(textValue, Environment.get(textKey, defaultValue));

		assertEquals(defaultValue, Environment.get(missingKey, defaultValue));

	}

	@Test
	public void testRequire() {

		assertEquals(textValue, Environment.require(textKey));

		assertThrows(Environment.MissingVariableException.class,
				() -> Environment.require(missingKey));

	}

	@Test
	public void testGetInt() {

		int defaultValue = 456;

		assertEquals(numericValue, Environment.getInt(numericKey, defaultValue));

		assertEquals(defaultValue, Environment.getInt(textKey, defaultValue));

		assertEquals(defaultValue, Environment.getInt(missingKey, defaultValue));

	}

	@Test
	public void testRequireInt() {

		assertEquals(numericValue, Environment.requireInt(numericKey));

		assertThrows(Environment.MissingVariableException.class,
				() -> Environment.requireInt(missingKey));

		assertThrows(Environment.InvalidValueException.class,
				() -> Environment.requireInt(textKey));


	}

	@Test
	public void testRequireIntRange() {

		assertEquals(numericValue, Environment.requireInt(numericKey, numericValue - 1, numericValue + 1));

		assertEquals(numericValue, Environment.requireInt(numericKey, numericValue, numericValue));

		assertThrows(Environment.InvalidValueException.class,
				() -> Environment.requireInt(numericKey, numericValue + 1, numericValue + 1));

	}

}
