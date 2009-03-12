package code.lucamarrocco.hoptoad;

public class SwitchBacktrace extends Backtrace {
	
	private static final QuietRubyBacktrace QuietRubyBacktrace = new QuietRubyBacktrace();

	private static final Backtrace Backtrace = new Backtrace();

	private Backtrace backtrace = Backtrace;

	public void quiet() {
		backtrace = QuietRubyBacktrace;
	}

	public void verbose() {
		backtrace = Backtrace;
	}
	
	@Override
	public code.lucamarrocco.hoptoad.Backtrace newBacktrace(Throwable throwable) {
		return backtrace.newBacktrace(throwable);
	}
	
}
