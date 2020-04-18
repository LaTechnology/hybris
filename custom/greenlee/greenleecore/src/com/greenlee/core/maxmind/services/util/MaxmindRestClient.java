package com.greenlee.core.maxmind.services.util;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * @author xiaochen bian
 *
 */
public class MaxmindRestClient extends RestTemplate
{
	public MaxmindRestClient(final String username, final String password)
	{
		final CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials(username, password));
		final HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
	}
}
