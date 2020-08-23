package clepto;

public class OOOOOO {

	private static final String[][] words = {
			{
					"таинственный",
					"ласковый",
					"сказочный",
					"пряничный",
					"серебрянный",
					"каменный",
					"пламенный",
					"изичный",
					"украденный",
					"захваченный",
					"положенный",
					"признательный",
					"связанный",
					"брошенный",
					"уверенный",
					"отчаянный",
					"изнеженный",
					"контуженный",
					"заботливый"
			},
			{
					"лучик",
					"тортик",
					"пряник",
					"крестик",
					"мальчик",
					"пончик",
					"кластер",
					"кратер",
					"билдер",
					"модер",
					"хелпер",
					"пластырь",
					"автор",
					"костя",
					"миша",
					"творог",
					"сахар",
					"отблеск",
					"шепот",
			},
			{
					"дремучего",
					"поющего",
					"вонючего",
					"изящного",
					"стеклянного",
					"кошмарного",
					"подпольного",
					"полярного",
					"ненужного",
					"хрустального",
					"волшебного",
					"нахального",
					"красивого",
					"рабочего",
					"упавшего",
					"дешевого",
					"несчастного",
					"линейного",
					"огромного",
					"токсичного",
					"четвертого"
			},
			{
					"жира",
					"мира",
					"бана",
					"леса",
					"кода",
					"снега",
					"джона",
					"пепла",
					"ветра",
					"храма",
					"шрама",
					"дарка",
					"фикса",
					"зикки",
					"кости",
			}
	};

	private static final String[][][] styles = {
			{
					{"a", "A"},
					{"b", "B"},
					{"v", "V"},
					{"g", "G"},
					{"d", "D"},
					{"e", "E"},
					{"zh", "zH", "Zh", "ZH"},
					{"z", "Z"},
					{"i", "I"},
					{"i", "I"},
					{"k", "K"},
					{"l", "L"},
					{"m", "M"},
					{"n", "N"},
					{"o", "O"},
					{"p", "P"},
					{"r", "R"},
					{"s", "S"},
					{"t", "T"},
					{"u", "U"},
					{"f", "F"},
					{"kh", "kH", "Kh", "KH"},
					{"c", "C", "ts", "TS", "tS", "Ts"},
					{"ch", "CH", "cH", "Ch"},
					{"sh", "SH", "sH", "Sh"},
					{"sch", "ScH", "sCH", "SCH"},
					{""},
					{"y", "Y"},
					{""},
					{"e", "E"},
					{"yu", "YU", "yU", "Yu"},
					{"ya", "YA", "yA", "Ya"},
			},
			{
					{"a", "A"},
					{"6"},
					{"B"},
					{"r"},
					{"g"},
					{"e", "E"},
					{"zh", "ZH"},
					{"3"},
					{"u"},
					{"u"},
					{"k", "K"},
					{"L"},
					{"M"},
					{"H"},
					{"O"},
					{"n"},
					{"p", "P"},
					{"c", "C"},
					{"T"},
					{"y"},
					{"F"},
					{"x"},
					{"Tc", "TC"},
					{"4"},
					{"w"},
					{"W", "w"},
					{"b"},
					{"bl", "bI"},
					{"b"},
					{"e", "E"},
					{"IO"},
					{"9I", "9l"},

			}
	};

	public static String mojaOborona() {
		String[][] style = ListUtils.random(styles);
		StringBuilder builder = new StringBuilder();
		randomizeWord("оооооооооо", style, builder);
		builder.append(' ');
		randomizeWord("моя", style, builder);
		builder.append(' ');
		randomizeWord("оборона", style, builder);
		builder.append(' ');
		for (int i = 0; i < words.length; i++) {
			if (i > 0) builder.append('_');
			randomizeWord(ListUtils.random(words[i]), style, builder);
		}
		return builder.toString();
	}

	public static void randomizeWord(String word, String[][] style, StringBuilder dst) {
		for (char c : word.toCharArray()) {
			int id = c - 'а';
			if (id < 0 || id >= style.length) dst.append(c);
			else dst.append(ListUtils.random(style[id]));
		}
	}

}
