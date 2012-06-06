package eu.trentorise.smartcampus.ac.provider.filters;

import eu.trentorise.smartcampus.ac.provider.AcProviderService;
import eu.trentorise.smartcampus.ac.provider.model.User;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 *
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 4, 2012 3:22:53 PM
 */
//@Component(value="acProviderFilter")
public class AcProviderFilter extends AbstractPreAuthenticatedProcessingFilter {

    String principalRequestHeader = "AUTH_TOKEN";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String s = request.getHeader(principalRequestHeader);
        return s;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String s = request.getHeader(principalRequestHeader);
        return s;
    }

    public void setPrincipalRequestHeader(String principalRequestHeader) {
        Assert.hasText(principalRequestHeader,
                "principalRequestHeader must not be empty or null");
        this.principalRequestHeader = principalRequestHeader;
    }
}
