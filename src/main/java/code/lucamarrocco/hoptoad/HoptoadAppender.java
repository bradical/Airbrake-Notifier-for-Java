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
		HoptoadNotice notice = new HoptoadNoticeBuilder(api_key, loggingEvent.getThrowableInformation().getThrowable()).newNotice();

		hoptoadNotifier.notify(notice);
	}

	@Override
	public void close() {}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
