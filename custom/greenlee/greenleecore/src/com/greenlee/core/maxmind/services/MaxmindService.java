/**
 *
 */
package com.greenlee.core.maxmind.services;

import javax.servlet.http.HttpServletRequest;


/**
 * @author xiaoc
 *
 */
public interface MaxmindService
{
	String getCountryForIP(String ip);

	String getIpFromRequest(HttpServletRequest httpRequest);
}
