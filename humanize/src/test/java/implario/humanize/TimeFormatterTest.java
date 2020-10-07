package implario.humanize;

import org.junit.Test;

import java.time.Duration;

import static implario.humanize.TimeFormatter.Interval.*;
import static org.junit.Assert.assertEquals;

public class TimeFormatterTest {

	public final Duration example = Duration.ofDays(6).plusHours(23).plusMinutes(4);

	@Test
	public void testDefault() {
		assertEquals("6 дней, 23 часа и 4 минуты",
				TimeFormatter.builder().build().format(example));
	}

	@Test
	public void testShortForm() {
		assertEquals("6 д. 23 ч. 4 м.",
				TimeFormatter.builder().shortForm(true).build().format(example));
	}

	@Test
	public void testCustomSpace() {
		assertEquals("6д. 23ч. 4м.",
				TimeFormatter.builder().space("").shortForm(true).build().format(example));
	}

	@Test
	public void testCustomJoiner() {
		StringJoiner customJoiner = StringJoiner.simple("§e", "§f, ", "");
		assertEquals("§e6 дней§f, §e23 часа§f, §e4 минуты",
				TimeFormatter.builder().joiner(customJoiner).build().format(example));

	}

	@Test
	public void testCustomAccuracy() {
		assertEquals("6 дней",
				TimeFormatter.builder().accuracy(0).build().format(example));

		assertEquals("6 дней и 23 часа",
				TimeFormatter.builder().accuracy(60).build().format(example));

		assertEquals("6 дней, 23 часа и 4 минуты",
				TimeFormatter.builder().accuracy(10000).build().format(example));
	}

	@Test
	public void testCustomFilters() {
		assertEquals("6 дней, 23 часа и 240 секунд",
				TimeFormatter.builder().excludeIntervals(MINUTE).build().format(example));

		assertEquals("10024 минуты",
				TimeFormatter.builder().intervals(MINUTE).build().format(example));

	}

	@Test
	public void test() {
		TimeFormatter formatter = TimeFormatter.builder().excludeIntervals(MINUTE).build();
		for (int i = 0; i < 1000000; i++) formatter.format(example); // warmup
		int ops = 10000000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < ops; i++) formatter.format(example);
		long end = System.currentTimeMillis();
		System.out.println("Finished in " + (end - start) + " ms, speed is " + ((double) (end - start) / ops * 1000_000) + " ns/op");
	}


}
