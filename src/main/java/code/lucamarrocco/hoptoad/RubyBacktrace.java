package code.lucamarrocco.hoptoad;

import java.text.*;
import java.util.*;

public class RubyBacktrace extends Backtrace {

	public RubyBacktrace(Throwable throwable) {
		super(throwable);
	}

	public RubyBacktrace() {
		super();
	}

	protected RubyBacktrace(List<String> backtrace) {
		super(backtrace);
	}

	protected String toBacktrace(String className, String fileName, int lineNumber, String methodName) {
		if (className.matches(".*\\." + fileName.replaceAll(".java$", ""))) {
			return MessageFormat.format("at {0}.java:{1}:in `{2}''", className, lineNumber, methodName);
		}
		return MessageFormat.format("at {0}, {1}:{2}:in `{3}''", className, fileName, lineNumber, methodName);
	}

	public Backtrace newBacktrace(Throwable throwable) {
		return new RubyBacktrace(throwable);
	}

}
