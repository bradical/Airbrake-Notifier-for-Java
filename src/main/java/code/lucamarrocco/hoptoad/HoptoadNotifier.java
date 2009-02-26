package code.lucamarrocco.hoptoad;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

public class HoptoadNotifier {

	public int notify(HoptoadNotice notice) {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://hoptoadapp.com/notices/");
		postMethod.setRequestHeader("Content-type", "application/x-yaml");
		postMethod.setRequestHeader("Accept", "text/xml, application/xml");
		postMethod.addParameter("api_key", notice.apiKey());
		postMethod.addParameter("error_message", notice.errorMessage());

		int statusCode;
		
		try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			postMethod.releaseConnection();
		}
		
		return statusCode;
	}

}
