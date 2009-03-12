package code.lucamarrocco.hoptoad;

import java.util.*;


public class QuietRubyBacktrace extends RubyBacktrace {

	public QuietRubyBacktrace(Throwable throwable) {
		super(throwable);
	}

	protected void ignore() {
		ignoreCocoon();
		ignoreMozilla();
		ignoreSpringSecurity();
		ignoreMortbayJetty();
		ignoreJunit();
		ignoreEclipse();
		ignoreNoise();
	}

	public QuietRubyBacktrace() {
		super();
	}

	protected QuietRubyBacktrace(List<String> backtrace) {
		super(backtrace);
	}

	public Backtrace newBacktrace(Throwable throwable) {
		return new QuietRubyBacktrace(throwable);
	}

}
