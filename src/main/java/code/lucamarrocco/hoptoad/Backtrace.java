package code.lucamarrocco.hoptoad;

import java.util.*;

public class Backtrace implements Iterable<String> {

	private final Iterable<String> backtrace;

	private List<String> ignoreRules = new LinkedList<String>();

	public Backtrace(Iterable<String> backtrace) {
		this.backtrace = backtrace;
	}

	protected void filter() {
		Iterator<String> iterator = iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			if (mustBeIgnored(string)) iterator.remove();
		}
	}

	protected void ignore(String ignoreRule) {
		this.ignoreRules.add(ignoreRule);
	}

	private boolean mustBeIgnored(String string) {
		for (String ignore : ignoreRules) {
			if (string.matches(ignore)) return true;
		}
		return false;
	}

	public Iterator<String> iterator() {
		return backtrace.iterator();
	}

	protected void ignoreCocoon() {
		ignore("^org.apache.cocoon.components.expression.*");
		ignore("^org.apache.cocoon.template.script.*");
		ignore("^org.apache.cocoon.template.instruction.*");
		ignore("^org.apache.cocoon.template.JXTemplateGenerator.*");
		ignore("^org.apache.cocoon.components.pipeline.AbstractProcessingPipeline.*");
		ignore("^org.apache.cocoon.components.treeprocessor.*");
		ignore("^org.apache.cocoon.environment.ForwardRedirector.*");
		ignore("^org.apache.cocoon.components.flow.AbstractInterpreter.*");
		ignore("^org.apache.cocoon.components.flow.javascript.fom.FOM_JavaScriptInterpreter.*");
		ignore("^org.apache.cocoon.components.flow.javascript.fom.FOM_Cocoon.*");
		ignore("^org.apache.commons.jexl.util.introspection.*");
		ignore("^org.apache.commons.jexl.parser.ASTMethod.*");
		ignore("^org.apache.commons.jexl.parser.ASTReference.*");
		ignore("^org.apache.commons.jexl.ExpressionImpl.*");
		ignore("^org.apache.cocoon.template.expression.*");
		ignore("^org.apache.cocoon.Cocoon.*");
		ignore("^org.apache.cocoon.servlet.*");
	}

	protected void ignoreMozilla() {
		ignore("^org.mozilla.javascript.FunctionObject.*");
		ignore("^org.mozilla.javascript.ScriptRuntime.*");
		ignore("^org.mozilla.javascript.continuations.*");
		ignore("^org.mozilla.javascript.ScriptRuntime.*");
		ignore("^org.mozilla.javascript.ScriptableObject.*");
		ignore("^org.mozilla.javascript.FunctionObject.*");
	}

	protected void ignoreMortbayJetty() {
		ignore("^org.mortbay.jetty.handler.ContextHandlerCollection.*");
		ignore("^org.mortbay.jetty.handler.ContextHandler.*");
		ignore("^org.mortbay.jetty.handler.HandlerCollection.*");
		ignore("^org.mortbay.jetty.handler.HandlerWrapper.*");
		ignore("^org.mortbay.jetty.HttpConnection.*");
		ignore("^org.mortbay.jetty.HttpParser.*");
		ignore("^org.mortbay.jetty.security.SecurityHandler.*");
		ignore("^org.mortbay.jetty.Server.*");
		ignore("^org.mortbay.jetty.servlet.ServletHandler.*");
		ignore("^org.mortbay.jetty.servlet.ServletHolder.*");
		ignore("^org.mortbay.jetty.servlet.SessionHandler.*");
		ignore("^org.mortbay.jetty.webapp.WebAppContext.*");
	}

	protected void ignoreSpringSecurity() {
		ignore("^org.springframework.security.context.HttpSessionContextIntegrationFilter.*");
		ignore("^org.springframework.security.intercept.web.FilterSecurityInterceptor.*");
		ignore("^org.springframework.security.providers.anonymous.AnonymousProcessingFilter.*");
		ignore("^org.springframework.security.ui.AbstractProcessingFilter.*");
		ignore("^org.springframework.security.ui.basicauth.BasicProcessingFilter.*");
		ignore("^org.springframework.security.ui.ExceptionTranslationFilter.*");
		ignore("^org.springframework.security.ui.logout.LogoutFilter.*");
		ignore("^org.springframework.security.ui.rememberme.RememberMeProcessingFilter.*");
		ignore("^org.springframework.security.ui.SessionFixationProtectionFilter.*");
		ignore("^org.springframework.security.ui.SpringSecurityFilter.*");
		ignore("^org.springframework.security.util.FilterChainProxy.*");
		ignore("^org.springframework.security.wrapper.SecurityContextHolderAwareRequestFilter.*");
		ignore("^org.springframework.web.filter.DelegatingFilterProxy.*");
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : backtrace) {
			stringBuilder.append(string).append("\n");
		}
		return stringBuilder.toString();
	}

}