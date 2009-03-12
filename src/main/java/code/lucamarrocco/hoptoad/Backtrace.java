package code.lucamarrocco.hoptoad;

import static code.lucamarrocco.hoptoad.IsValidBacktrace.*;
import static org.hamcrest.Matchers.*;

import java.text.*;
import java.util.*;

public class Backtrace implements Iterable<String> {

	private String messageIn(Throwable throwable) {
		String message = throwable.getMessage();
		if (message == null) message = throwable.getClass().getName();
		return message;
	}

	private void toBacktrace(Throwable throwable) {
		if (throwable == null) return;

		backtrace.add(causedBy(throwable));
		for (StackTraceElement element : throwable.getStackTrace())
			backtrace.add(toBacktrace(element));

		toBacktrace(throwable.getCause());
	}

	private String causedBy(Throwable throwable) {
		return MessageFormat.format("Caused by {0}", messageIn(throwable));
	}

	private String toBacktrace(StackTraceElement element) {
		return toBacktrace(element.getClassName(), element.getFileName(), element.getLineNumber(), element.getMethodName());
	}

	protected String toBacktrace(String className, String fileName, int lineNumber, String methodName) {
		return MessageFormat.format("at {0}.{1}({2}:{3})", className, methodName, fileName, lineNumber);
	}

	private final List<String> backtrace = new LinkedList<String>();

	private final List<String> ignoreRules = new LinkedList<String>();

	private final List<String> filteredBacktrace = new LinkedList<String>();

	public Backtrace(List<String> backtrace) {
		this.backtrace.addAll(backtrace);
		ignore();
		filter();
	}

	public Backtrace(List<String> backtrace, String errorMessage) {
		this.backtrace.addAll(backtrace);
		ignore(".*" + errorMessage + ".*");
		ignore();
		filter();
	}

	public Backtrace(Throwable throwable) {
		toBacktrace(throwable);
		ignore(".*" + messageIn(throwable) + ".*");
		ignore();
		filter();
	}

	protected Backtrace() {
	}

	protected void filter() {
		filter(backtrace);
	}

	private final List<String> filter(final List<String> backtrace) {
		ListIterator<String> iterator = backtrace.listIterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			if (mustBeIgnored(string)) continue;
			if (not(validBacktrace()).matches(string)) string = removeDobuleDot(string);
			filteredBacktrace.add(string);
		}

