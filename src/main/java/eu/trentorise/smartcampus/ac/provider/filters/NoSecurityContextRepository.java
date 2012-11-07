package eu.trentorise.smartcampus.ac.provider.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

public class NoSecurityContextRepository implements SecurityContextRepository {

	@Override
	public SecurityContext loadContext(
			HttpRequestResponseHolder requestResponseHolder) {
		return null;
	}

	@Override
	public void saveContext(SecurityContext context,
			HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return false;
	}

}
