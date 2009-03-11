package code.lucamarrocco.hoptoad;

import java.util.*;


public class QuietBacktrace extends RubyBacktrace {

	public QuietBacktrace(Throwable throwable) {
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

	public QuietBacktrace() {
		super();
	}

	protected QuietBacktrace(List<String> backtrace) {
		super(backtrace);
	}

	public Backtrace newBacktrace(Throwable throwable) {
		return new QuietBacktrace(throwable);
	}

}
