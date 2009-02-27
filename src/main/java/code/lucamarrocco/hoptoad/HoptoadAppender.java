package code.lucamarrocco.hoptoad;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class HoptoadAppender extends AppenderSkeleton {
	private HoptoadNotifier hoptoadNotifier = new HoptoadNotifier();

	private String api_key;
	
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	
	@Override
	protected void append(final LoggingEvent loggingEvent) {
		if(thereIsThrowableIs(loggingEvent))
		hoptoadNotifier.notify(newNoticeUsing(loggingEvent));
	}

	private boolean thereIsThrowableIs(LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation() != null;
	}

	private HoptoadNotice newNoticeUsing(final LoggingEvent loggingEvent) {
		return new HoptoadNoticeBuilder(api_key, throwable(loggingEvent)).newNotice();
	}

	private Throwable throwable(final LoggingEvent loggingEvent) {
		return loggingEvent.getThrowableInformation().getThrowable();
	}

	@Override
	public void close() {}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
