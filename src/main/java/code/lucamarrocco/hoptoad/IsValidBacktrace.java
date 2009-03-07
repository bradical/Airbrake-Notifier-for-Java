package code.lucamarrocco.hoptoad;

import org.hamcrest.*;

public class IsValidBacktrace<T> extends BaseMatcher<T> {

	@Factory
	public static final <T> Matcher<T> validBacktrace() {
		return new IsValidBacktrace();
	}

	public void describeTo(Description description) {
		description.appendText("valid backtrace");
	}

	public boolean matches(Object item) {
		return item.toString().matches("[^:]*:\\d+.*");
	}

}
