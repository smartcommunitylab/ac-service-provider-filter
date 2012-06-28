package eu.trentorise.smartcampus.ac.provider.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.Assert;

/**
 *
 * @author Viktor Pravdin <pravdin@disi.unitn.it>
 * @date Jun 4, 2012 3:22:53 PM
 */
//@Component(value="acProviderFilter")
public class AcProviderFilter extends AbstractPreAuthenticatedProcessingFilter {

	public static final String TOKEN_HEADER = "AUTH_TOKEN";
	
    String principalRequestHeader = TOKEN_HEADER;

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
