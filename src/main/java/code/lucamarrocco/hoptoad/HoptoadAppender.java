package code.lucamarrocco.hoptoad;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class HoptoadAppender extends AppenderSkeleton {
	private HoptoadNotifier hoptoadNotifier = new HoptoadNotifier();

	private String apiKey;

	private String env;
	
	private boolean enabled;

	private Backtrace backtrace = new QuietRubyBacktrace();
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public HoptoadAppender() {
		setThreshold(Level.ERROR);
	}

	public HoptoadAppender(String apiKey) {
		setApi_key(apiKey);
	}

	public HoptoadAppender(String apiKey, Backtrace backtrace) {
		setApi_key(apiKey);
		setBacktrace(backtrace);
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setApi_key(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	protected void append(final LoggingEvent loggingEvent) {
		if(!enabled) return;
		
		if (thereIsThrowableIn(loggingEvent)) {
			notifyThrowableIn(loggingEvent);
		}
	}

	@Override
	public void close() {}

	public HoptoadNotice newNoticeFor(Throwable throwable) {
		return new HoptoadNoticeBuilderUsingFilterdSystemProperties(apiKey, backtrace, throwable, env).newNotice();
	}

	private int notifyThrowableIn(LoggingEvent loggingEvent) {
		return hoptoadNotifier.notify(newNoticeFor(throwable(loggingEvent)));
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	private boolean thereIsThrowableIn(LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation() != null;
	}

	private Throwable throwable(final LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation().getThrowable();
	}

	public void setBacktrace(Backtrace backtrace) {
		this.backtrace = backtrace;
	}
}