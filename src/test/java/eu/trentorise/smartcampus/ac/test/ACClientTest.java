/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.ac.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.ac.provider.AcServiceException;
import eu.trentorise.smartcampus.ac.provider.filters.AcClient;
import eu.trentorise.smartcampus.ac.provider.model.Attribute;
import eu.trentorise.smartcampus.ac.provider.model.User;

/**
 * @author raman
 *
 */
public class ACClientTest {

	AcClient client = null;
	
	@Before
	public void setUp() throws AcServiceException {
		client = new AcClient();
		client.setEndpointUrl("https://vas-dev.smartcampuslab.it/acService");
	}
	
	@Test
	public void testReadUser() throws AcServiceException {
		// call the user
		User user = client.getUserByToken("123");
		assert user != null;
	}
	
	@Test
	public void testReadAttributes() throws AcServiceException {
		List<Attribute> 
		list = client.getUserAttributes("123","authority","key");
		assert list.size() > 0;
		list = client.getUserAttributes("123","authority", null);
		assert list.size() > 0;
		list = client.getUserAttributes("123",null, null);
		assert list.size() > 0;
	}

	@Test
	public void testValidUser() throws AcServiceException {
		assert client.isValidUser("123");
	}

	@Test
	public void testResourceAvailable() throws AcServiceException {
		assert client.canReadResource("123","1");
	}

}
