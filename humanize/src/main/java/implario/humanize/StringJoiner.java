package implario.humanize;

public interface StringJoiner {

	StringJoiner AND_RUSSIAN = simpleAnd("и");
	StringJoiner AND_ENGLISH = simpleAnd("and");

	static StringJoiner simple(String prefix, String delimiter, String suffix) {
		return strings -> {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < strings.length; i++) {
				builder.append(prefix);
				builder.append(strings[i]);
				builder.append(suffix);
				if (i < strings.length - 1) builder.append(delimiter);
			}
			return builder.toString();
		};
	}

	static StringJoiner simpleAnd(String and) {
		return strings -> {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < strings.length; i++) {
				builder.append(strings[i]);
				if (i < strings.length - 2) builder.append(", ");
				else if (i == strings.length - 2) builder.append(' ').append(and).append(' ');
			}
			return builder.toString();
		};
	}

	String join(String... strings);

}
