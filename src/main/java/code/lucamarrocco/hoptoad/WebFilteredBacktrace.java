package code.lucamarrocco.hoptoad;

import java.util.*;

public class WebFilteredBacktrace extends Backtrace {

	public WebFilteredBacktrace(Throwable throwable) {
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

	public WebFilteredBacktrace(List<String> backtrace) {
		super(backtrace);
	}

}
