package clepto;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.*;

@UtilityClass
public class LoggerUtils {

	public static Logger simpleLogger(String name) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy.MM.dd, HH:mm:ss] ");
		return simpleLogger(name, lr -> {
			List<String> lines = new ArrayList<>();
			lines.add(lr.getMessage());


			if (lr.getThrown() != null) {

				StringWriter writer = new StringWriter();
				PrintWriter printer = new PrintWriter(writer);
				lr.getThrown().printStackTrace(printer);
				printer.close();

				String wrote = writer.toString();
				lines.addAll(Arrays.asList(wrote.replace("\r", "").split("\n")));
			}


			StringBuilder builder = new StringBuilder();
			for (String line : lines) {
				builder.append(dateFormat.format(new Date(lr.getMillis())))
						.append(lr.getLoggerName())
						.append(" (")
						.append(lr.getLevel().getName())
						.append("): ")
						.append(line)
						.append("\r\n");
			}

			return builder.toString();
		});
	}

	public static Logger simpleLogger(String name, Function<LogRecord, String> formatter) {
		Logger logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		logger.addHandler(new SimpleConsoleHandler(formatter));
		return logger;
	}

	public static class SimpleConsoleHandler extends StreamHandler {

		public SimpleConsoleHandler(Function<LogRecord, String> formatter) {
			super(System.out, new SimpleFormatter() {
				@Override
				public synchronized String format(LogRecord lr) {
					return formatter.apply(lr);
				}
			});
		}

		@Override
		public void publish(LogRecord record) {
			super.publish(record);
			flush();
		}

		@Override
		public void close() {
			flush();
		}

	}

}
