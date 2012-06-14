package eu.trentorise.smartcampus.ac.provider.filters;

import eu.trentorise.smartcampus.ac.provider.AcProviderService;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 5, 2012 2:36:31 PM
 */
public class SpringAcProvider implements AuthenticationProvider {

	// @Value("${ac.endpoint.url}")
	private String endpointUrl;
	private AcProviderService service;

	@PostConstruct
	private void init() throws JAXBException {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(AcProviderService.class);
		factory.setAddress(endpointUrl);

		service = (AcProviderService) factory.create();

		Client client = ClientProxy.getClient(service);
		if (client != null) {
			HTTPConduit conduit = (HTTPConduit) client.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			policy.setConnectionTimeout(10000);
			policy.setReceiveTimeout(10000);
			policy.setAllowChunking(false);
			conduit.setClient(policy);
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String token = authentication.getPrincipal().toString();
		if (!service.isValidUser(token)) {
			throw new BadCredentialsException(
					"Authentication token is absent or expired");
		}
		authentication.setAuthenticated(true);
		return authentication;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return PreAuthenticatedAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

}