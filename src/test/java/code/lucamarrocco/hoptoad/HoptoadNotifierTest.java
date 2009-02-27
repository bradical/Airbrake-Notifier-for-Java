package code.lucamarrocco.hoptoad;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.junit.*;

public class HoptoadNotifierTest {
	protected static final String API_KEY = "7274e956946585ce31dda87801a0de56";
	protected static final String ERROR_MESSAGE = "undefined method `password' for nil:NilClass";
	protected static final String[] BACKTRACE = new String[0];
	protected static final Map REQUEST = new HashMap();
	protected static final Map SESSION = new HashMap();
	protected static final Map ENVIRONMENT = new HashMap();

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithBacktrace() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(BACKTRACE);
			}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.backtrace(), is(BACKTRACE));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithEnvironment() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				environment(ENVIRONMENT);
			}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.environment(), is(ENVIRONMENT));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithErrorMessage() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithRequest() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				request(REQUEST);
			}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.request(), is(REQUEST));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithSession() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				session(SESSION);
			}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.session(), is(SESSION));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeFromException() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION).newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.backtrace(), is(notNullValue()));
	}

	@Test
	public void testHoptoadBuilderBacktrace() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		String[] backtrace = HoptoadNoticeBuilder.toBacktrace(EXCEPTION.getStackTrace());

		assertThat(backtrace, is(notNullValue()));
		assertThat(backtrace[0], containsString("code.lucamarrocco.hoptoad.HoptoadNotifierTest.newException(HoptoadNotifierTest.java:"));
	}

	private Exception newException(String errorMessage) {
		try {
			throw new RuntimeException(errorMessage);
		} catch (Exception e) {
			return e;
		}
	}

	@Test
	public void testSendNoticeToHoptoad() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE).newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	private String expectedNoticeAsString() {
		try {
			InputStream inputStream = getClass().getResource("Notice.txt").openStream();
			StringBuilder out = new StringBuilder();
			byte[] b = new byte[4096];
			for (int n; (n = inputStream.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			return out.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testSendExceptionToHoptoad() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION).newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	private Log logger = LogFactory.getLog(getClass());
	
	@Test
	public void testLogError() {
		logger.error("error", newException(ERROR_MESSAGE));
	}
}