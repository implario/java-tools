package clepto.humanize;

public class Humanize {

	public static String plurals(String one, String two, String five, int n) {
		if (n < 0) n = -n;
		return n % 10 == 1 && n % 100 != 11 ? one :
				n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? two : five;
	}

}
