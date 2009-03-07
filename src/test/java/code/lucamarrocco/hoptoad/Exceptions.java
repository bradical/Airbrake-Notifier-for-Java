package code.lucamarrocco.hoptoad;

public class Exceptions {
	protected static final String ERROR_MESSAGE = "undefined method `password' for nil:NilClass";

	public static final Exception newException(String errorMessage) {
		String string = null;
		try {
			string.toString();
		} catch (Exception e) {
			return new RuntimeException(errorMessage, e);
		}
		return newException(errorMessage);
	}

}
