/**
 *
 */
package com.greenlee.core.maxmind.services.impl;

import de.hybris.platform.util.Config;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.maxmind.services.MaxmindService;
import com.greenlee.core.maxmind.services.util.MaxmindRestClient;
import com.greenlee.core.maxmind.services.util.MaxmindReturnJson;


/**
 * @author xiaochen bian
 *
 */
public class DefaultMaxmindService implements MaxmindService
{
	private static final Logger LOG = Logger.getLogger(DefaultMaxmindService.class);

	private static final String[] HEADERS_TO_TRY =
	{ "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	private static final String UNKNOWN = "unknown";

	private int userId;
	private String userKey;
	private String host;
	private Integer connectTimeout;
	private Integer readTimeout;

	@Override
	public String getCountryForIP(final String ip)
	{
		final boolean isMaxmindTesting = Config.getBoolean("maxmind.use.testing", true);
		final String canadaIp = Config.getString("maxmind.use.canadaIp", "maxmind.use.canadaIp");
		MaxmindRestClient restClient = null;
		try
		{
			restClient = new MaxmindRestClient(String.valueOf(getUserId()), getUserKey());

			//		Testing code
			final MaxmindReturnJson result = restClient.getForObject(getHost() + ((isMaxmindTesting == true) ? canadaIp : ip),
					MaxmindReturnJson.class);
			return result.getCountry().getIso_code();

		}
		catch (final Exception e)
		{
			LOG.error("ERR_NTFY_SUPPORT_00012 - Maxmind Call failed");
			LOG.error("Error Message", e);
		}
		return null;
	}

	@Override
	public String getIpFromRequest(final HttpServletRequest httpRequest)
	{
		for (final String header : HEADERS_TO_TRY)
		{
			final String ip = httpRequest.getHeader(header);
			LOG.info(header + " " + ip);
			if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip))
			{
				return ip;
			}
		}
		return httpRequest.getRemoteAddr();
	}

	public int getUserId()
	{
		return userId;
	}

	@Required
	public void setUserId(final int userId)
	{
		this.userId = userId;
	}

	public String getUserKey()
	{
		return userKey;
	}

	@Required
	public void setUserKey(final String userKey)
	{
		this.userKey = userKey;
	}

	public String getHost()
	{
		return host;
	}

	@Required
	public void setHost(final String host)
	{
		this.host = host;
	}

	public Integer getConnectTimeout()
	{
		return connectTimeout;
	}

	public void setConnectTimeout(final Integer connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	public Integer getReadTimeout()
	{
		return readTimeout;
	}

	public void setReadTimeout(final Integer readTimeout)
	{
		this.readTimeout = readTimeout;
	}

}