		return filteredBacktrace;
	}

	protected void ignore() {
		ignoreEmptyCause();
	}

	private void ignoreEmptyCause() {
		ignore("^Caused by $");
	}

	protected void ignore(String ignoreRule) {
		ignoreRules.add(ignoreRule);
	}

	protected void ignoreCocoon() {
		ignore(".*org.apache.cocoon.components.expression.*");
		ignore(".*org.apache.cocoon.template.script.*");
		ignore(".*org.apache.cocoon.template.instruction.*");
		ignore(".*org.apache.cocoon.template.JXTemplateGenerator.*");
		ignore(".*org.apache.cocoon.components.pipeline.AbstractProcessingPipeline.*");
		ignore(".*org.apache.cocoon.components.treeprocessor.*");
		ignore(".*org.apache.cocoon.environment.ForwardRedirector.*");
		ignore(".*org.apache.cocoon.components.flow.AbstractInterpreter.*");
		ignore(".*org.apache.cocoon.components.flow.javascript.fom.FOM_JavaScriptInterpreter.*");
		ignore(".*org.apache.cocoon.components.flow.javascript.fom.FOM_Cocoon.*");
		ignore(".*org.apache.commons.jexl.util.introspection.*");
		ignore(".*org.apache.commons.jexl.parser.ASTMethod.*");
		ignore(".*org.apache.commons.jexl.parser.ASTReference.*");
		ignore(".*org.apache.commons.jexl.ExpressionImpl.*");
		ignore(".*org.apache.cocoon.template.expression.*");
		ignore(".*org.apache.cocoon.Cocoon.*");
		ignore(".*org.apache.cocoon.servlet.*");
	}

	protected void ignoreEclipse() {
		ignore(".*org.eclipse.jdt.internal.junit4.runner.*");
		ignore(".*org.eclipse.jdt.internal.junit.runner.*");
	}

	protected void ignoreJunit() {
		ignore(".*.*org.junit.internal.runners.*");
	}

	protected void ignoreMortbayJetty() {
		ignore(".*org.mortbay.jetty.handler.ContextHandlerCollection.*");
		ignore(".*org.mortbay.jetty.handler.ContextHandler.*");
		ignore(".*org.mortbay.jetty.handler.HandlerCollection.*");
		ignore(".*org.mortbay.jetty.handler.HandlerWrapper.*");
		ignore(".*org.mortbay.jetty.HttpConnection.*");
		ignore(".*org.mortbay.jetty.HttpParser.*");
		ignore(".*org.mortbay.jetty.security.SecurityHandler.*");
		ignore(".*org.mortbay.jetty.Server.*");
		ignore(".*org.mortbay.jetty.servlet.ServletHandler.*");
		ignore(".*org.mortbay.jetty.servlet.ServletHolder.*");
		ignore(".*org.mortbay.jetty.servlet.SessionHandler.*");
		ignore(".*org.mortbay.jetty.webapp.WebAppContext.*");
		ignore(".*org.mortbay.io.nio.*");
		ignore(".*org.mortbay.thread.*");
	}

	protected void ignoreMozilla() {
		ignore(".*org.mozilla.javascript.FunctionObject.*");
		ignore(".*org.mozilla.javascript.ScriptRuntime.*");
		ignore(".*org.mozilla.javascript.continuations.*");
		ignore(".*org.mozilla.javascript.ScriptRuntime.*");
		ignore(".*org.mozilla.javascript.ScriptableObject.*");
		ignore(".*org.mozilla.javascript.FunctionObject.*");
	}

	protected void ignoreNoise() {
		ignore(".*inv1.invoke.*");
		ignore(".*javax.servlet.http.HttpServlet.*");
		ignore(".*sun.reflect.*");
		ignore(".*java.lang.reflect.Method.*");
	}

	protected void ignoreSpringSecurity() {
		ignore(".*org.springframework.security.context.HttpSessionContextIntegrationFilter.*");
		ignore(".*org.springframework.security.intercept.web.FilterSecurityInterceptor.*");
		ignore(".*org.springframework.security.providers.anonymous.AnonymousProcessingFilter.*");
		ignore(".*org.springframework.security.ui.AbstractProcessingFilter.*");
		ignore(".*org.springframework.security.ui.basicauth.BasicProcessingFilter.*");
		ignore(".*org.springframework.security.ui.ExceptionTranslationFilter.*");
		ignore(".*org.springframework.security.ui.logout.LogoutFilter.*");
		ignore(".*org.springframework.security.ui.rememberme.RememberMeProcessingFilter.*");
		ignore(".*org.springframework.security.ui.SessionFixationProtectionFilter.*");
		ignore(".*org.springframework.security.ui.SpringSecurityFilter.*");
		ignore(".*org.springframework.security.util.FilterChainProxy.*");
		ignore(".*org.springframework.security.wrapper.SecurityContextHolderAwareRequestFilter.*");
		ignore(".*org.springframework.web.filter.DelegatingFilterProxy.*");
	}

	public Iterator<String> iterator() {
		if (needToBeFiltered()) {
			filter(backtrace);
		}
		return filteredBacktrace.iterator();
	}

	private boolean mustBeIgnored(String string) {
		for (String ignore : ignoreRules) {
			if (string.matches(ignore)) return true;
		}
		return false;
	}

	private boolean needToBeFiltered() {
		return filteredBacktrace.isEmpty();
	}

	private String removeDobuleDot(String string) {
		return string.replaceAll(":", "");
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : filteredBacktrace) {
			stringBuilder.append(string).append("\n");
		}
		return stringBuilder.toString();
	}

	public Backtrace newBacktrace(Throwable throwable) {
		return new Backtrace(throwable);
	}
}
