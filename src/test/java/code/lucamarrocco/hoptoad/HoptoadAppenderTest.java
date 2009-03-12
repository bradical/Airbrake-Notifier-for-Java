package code.lucamarrocco.hoptoad;

import static code.lucamarrocco.hoptoad.Exceptions.*;
import static code.lucamarrocco.hoptoad.HoptoadNotifierTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class HoptoadAppenderTest {

	@Test
	public void testNewAppenderWithApiKey() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY);

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice, is(notNullValue()));
	}

	@Test
	public void testNewAppenderWithApiKeyAndBacktrace() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY, new Backtrace());

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice, is(notNullValue()));
	}

	@Test
	public void testNotyfyThrowable() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY);

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:11:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:9:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseQuiteBacktrace() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY, new QuietRubyBacktrace());

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:11:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:9:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseRubyBacktrace() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY, new RubyBacktrace());

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:11:in `newException'"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("at code.lucamarrocco.hoptoad.Exceptions.java:9:in `newException'"));
	}

	@Test
	public void testNotyfyThrowable$UseBacktrace() {
		HoptoadAppender appender = new HoptoadAppender(API_KEY, new Backtrace());

		HoptoadNotice notice = appender.newNoticeFor(newException(ERROR_MESSAGE));

		assertThat(notice.backtrace(), hasItem("\tat code.lucamarrocco.hoptoad.Exceptions.newException(Exceptions.java:11)"));
		assertThat(notice.backtrace(), hasItem("Caused by java.lang.NullPointerException"));
		assertThat(notice.backtrace(), hasItem("\tat code.lucamarrocco.hoptoad.Exceptions.newException(Exceptions.java:9)"));

		assertThat(notice.backtrace(), hasItem("\tat sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)"));
		assertThat(notice.backtrace(), hasItem("\tat java.lang.reflect.Method.invoke(Method.java:597)"));
		assertThat(notice.backtrace(), hasItem("\tat org.junit.internal.runners.TestMethod.invoke(TestMethod.java:59)"));
		assertThat(notice.backtrace(), hasItem("\tat org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:45)"));
	}

	@Test
	public void testNotyfyThrowable$UseSwitchBacktrace() {		
		SwitchBacktrace switchBacktrace = new SwitchBacktrace();
		HoptoadAppender appender = new HoptoadAppender(API_KEY, switchBacktrace);

		switchBacktrace.quiet();
		HoptoadNotice quietNotice = appender.newNoticeFor(newException(ERROR_MESSAGE));
		assertThat(quietNotice.backtrace(), not(hasItem("\tat sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)")));

		switchBacktrace.verbose();
		HoptoadNotice verboseNotice = appender.newNoticeFor(newException(ERROR_MESSAGE));
		assertThat(verboseNotice.backtrace(), hasItem("\tat sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java-2)"));
	}
}
