package clepto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoveHumans {
//
//	public static String formatTime(long time) {
//		StringBuilder sb = new StringBuilder();
//		boolean formattedOnce = false;
//		if (time > 86400_000) {
//			long days = time / 86400_000;
//			sb.append(days).append(plurals(" день ", " дня ", " дней ", (int) days));
//			time %= 86400_000;
//			formattedOnce = true;
//		}
//
//		if (time > 3600_000) {
//			long hours = time / 3600_000;
//			sb.append(hours).append(plurals(" час ", " часа ", " часов ", (int) hours));
//			time %= 3600_000;
//			if (formattedOnce) return sb.toString();
//			formattedOnce = true;
//		}
//
//		if (time > 60_000) {
//			long minutes = time / 60_000;
//			sb.append(minutes).append(plurals(" минуту ", " минуты ", " минут ", (int) minutes));
//			time %= 60_000;
//			if (formattedOnce) return sb.toString();
//			formattedOnce = true;
//		}
//
//		if (time > 1_000) {
//			long seconds = time / 1_000;
//			sb.append(seconds).append(plurals(" секунду ", " секунды ", " секунд ", (int) seconds));
//			time %= 1_000;
//			if (formattedOnce) return sb.toString();
//		}
//
//		return sb.append(time).append(" мс.").toString();
//
//	}


	/**
	 * @param duration 10s, 5m, 12h, 3d, 1w
	 * @return 10 * 1000, 5 * 60 * 1000, 12 * 60 * 60 * 1000, 3 * 24 * 60 * 60 * 1000, 1 * 7 * 24 * 60 * 60 * 1000
	 */
	public static long durationToMillis(String duration) {
		if (duration == null || duration.isEmpty()) return 0;
		char flag = duration.charAt(duration.length() - 1);

		if (flag >= '0' && flag <= '9')
			return Long.parseLong(duration) * 60_000;

		long multiplier;
		switch (flag) {
			case 'S':
			case 's':
				multiplier = 1000;
				break;
			case 'm':
			case 'M':
			default:
				multiplier = 1000 * 60;
				break;
			case 'h':
			case 'H':
				multiplier = 1000 * 60 * 60;
				break;
			case 'd':
			case 'D':
				multiplier = 1000 * 60 * 60 * 24;
				break;
			case 'w':
			case 'W':
				multiplier = 1000 * 60 * 60 * 24 * 7;
				break;
			case 'i':
				multiplier = 1;
				break;
		}

		if (duration.length() == 1) return multiplier;

		String digits = duration.substring(0, duration.length() - 1);
		try {
			return (long) (Double.parseDouble(digits) * multiplier);
		} catch (NumberFormatException ex) {
			return multiplier;
		}
	}

	public static String formatSeconds(int seconds) {
		String min = String.valueOf(seconds / 60);
		String sec = String.valueOf(seconds % 60);
		return (min.length() < 2 ? "0" : "") + min + (sec.length() < 2 ? ":0" : ":") + sec;
	}

	public static String join(String comma, String and, String... array) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i < array.length - 2) builder.append(comma);
			if (i == array.length - 2) builder.append(and);
		}
		return builder.toString();
	}

	public static String sup(int value) {
		String s = String.valueOf(value);
		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c == '1') b.append('\u00b9');
			else if (c == '2' || c == '3') b.append((char) (c + 128));
			else if (c >= '0' && c <= '9') b.append((char) (c + 8256));
			else b.append(c);
		}
		return b.toString();
	}

	public static String healthToHearts(double health) {
		return (int) (health * 5) / 10.0 + " ❤";
	}

}
