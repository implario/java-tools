package clepto.humanize;

import org.junit.Test;

public class SeparateTest {

	@Test
	public void testSeparate() {
		System.out.println(Humanize.separate(Long.MIN_VALUE, 3, '.'));
	}

}
