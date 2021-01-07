package implario.humanize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HumanizeTest {

	@Test
	public void testPlurals() {
		assertEquals(pluralExample(1), "алмаз");
		assertEquals(pluralExample(2), "алмаза");
		assertEquals(pluralExample(4), "алмаза");
		assertEquals(pluralExample(5), "алмазов");

		assertEquals(pluralExample(10), "алмазов");
		assertEquals(pluralExample(11), "алмазов");
		assertEquals(pluralExample(14), "алмазов");

		assertEquals(pluralExample(20), "алмазов");
		assertEquals(pluralExample(21), "алмаз");
		assertEquals(pluralExample(24), "алмаза");
		assertEquals(pluralExample(100), "алмазов");

		assertEquals(pluralExample(0), "алмазов");

		assertEquals(pluralExample(-1), "алмаз");
		assertEquals(pluralExample(-2), "алмаза");
		assertEquals(pluralExample(-5), "алмазов");
	}

	private String pluralExample(int n) {
		return Humanize.plurals("алмаз", "алмаза", "алмазов", n);
	}

}
