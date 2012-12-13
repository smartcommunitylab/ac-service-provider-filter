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
	public List<User> getUsersByAttributes(List<Attribute> attributes)
			throws AcServiceException {
		return getService().getUsersByAttributes(attributes);
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
	public void setAttribute(long userId, Attribute attribute)
			throws AcServiceException {
		getService().setAttribute(userId, attribute);

	}

}