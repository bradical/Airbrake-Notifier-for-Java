package code.lucamarrocco.hoptoad;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class HoptoadAppender extends AppenderSkeleton {
	private HoptoadNotifier hoptoadNotifier = new HoptoadNotifier();

	private String api_key;

	@Override
	protected void append(final LoggingEvent loggingEvent) {
		notify(loggingEvent);
	}

	@Override
	public void close() {}

	private String messageIn(LoggingEvent loggingEvent) {
		return loggingEvent.getMessage().toString();
	}

	private HoptoadNotice newNoticeUsing(Throwable throwable) {
		return new HoptoadNoticeBuilder(api_key, throwable).newNotice();
	}

	private int notify(LoggingEvent loggingEvent) {
		if (thereIsThrowableIn(loggingEvent)) {
			return notifyThrowableIn(loggingEvent);
		}

		return notifyMessageIn(loggingEvent);
	}

	private int notifyMessageIn(LoggingEvent loggingEvent) {
		return hoptoadNotifier.notify(newNoticeUsing(pleaseIgnoreThisException$ItIsAWorkaroundOverHoptoadToHaveABacktraceInAnyCase(messageIn(loggingEvent))));
	}

	private int notifyThrowableIn(LoggingEvent loggingEvent) {
		return hoptoadNotifier.notify(newNoticeUsing(throwable(loggingEvent)));
	}

	private Exception pleaseIgnoreThisException$ItIsAWorkaroundOverHoptoadToHaveABacktraceInAnyCase(String errorMessage) {
		try {
			throw new RuntimeException(errorMessage);
		} catch (Exception e) {
			return e;
		}
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	private boolean thereIsThrowableIn(LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation() != null;
	}

	private Throwable throwable(final LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation().getThrowable();
	}

}
