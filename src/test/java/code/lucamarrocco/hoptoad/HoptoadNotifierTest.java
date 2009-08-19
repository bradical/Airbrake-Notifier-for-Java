// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

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
	public static final String API_KEY = "0888f66cb50a4a35f6fa90edad7f5ad2";

	protected static final Backtrace BACKTRACE = new Backtrace(asList("backtrace is empty"));;
	protected static final Map<String, Object> REQUEST = new HashMap<String, Object>();
	protected static final Map<String, Object> SESSION = new HashMap<String, Object>();
	protected static final Map<String, Object> ENVIRONMENT = new HashMap<String, Object>();

	private final Log logger = LogFactory.getLog(getClass());

	private final Map<String, Object> EC2 = new HashMap<String, Object>();

	private <T> Matcher<T> internalServerError() {
		return new BaseMatcher<T>() {
			public void describeTo(final Description description) {
				description.appendText("internal server error");
			}

			public boolean matches(final Object item) {
				return item.equals(500);
			}
		};
	}

	private int notifing(final String string) {
		return new HoptoadNotifier().notify(new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new Backtrace(asList(string)));
			}
		}.newNotice());
	}

	@Before
	public void setUp() {
		ENVIRONMENT.put("A_KEY", "test");
		EC2.put("AWS_SECRET", "AWS_SECRET");
		EC2.put("EC2_PRIVATE_KEY", "EC2_PRIVATE_KEY");
		EC2.put("AWS_ACCESS", "AWS_ACCESS");
		EC2.put("EC2_CERT", "EC2_CERT");
	}

	@Test
	public void testHowBacktraceHoptoadNotInternalServerError() {
		assertThat(notifing(ERROR_MESSAGE), not(internalServerError()));
		assertThat(notifing("java.lang.RuntimeException: an expression is not valid"), not(internalServerError()));
		assertThat(notifing("Caused by: java.lang.NullPointerException"), not(internalServerError()));
		assertThat(notifing("at code.lucamarrocco.notifier.Exceptions.newException(Exceptions.java:11)"), not(internalServerError()));
		assertThat(notifing("... 23 more"), not(internalServerError()));
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
	public void testNewHoptoadUsingBuilderNoticeFromException() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION).newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
		assertThat(notice.backtrace(), is(notNullValue()));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithBacktrace() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
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

		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {

			{
				environment(EC2);
				ec2EnvironmentFilters();
			}
		}.newNotice();

		final Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("AWS_SECRET")));
		assertThat(environmentKeys, not(hasItem("EC2_PRIVATE_KEY")));
		assertThat(environmentKeys, not(hasItem("AWS_ACCESS")));
		assertThat(environmentKeys, not(hasItem("EC2_CERT")));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithEnvironment() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
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
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{}
		}.newNotice();

		assertThat(notice, is(notNullValue()));

		assertThat(notice.apiKey(), is(API_KEY));
		assertThat(notice.errorMessage(), is(ERROR_MESSAGE));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithFilterdEnvironment() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				environmentFilter("A_KEY");
			}
		}.newNotice();

		final Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("A_KEY")));
	}

	@Test
	public void testNewHoptoadUsingBuilderNoticeWithRequest() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
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
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
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
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				environment(System.getProperties());
				standardEnvironmentFilters();
			}
		}.newNotice();

		final Set<String> environmentKeys = notice.environment().keySet();

		assertThat(environmentKeys, not(hasItem("java.awt.graphicsenv")));
		assertThat(environmentKeys, not(hasItem("java.vendor.url")));
		assertThat(environmentKeys, not(hasItem("java.class.path")));
		assertThat(environmentKeys, not(hasItem("java.vm.specification")));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeFromExceptionInEnv() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION, "test").newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeFromExceptionInEnvAndSystemProperties() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION, "test") {
			{
				filteredSystemProperties();
			}

		}.newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testNotifyToHoptoadUsingBuilderNoticeInEnv() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE, "test").newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionNoticeWithFilteredBacktrace() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, new QuietRubyBacktrace(), EXCEPTION, "test").newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionToHoptoad() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, EXCEPTION).newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionToHoptoadUsingRubyBacktrace() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, new RubyBacktrace(), EXCEPTION, "test").newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendExceptionToHoptoadUsingRubyBacktraceAndFilteredSystemProperties() {
		final Exception EXCEPTION = newException(ERROR_MESSAGE);
		final HoptoadNotice notice = new HoptoadNoticeBuilderUsingFilterdSystemProperties(API_KEY, new RubyBacktrace(), EXCEPTION, "test").newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeToHoptoad() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE).newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeWithFilteredBacktrace() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new QuietRubyBacktrace(strings(slurp(read("backtrace.txt")))));
			}
		}.newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}

	@Test
	public void testSendNoticeWithLargeBacktrace() {
		final HoptoadNotice notice = new HoptoadNoticeBuilder(API_KEY, ERROR_MESSAGE) {
			{
				backtrace(new Backtrace(strings(slurp(read("backtrace.txt")))));
			}
		}.newNotice();
		final HoptoadNotifier notifier = new HoptoadNotifier();

		assertThat(notifier.notify(notice), is(201));
	}
}