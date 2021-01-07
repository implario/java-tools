package implario.math;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class V3GsonTest {

	@Test
	public void testV3Gson() {
		Gson gson = new Gson();
		V3 example = V3.of(1, 2, -3);
		String json = gson.toJson(example);
		System.out.println(json);
		V3 parsed = gson.fromJson(json, V3.Impl.class);
		assertEquals(example, parsed);
	}

}
