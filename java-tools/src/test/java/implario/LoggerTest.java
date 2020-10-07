package implario;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest {

	@Test
	public void testLogger() {
		Logger logger = LoggerUtils.simpleLogger("TEST");
		logger.info("This is an info message.");
		logger.fine("This is a fine message.");
		logger.warning("This is a warning.");
		logger.severe("This is an error.");
		logger.log(Level.SEVERE, "This is an exception:", new Exception());
	}


}
