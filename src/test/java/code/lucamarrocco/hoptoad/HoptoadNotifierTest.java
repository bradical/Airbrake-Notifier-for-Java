package code.lucamarrocco.hoptoad;

import static code.lucamarrocco.hoptoad.Exceptions.*;
import static code.lucamarrocco.hoptoad.Slurp.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.apache.commons.logging.*;
import org.hamcrest.*;
import org.junit.*;

public class HoptoadNotifierTest {
	protected static final String API_KEY = "ee477a5da66f0079a49a7b6a07f0fe5f";
	protected static final Backtrace BACKTRACE = new Backtrace(asList("backtrace is empty"));;
	protected static final Map REQUEST = new HashMap();
	protected static final Map SESSION = new HashMap();
	protected static final Map ENVIRONMENT = new HashMap();

	private Log logger = LogFactory.getLog(getClass());

	private Map EC2 = new HashMap();

	@Before
	public void setUp() {
		ENVIRONMENT.put("A_KEY", "test");
		EC2.put("AWS_SECRET", "AWS_SECRET");
		EC2.put("EC2_PRIVATE_KEY", "EC2_PRIVATE_KEY");
		EC2.put("AWS_ACCESS", "AWS_ACCESS");
		EC2.put("EC2_CERT", "EC2_CERT");
	}

	@Test
	public void testLogThresholdLesserThatErrorWithExceptionDoNotNotifyToHoptoad() {
		logger.info("info", newException(ERROR_MESSAGE));
		logger.warn("warn", newException(ERROR_MESSAGE));
	}

	@Test
	public void testLogThresholdLesserThatErrorWithoutExceptionDoNotNotifyToHoptoad() {
		logger.info("info");
		logger.warn("warn");
	}

	@Test
	public void testLogErrorWithException() {
		logger.error("error", newException(ERROR_MESSAGE));
	}

	@Test
	public void testLogErrorWithoutException() {
		logger.error("error");
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
	public void testNewHoptoadUsingBuilderNoticeWithEc2FilteredEnvironmentWithSystemProperties() {

		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {

			{
				environment(EC2);
				ec2EnvironmentFilters();
			}
		}.newNotice();

		Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("AWS_SECRET")));
		assertThat(environmentKeys, not(hasItem("EC2_PRIVATE_KEY")));
		assertThat(environmentKeys, not(hasItem("AWS_ACCESS")));
		assertThat(environmentKeys, not(hasItem("EC2_CERT")));
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
		assertThat(notice.environment().keySet(), hasItem("A_KEY"));
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
	public void testNewHoptoadUsingBuilderNoticeWithFilterdEnvironment() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				environmentFilter("A_KEY");
			}
		}.newNotice();

		Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("A_KEY")));
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
	public void testNewHoptoadUsingBuilderNoticeWithStandardFilteredEnvironmentWithSystemProperties() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				environment(System.getProperties());
				standardEnvironmentFilters();
			}
		}.newNotice();

		Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("java.awt.graphicsenv")));
		assertThat(environmentKeys, not(hasItem("java.vendor.url")));
		assertThat(environmentKeys, not(hasItem("java.class.path")));
		assertThat(environmentKeys, not(hasItem("java.vm.specification")));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeFromExceptionInEnv() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION, "test").newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeFromExceptionInEnvAndSystemProperties() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION, "test") {
			{
				filteredSystemProperties();
			}

		}.newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeInEnv() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE, "test").newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionToHoptoad() {
		Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION).newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeToHoptoad() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE).newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeWithLargeBacktrace() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new Backtrace(strings(slurp(read("backtrace.txt")))));
			}
		}.newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeWithFilteredBacktrace() {
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new QuietBacktrace(strings(slurp(read("backtrace.txt")))));
			}
		}.newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionNoticeWithFilteredBacktrace() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new QuietBacktrace(EXCEPTION));
			}
		}.newNotice();
		HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testHowBacktraceHoptoadNotInternalServerError() {
		assertThat(notifing(ERROR_MESSAGE), not(internalServerError()));
		assertThat(notifing("java.lang.RuntimeException: an expression is not valid"), not(internalServerError()));
		assertThat(notifing("Caused by: java.lang.NullPointerException"), not(internalServerError()));
		assertThat(notifing("at code.lucamarrocco.notifier.Exceptions.newException(Exceptions.java:11)"), not(internalServerError()));
		assertThat(notifing("... 23 more"), not(internalServerError()));
	}

	private int notifing(final String string) {
		return new HoptoadNotifier().notify(new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new Backtrace(asList(string)));
			}
		}.newNotice());
	}

	private <T> Matcher<T> internalServerError() {
		return new BaseMatcher<T>() {
			public void describeTo(Description description) {
				description.appendText("internal server error");
			}

			public boolean matches(Object item) {
				return item.equals(500);
			}
		};
	}
}