package code.lucamarrocco.hoptoad;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class HoptoadNoticeBuilderTest {
	
	@Test
	public void testBuildNoticeErrorClass() {
		HoptoadNoticeBuilder builder = new HoptoadNoticeBuilder("apiKey", new RuntimeException("errorMessage"));
		HoptoadNotice notice = builder.newNotice();
		assertThat(notice.errorClass(), is(equalTo("java.lang.RuntimeException")));
	}
	
	@Test
	public void testErrorClass() {
		HoptoadNoticeBuilder builder = new HoptoadNoticeBuilder("apiKey", new RuntimeException("errorMessage"));
		assertTrue(builder.errorClassIs("java.lang.RuntimeException"));
	}
}
