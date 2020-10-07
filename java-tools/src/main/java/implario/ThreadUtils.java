package implario;

public class ThreadUtils {

	public static void notify(Object lock) {
		synchronized (lock) {
			lock.notify();
		}
	}

	public static void wait(Object lock) {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException ignored) {}
		}
	}

	/**
	 * Оберётка для метода Thread.sleep();
	 * @param millis Кол-во милисекунд для задержки
	 * @param onInterruption Действие, которое следует выполнить при преждевременном прерывании сна,
	 *                       Например освободить ресурсы
	 * @return был ли сон прерван.
	 */
	public static boolean sleep(long millis, Runnable onInterruption) {
		try {
			Thread.sleep(millis);
			return false;
		} catch (InterruptedException e) {
			return true;
		}
	}

}
