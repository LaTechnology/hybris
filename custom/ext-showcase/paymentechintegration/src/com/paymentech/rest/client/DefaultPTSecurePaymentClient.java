package com.paymentech.rest.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.api.services.PTAPIPaymentService;
import com.paymentech.jaxb.response.Response;

import de.hybris.platform.util.Config;

/**
 * Implementation of a REST client for communication with the Data Hub
 */
public class DefaultPTSecurePaymentClient implements PTSecurePaymentClient {
	private static final Logger LOG = Logger
			.getLogger(DefaultPTSecurePaymentClient.class);
	private String postXMLURL;
	private String timeOut;

	PTAPIPaymentService ptAPIPaymentService;
	private String getMaxUserRetries()
	{
		return Config.getString("paymentech.config.paymentpages.maxuserretries","4");
	}
	@Override
	public Response authPayment(final String requestXML) {
		CloseableHttpClient client = null;
		final HttpPost post = new HttpPost(postXMLURL);
		try {
			LOG.error("PT Auth Payment Execution");
			final RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(Integer.parseInt(timeOut))
					.setConnectionRequestTimeout(Integer.parseInt(timeOut))
					.setSocketTimeout(Integer.parseInt(timeOut)).build();
			client = HttpClientBuilder.create().setDefaultRequestConfig(config)
					.build();

			post.addHeader("Mime-Version", "1.0");
			post.addHeader(HttpHeaders.CONTENT_TYPE, "application/PTI68");
			// post.addHeader(HttpHeaders.CONTENT_LENGTH, "1918");
			post.addHeader("Content-Transfer-Encoding", "text");
			post.addHeader("Request-number", "1");
			post.addHeader("Document-type", "Request");
			post.addHeader("Trace-number", getMaxUserRetries()); //GRE-2101 & GRE2218
			LOG.info("Request XML: [" + requestXML + "]");
			LOG.error("Request XML: [" + requestXML + "]");
			final StringEntity entity = new StringEntity(requestXML,
					ContentType.create("text/xml", Consts.UTF_8));
			post.setEntity(entity);
			final HttpResponse response = client.execute(post);
			return handleResponse(response);
		} catch (final MalformedURLException e) {
			LOG.error("Error occured during URL creation {}", e);
			return null;
		} catch (final SocketTimeoutException e) {
			LOG.error(
					"Timeout has occurred on Socket Read or Accept, please check your connection timeout settings {}",
					e);
			return null;
		} catch (final ClientProtocolException e) {
			LOG.error("caught ClientProtocolException performing POST {} "
					+ post.getURI() + "\n" + e);
			return null;
		} catch (final IOException e) {
			LOG.error("Unable to open the connection for data transfer..\n {}",
					e);
			return null;
		} finally {
			try {
				client.close();
			} catch (final IOException e) {
				LOG.error("Error during closing the stream: {}", e);
			}
		}
	}

	private Response handleResponse(final HttpResponse response) {
		Response serverResponse = null;
		String result = null;
		try {
			String statusCode = String.valueOf(response.getStatusLine()
					.getStatusCode());
			LOG.error("Response Code received: " + statusCode);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toString(response.getEntity());
				if (StringUtils.isNotEmpty(result)) {
					serverResponse = getPtAPIPaymentService().xmlToJAXBObject(
							result);
				}
				LOG.error("Response XML :[ " + result + " ]");
				return serverResponse;
			} else {
				if (statusCode.equals("412")) {
					LOG.error("ERR_NTFY_SUPPORT_0014 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0014 - IP Security Failure : A non-registered IP Address attempted to connect to the Orbital Gateway. The HTTP connection was refused as a result."
							+ statusCode);
				} else if (statusCode.equals("400")) {
					LOG.error("ERR_NTFY_SUPPORT_0015 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0015 - Invalid Request The server, due to malformed syntax, could not understand the request."
							+ statusCode);
				} else if (statusCode.equals("403")) {
					LOG.error("ERR_NTFY_SUPPORT_0016 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0016 - A Clear Text (or unencrypted) request was made to the Orbital Gateway. All transactions must be SSL Encrypted to interface to Orbital."
							+ statusCode);
				} else if (statusCode.equals("408")) {
					LOG.error("ERR_NTFY_SUPPORT_0017 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0017 -  Request Timed Out The Response could not be processed within the maximum time allowed."
							+ statusCode);
				} else if (statusCode.equals("500")) {
					LOG.error("ERR_NTFY_SUPPORT_0019 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0019 - Internal Server Error The server encountered an unexpected condition, which prevented it from fulfilling the request."
							+ statusCode);
				}else if (statusCode.equals("502")) {
							LOG.error("ERR_NTFY_SUPPORT_0019 - Error In Response XML :[ "
									+ result + " ]");
							LOG.error("ERR_NTFY_SUPPORT_0019 - Connection Error The server, while acting as a gateway or proxy, received an invalid response from the upstream server it accessed in attempting to fulfill the request."
									+ statusCode);
				} else {
					LOG.error("ERR_NTFY_SUPPORT_0020 - Error In Response XML :[ "
							+ result + " ]");
					LOG.error("ERR_NTFY_SUPPORT_0020 - Error."
							+ statusCode);
				}
				return serverResponse;
			}
		} catch (final JsonMappingException exception) {
			LOG.error("Error occurred while writing data from the respone XML: "
					+ exception.getMessage());
		} catch (final IOException e) {
			LOG.error("Error occurred while reading data from the respone" + e);
		}
		return null;

	}

	/**
	 * @return the ptAPIPaymentService
	 */
	public PTAPIPaymentService getPtAPIPaymentService() {
		return ptAPIPaymentService;
	}

	/**
	 * @param ptAPIPaymentService
	 *            the ptAPIPaymentService to set
	 */
	@Required
	public void setPtAPIPaymentService(
			final PTAPIPaymentService ptAPIPaymentService) {
		this.ptAPIPaymentService = ptAPIPaymentService;
	}

	/**
	 * @return the postXMLURL
	 */
	public String getPostXMLURL() {
		return postXMLURL;
	}

	/**
	 * @param postXMLURL
	 *            the postXMLURL to set
	 */
	@Required
	public void setPostXMLURL(final String postXMLURL) {
		this.postXMLURL = postXMLURL;
	}

	/**
	 * @return the timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	@Required
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

}
