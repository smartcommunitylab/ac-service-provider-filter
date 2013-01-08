package eu.trentorise.smartcampus.ac.provider.filters;

import javax.xml.bind.JAXBException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import eu.trentorise.smartcampus.ac.provider.AcProviderService;
import eu.trentorise.smartcampus.ac.provider.AcServiceException;

/**
 * Filter to check authentication of user in web application
 * 
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 5, 2012 2:36:31 PM
 */
public class SpringAcProvider implements AuthenticationProvider {

	private String endpointUrl;
	private AcProviderService service;

	public SpringAcProvider(String endpointUrl) throws JAXBException {
		super();
		this.endpointUrl = endpointUrl;
		init();
	}

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

	/**
	 * Checks if the authentication token is yet valid
	 * 
	 * @param authentication
	 *            spring authentication object
	 * @return the authentication object with authenticated flag setted true if
	 *         authentication token is yet valid
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String token = authentication.getPrincipal().toString();
		try {
			boolean valid = service.isValidUser(token);
			if (!valid) {
				throw new BadCredentialsException(
						"Authentication token is absent or expired");
			}
			authentication.setAuthenticated(true);
			return authentication;
		} catch (AcServiceException e) {
			throw new AuthenticationServiceException(
					"Problem accessing AC provider service: " + e.getMessage());
		}

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