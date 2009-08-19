// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

import java.text.*;
import java.util.*;

public class RubyBacktrace extends Backtrace {

	public RubyBacktrace() {
		super();
	}

	protected RubyBacktrace(final List<String> backtrace) {
		super(backtrace);
	}

	public RubyBacktrace(final Throwable throwable) {
		super(throwable);
	}

	@Override
	public Backtrace newBacktrace(final Throwable throwable) {
		return new RubyBacktrace(throwable);
	}

	@Override
	protected String toBacktrace(final String className, final String fileName, final int lineNumber, final String methodName) {
		if (className.matches(".*\\." + fileName.replaceAll(".java$", ""))) return MessageFormat.format("at {0}.java:{1}:in `{2}''", className, lineNumber, methodName);
		return MessageFormat.format("at {0}, {1}:{2}:in `{3}''", className, fileName, lineNumber, methodName);
	}

}
