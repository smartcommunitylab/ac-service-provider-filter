/**
 *    Copyright 2012-2013 Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.smartcampus.ac.provider.filters;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Filter to check authentication of user in web application
 * 
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 5, 2012 2:36:31 PM
 */
public class SpringAcProvider implements AuthenticationProvider {

	private String endpointUrl;

	public SpringAcProvider(String endpointUrl) throws JAXBException {
		super();
		this.endpointUrl = endpointUrl;
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
			boolean valid = WebClient.create(endpointUrl).path("/users/me/validity").header("AUTH_TOKEN", token).accept("application/json").get(Boolean.class);
			if (!valid) {
				throw new BadCredentialsException(
						"Authentication token is absent or expired");
			}
			authentication.setAuthenticated(true);
			return authentication;
		} catch (WebApplicationException e) {
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