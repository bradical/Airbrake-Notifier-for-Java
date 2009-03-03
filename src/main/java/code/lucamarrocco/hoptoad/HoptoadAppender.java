package code.lucamarrocco.hoptoad;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class HoptoadAppender extends AppenderSkeleton {
	private HoptoadNotifier hoptoadNotifier = new HoptoadNotifier();

	private String api_key;

	private String env;

	public HoptoadAppender() {
		setThreshold(Level.ERROR);
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	@Override
	protected void append(final LoggingEvent loggingEvent) {
		if (thereIsThrowableIn(loggingEvent)) {
			notifyThrowableIn(loggingEvent);
		}
	}

	@Override
	public void close() {}

	private HoptoadNotice newNoticeUsing(Throwable throwable) {
		return new HoptoadNoticeBuilderUsingFilterdSystemProperties(api_key, throwable, env).newNotice();
	}

	private int notifyThrowableIn(LoggingEvent loggingEvent) {
		return hoptoadNotifier.notify(newNoticeUsing(throwable(loggingEvent)));
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
}