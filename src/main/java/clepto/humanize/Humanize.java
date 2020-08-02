package clepto.humanize;

public class Humanize {

	public static String plurals(String one, String couple, String many, int n) {
		return n % 10 == 1 && n % 100 != 11 ? one :
				n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? couple : many;
	}

}
