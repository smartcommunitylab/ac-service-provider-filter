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

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import eu.trentorise.smartcampus.ac.provider.AcProviderService;
import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.AcServiceException;
import eu.trentorise.smartcampus.ac.provider.model.Attribute;
import eu.trentorise.smartcampus.ac.provider.model.User;

/**
 * Implementation of AcService interface
 * 
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 5, 2012 2:36:31 PM
 */
public class AcClient implements AcService {

	private String endpointUrl;
	private AcProviderService service;

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

	private AcProviderService getService() throws AcServiceException {
		if (service == null) {
			try {
				init();
			} catch (JAXBException e) {
				throw new AcServiceException(e);
			}
		}
		return service;
	}

	@Override
	public User getUserByToken(String authToken) throws AcServiceException {
		return getService().getUserByToken(authToken);
	}

	@Override
	public boolean isValidUser(String authToken) throws AcServiceException {
		return getService().isValidUser(authToken);
	}

	@Override
	public List<Attribute> getUserAttributes(String authToken,
			String authority, String key) throws AcServiceException {
		return getService().getUserAttributes(authToken, authority, key);
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) throws AcServiceException {
		this.endpointUrl = endpointUrl;
		try {
			init();
		} catch (JAXBException e) {
			throw new AcServiceException();
		}
	}

	@Override
	public boolean canReadResource(String authToken, String resourceId) throws AcServiceException {
		return getService().canReadResource(authToken, resourceId);
	}

	
}