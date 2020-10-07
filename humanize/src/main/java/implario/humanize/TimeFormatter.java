package implario.humanize;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

import java.time.Duration;
import java.util.*;

@Data
@RequiredArgsConstructor
public class TimeFormatter {

	private final Collection<Interval> intervals;
	private final boolean shortForm;
	private final StringJoiner joiner;
	private final String space;
	private final double accuracy;
	private Interval[] sortedIntervals0;

	public TimeFormatter(Collection<Interval> intervals, boolean fullForm) {
		this(intervals, fullForm, fullForm ? StringJoiner.AND_RUSSIAN : StringJoiner.simple("", " ", "."), " ", Double.POSITIVE_INFINITY);
	}

	protected Interval[] getSortedIntervals() {
		if (this.sortedIntervals0 != null) return this.sortedIntervals0;
		ArrayList<Interval> sorted = new ArrayList<>(this.intervals);
		sorted.sort(Comparator.comparingLong(Interval::getNanos).reversed());
		return sorted.toArray(new Interval[0]);
	}

	public String format(Duration duration) {
		return this.formatNanos(duration.toNanos());
	}

	public String formatNanos(long nanos) {
		long counted = 0;
		Interval[] sortedIntervals = getSortedIntervals();
		List<String> elements = new ArrayList<>(sortedIntervals.length);
		for (Interval interval : sortedIntervals) {
			long value = nanos / interval.nanos;
			if (value == 0) continue;

			String word = shortForm ? interval.shortcut : interval.plurals(value);
			String result = value + this.space + word;
			elements.add(result);

			long subracted = value * interval.nanos;
			counted += subracted;
			nanos -= subracted;
			if ((double) counted / nanos > this.accuracy) break;

		}

		return this.joiner.join(elements.toArray(new String[0]));

	}

	@Getter
	@RequiredArgsConstructor
	public enum Interval {

		NANOSECOND(1, "наносекунда", "наносекунды", "наносекунд", "нс"),
		MILLISECOND(1000 * 1000, "милисекунда", "милисекунды", "милисекунд", "мс"),
		TICK(MILLISECOND.nanos * 50, "тик", "тика", "тиков", "т"),
		SECOND(TICK.nanos * 20, "секунда", "секунды", "секунд", "с"),
		MINUTE(SECOND.nanos * 60, "минута", "минуты", "минут", "м"),
		HOUR(MINUTE.nanos * 60, "час", "часа", "часов", "ч"),
		DAY(HOUR.nanos * 24, "день", "дня", "дней", "д"),
		MONTH(DAY.nanos * 30, "месяц", "месяца", "месяцев", "мес"),
		YEAR(DAY.nanos * 365, "год", "года", "лет", "г");

		private final long nanos;
		private final String nominative;
		private final String genitiveSingular;
		private final String genitivePlural;
		private final String shortcut;

		public String plurals(long n) {
			if (n < 0) n = -n;
			return n % 10 == 1 && n % 100 != 11 ? this.nominative :
					n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? this.genitiveSingular : this.genitivePlural;
		}


	}

	public static Builder builder() {
		return new Builder();
	}

	@Setter
	@Accessors(fluent = true, chain = true)
	public static class Builder {
		private Collection<Interval> intervals = EnumSet.allOf(Interval.class);
		private boolean shortForm = false;
		private StringJoiner joiner;
		private String space = " ";
		private double accuracy = Double.POSITIVE_INFINITY;

		public TimeFormatter build() {
			if (joiner == null) joiner = shortForm ? StringJoiner.simple("", " ", ".") : StringJoiner.AND_RUSSIAN;
			return new TimeFormatter(intervals, shortForm, joiner, space, accuracy);
		}

		@Tolerate
		public Builder intervals(Interval... intervals) {
			this.intervals = EnumSet.noneOf(Interval.class);
			this.intervals.addAll(Arrays.asList(intervals));
			return this;
		}

		public Builder excludeIntervals(Interval... intervals) {
			this.intervals.removeAll(Arrays.asList(intervals));
			return this;
		}

	}

}
