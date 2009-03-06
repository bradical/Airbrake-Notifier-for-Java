package code.lucamarrocco.hoptoad;

import static code.lucamarrocco.hoptoad.Slurp.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class BacktraceFilterTest {

	public static final List<String> backtrace() {
		return strings(slurp(read("backtrace.txt")));
	}


	private Iterable<String> filteredBacktrace() {
		return strings(slurp(read("filteredBacktrace.txt")));
	}
	
	@Test
	public void testFilteredCocoonBacktrace() {
		Iterable<String> backtrace = new Backtrace(backtrace()) {
			{
				ignoreCocoon();
				filter();
			}
		};

		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.expression.jexl.JexlExpression.evaluate(JexlExpression.java:49)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.template.script.event.StartElement.execute(StartElement.java:115)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.template.instruction.Call.execute(Call.java:143)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.template.JXTemplateGenerator.performGeneration(JXTemplateGenerator.java:117)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.pipeline.AbstractProcessingPipeline.processXMLPipeline(AbstractProcessingPipeline.java:578)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.treeprocessor.sitemap.SerializeNode.invoke(SerializeNode.java:120)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.environment.ForwardRedirector.redirect(ForwardRedirector.java:59)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.flow.AbstractInterpreter.forwardTo(AbstractInterpreter.java:209)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.flow.javascript.fom.FOM_JavaScriptInterpreter.forwardTo(FOM_JavaScriptInterpreter.java:905)")));
		assertThat(backtrace, not(hasItem("org.apache.cocoon.components.flow.javascript.fom.FOM_Cocoon.forwardTo(FOM_Cocoon.java:698)")));
		assertThat(backtrace, not(hasItem("org.apache.commons.jexl.util.introspection.UberspectImpl$VelMethodImpl.invoke(UberspectImpl.java:268)")));
		assertThat(backtrace, not(hasItem("org.apache.commons.jexl.parser.ASTMethod.execute(ASTMethod.java:61)")));
		assertThat(backtrace, not(hasItem("org.apache.commons.jexl.parser.ASTReference.execute(ASTReference.java:68)")));
		assertThat(backtrace, not(hasItem("org.apache.commons.jexl.parser.ASTReference.value(ASTReference.java:50)")));
		assertThat(backtrace, not(hasItem("org.apache.commons.jexl.ExpressionImpl.evaluate(ExpressionImpl.java:86)")));
	}

	@Test
	public void testFilteredMozillaBacktrace() {
		Iterable<String> backtrace = new Backtrace(backtrace()) {
			{
				ignoreMozilla();
				filter();
			}
		};

		assertThat(backtrace, not(hasItem("org.mozilla.javascript.FunctionObject.doInvoke(FunctionObject.java:523)")));
		assertThat(backtrace, not(hasItem("org.mozilla.javascript.ScriptRuntime.call(ScriptRuntime.java:1,244)")));
		assertThat(backtrace, not(hasItem("org.mozilla.javascript.continuations.ContinuationInterpreter.interpret(ContinuationInterpreter.java:1,134)")));
		assertThat(backtrace, not(hasItem("org.mozilla.javascript.ScriptRuntime.call(ScriptRuntime.java:1,244)")));
		assertThat(backtrace, not(hasItem("org.mozilla.javascript.ScriptableObject.callMethod(ScriptableObject.java:1,591)")));
		assertThat(backtrace, not(hasItem("org.mozilla.javascript.FunctionObject.doInvoke(FunctionObject.java:523)")));
	}

	@Test
	public void testFilteredMortbayJettyBacktrace() {
		Iterable<String> backtrace = new Backtrace(backtrace()) {
			{
				ignoreMortbayJetty();
				filter();
			}
		};

		assertThat(backtrace, not(hasItem("org.mortbay.jetty.handler.ContextHandlerCollection.handle(ContextHandlerCollection.java:206)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:729)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.handler.HandlerCollection.handle(HandlerCollection.java:114)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.handler.HandlerWrapper.handle(HandlerWrapper.java:152)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.HttpConnection.handle(HttpConnection.java:380)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.HttpConnection.handleRequest(HttpConnection.java:505)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.HttpConnection$RequestHandler.content(HttpConnection.java:843)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.HttpParser.parseAvailable(HttpParser.java:211)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.HttpParser.parseNext(HttpParser.java:647)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.Server.handle(Server.java:324)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1,088)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:360)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:487)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:181)")));
		assertThat(backtrace, not(hasItem("org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:405)")));
	}

	@Test
	public void testFilteredSpringBacktrace() {
		Iterable<String> backtrace = new Backtrace(backtrace()) {
			{
				ignoreSpringSecurity();
				filter();
			}
		};

		assertThat(backtrace, not(hasItem("org.springframework.security.context.HttpSessionContextIntegrationFilter.doFilterHttp(HttpSessionContextIntegrationFilter.java:235)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.intercept.web.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:83)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.providers.anonymous.AnonymousProcessingFilter.doFilterHttp(AnonymousProcessingFilter.java:105)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.AbstractProcessingFilter.doFilterHttp(AbstractProcessingFilter.java:271)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.basicauth.BasicProcessingFilter.doFilterHttp(BasicProcessingFilter.java:173)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.ExceptionTranslationFilter.doFilterHttp(ExceptionTranslationFilter.java:101)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.logout.LogoutFilter.doFilterHttp(LogoutFilter.java:89)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.rememberme.RememberMeProcessingFilter.doFilterHttp(RememberMeProcessingFilter.java:116)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.SessionFixationProtectionFilter.doFilterHttp(SessionFixationProtectionFilter.java:67)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.ui.SpringSecurityFilter.doFilter(SpringSecurityFilter.java:53)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.util.FilterChainProxy.doFilter(FilterChainProxy.java:174)")));
		assertThat(backtrace, not(hasItem("org.springframework.security.wrapper.SecurityContextHolderAwareRequestFilter.doFilterHttp(SecurityContextHolderAwareRequestFilter.java:91)")));
		assertThat(backtrace, not(hasItem("org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:167)")));
	}


	@Test
	public void testFilteredIgnomreCommonsBacktrace() {
		Iterable<String> backtrace = new CocoonFilteredBacktrace(backtrace());
		Iterable<String> filteredBacktrace = new CocoonFilteredBacktrace(filteredBacktrace());
		
		assertEquals(backtrace.toString(), filteredBacktrace.toString());
	}
}