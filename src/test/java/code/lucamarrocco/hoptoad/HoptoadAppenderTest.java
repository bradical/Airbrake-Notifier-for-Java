// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

import static code.lucamarrocco.hoptoad.Exceptions.*;
import static code.lucamarrocco.hoptoad.HoptoadNotifierTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class HoptoadAppenderTest {

	@Test
	public void testNewAppenderWithApiKey() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY);

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice, is(notNullValue()));
	}

	@Test
	public void testNewAppenderWithApiKeyAndBacktrace() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY, new Backtrace());

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice, is(notNullValue()));
	}

	@Test
	public void testNotyfyThrowable() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY);

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:15:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:13:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseBacktrace() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY, new Backtrace());

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.newException(Exceptions.java:15)"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.newException(Exceptions.java:13)"));

		assertThat(notice.backtrace(), hasItem("at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)"));
		assertThat(notice.backtrace(), hasItem("at org.junit.internal.runners.TestMethod.invoke(TestMethod.java:59)"));
	}

	@Test
	public void testNotyfyThrowable$UseQuiteBacktrace() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY, new QuietRubyBacktrace());

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:15:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:13:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseRubyBacktrace() {
		final HoptoadAppender appender = new HoptoadAppender(API_KEY, new RubyBacktrace());

		final HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:15:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:13:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseSwitchBacktrace() {
		final SwitchBacktrace switchBacktrace = new SwitchBacktrace();
		final HoptoadAppender appender = new HoptoadAppender(API_KEY, switchBacktrace);

		switchBacktrace.quiet();
		final HoptoadNotice quietNotice = appender.newNoticeFor(newException(ERROR_MESSAGE));
		assertThat(quietNotice.backtrace(), not(hasItem("at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)")));

		switchBacktrace.verbose();
		final HoptoadNotice verboseNotice = appender.newNoticeFor(newException(ERROR_MESSAGE));
		assertThat(verboseNotice.backtrace(), hasItem("at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)"));
	}
}
