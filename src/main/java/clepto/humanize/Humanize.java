package clepto.humanize;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Humanize {

	public static String plurals(String one, String couple, String many, int n) {
		return n % 10 == 1 && n % 100 != 11 ? one :
				n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? couple : many;
	}

	private static final DecimalFormat format = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###,###");

	public static String separate(long number, int groupLength, char separator) {

		DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance();
		instance.setGroupingSeparator(separator);
		format.setDecimalFormatSymbols(instance);

		return format.format(number);

	}

}
