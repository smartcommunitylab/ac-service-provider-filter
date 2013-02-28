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

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;

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

	private WebClient getClient() {
		return WebClient.create(endpointUrl);
	}

	@Override
	public User getUserByToken(String authToken) throws AcServiceException {
		return getClient().path("/users/me").header("AUTH_TOKEN", authToken).accept("application/xml").get(User.class);
	}

	@Override
	public boolean isValidUser(String authToken) throws AcServiceException {
		return getClient().path("/users/me/validity").header("AUTH_TOKEN", authToken).accept("application/xml").get(Boolean.class);
	}

	@Override
	public List<Attribute> getUserAttributes(String authToken, String authority, String key) throws AcServiceException {
		WebClient client = getClient().path("/users/me/attributes").header("AUTH_TOKEN", authToken);
		if (authority != null){
			client.path("/authorities/"+authority);
			if (key != null) {
				client.path("/keys/"+key);
			}
		}
		return new ArrayList<Attribute>(client.accept("application/xml").getCollection(Attribute.class));
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) throws AcServiceException {
		this.endpointUrl = endpointUrl;
	}

	@Override
	public boolean canReadResource(String authToken, String resourceId) throws AcServiceException {
		return getClient().path("/resources/"+resourceId+"/access").header("AUTH_TOKEN", authToken).accept("application/xml").get(Boolean.class);
	}

	
}