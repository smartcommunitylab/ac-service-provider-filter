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
import eu.trentorise.smartcampus.ac.provider.model.Attribute;
import eu.trentorise.smartcampus.ac.provider.model.User;

/**
 * 
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 5, 2012 2:36:31 PM
 */
public class AcClient implements AcService {

	// @Value("${ac.endpoint.url}")
	private String endpointUrl;
	private AcProviderService service;

	public void init() throws JAXBException {
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
	public User getUserByToken(String authToken) {
		return service.getUserByToken(authToken);
	}

	@Override
	public List<User> getUsersByAttributes(List<Attribute> attributes) {
		return service.getUsersByAttributes(attributes);
	}

	@Override
	public boolean isValidUser(String authToken) {
		return service.isValidUser(authToken);
	}

	@Override
	public List<Attribute> getUserAttributes(String authToken,
			String authority, String key) {
		return service.getUserAttributes(authToken, authority, key);
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

}