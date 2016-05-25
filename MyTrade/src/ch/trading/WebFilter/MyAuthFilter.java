package ch.trading.WebFilter;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.traiding.model.User;

/**
* @author  Joshua	Blöchliger
* @version 1.1
*/

@WebFilter("/faces/private/*") // oder z. B. @WebFilter("/privat/*")
public class MyAuthFilter implements Filter {

	boolean debug = true;
	private void debugOut(String meldung) {
		if(debug) {
			System.out.println("Debug MyAuthFilter." + meldung);
		}
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		debugOut("init(): AuthFilter...");
	} 

/**
 * Versuche, die Session aus dem Request zu holen.
 * Ist das nicht möglich, so gehe über den FacesContext.
 */
	HttpSession holeSessionVariable(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(null == session) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			if(null == facesContext || null == facesContext.getExternalContext()) {
				debugOut("holeSessionVariable(): No session!");
			} else {
				session = (HttpSession) facesContext.getExternalContext().getSession(true);            		
			}
		}
		return session;
	}


	String loginUrl = "/MyTrade/faces/Login.xhtml";

	boolean istLoginURL(HttpServletRequest request) {
		String reqString = request.getRequestURI();
		debugOut("istLoginURL(): reqString: [" + reqString + "]");
		return reqString.contains(loginUrl);
	}
	
	String adminUrl = "/MyTrade/faces/private/admin/";
	
	boolean istAdminURL(HttpServletRequest request){
		String reqString = request.getRequestURI();
		debugOut("istAdminURL(): reqString: [" + reqString + "]");
		return reqString.contains(adminUrl);
	}
	
String haendlerUrl = "/MyTrade/faces/private/haendler/";
	
	boolean istHaendlerURL(HttpServletRequest request){
		String reqString = request.getRequestURI();
		debugOut("istHaendlerURL(): reqString: [" + reqString + "]");
		return reqString.contains(haendlerUrl);
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
			debugOut("doFilter(): start...");

			eigenerDoHTTPFilter((HttpServletRequest)  req, 
			                    (HttpServletResponse) res, 
			                    chain);	

			debugOut("doFilter(): ... done.");
		} catch (Exception ex) {
			System.out.println("Exception im MyAuthFilter " + ex);
			ex.printStackTrace();
		}
	}


	/**
	 * Wie "doFilter", doch a) mit throws, statt try-catch und
	 *                      b) mit HttpServlet, statt Servlet
	 */
	private void eigenerDoHTTPFilter(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(null == holeSessionVariable(request)) {
			behandleLeereSession(request, response, chain);
			return;
		}

		if(istLoginURL(request)) {
			debugOut("eigenerDoHTTPFilter(): Request is login request!");
			chain.doFilter(request, response); // hier drauf darf eingentlich jeder
			return;
		}

		User user = (User) holeSessionVariable(request).getAttribute("benutzer");
		if(null == user && istOeffentlicheSeite(request)) {
			debugOut("eigenerDoHTTPFilter(): Request ist freie Seite");
			chain.doFilter(request, response); // jeder, da öffentlich	
			return;
		}

		if(null == user) { // hier aber keine freie Seite
			debugOut("eigenerDoHTTPFilter(): user ist null, aber nicht freie Seite!");	
			response.sendRedirect(loginUrl);
			return;
		}
//		1=Admin
		if(1 == user.getRole() && istAdminURL(request)){
			debugOut("eigenerDoHTTPFilter(): user ist Admin und will auf AdminSeite");
			chain.doFilter(request, response);
			return;
		}
//		1=Admin
		if(1 == user.getRole() && istHaendlerURL(request)){
			debugOut("eigenerDoHTTPFilter(): user ist Admin und will auf HaendlerSeite");
			
			response.sendRedirect(adminUrl + "Admin.xhtml");
			//Vllt. noch message
			return;
		}
//		2=Haendler
		if(2 == user.getRole() && istHaendlerURL(request)){
			debugOut("eigenerDoHTTPFilter(): user ist Haendler und will auf HaendlerSeite");
			chain.doFilter(request, response);
			return;
		}
//		2=Haendler
		if(2 == user.getRole() && istAdminURL(request)){

			response.sendRedirect(haendlerUrl + "Portfolio.xhtml");
		}

		debugOut("  Session: " + holeSessionVariable(request));
		debugOut("  User:    " + user                        );
		chain.doFilter(request, response); // darf weiterleiten, da eingeloggt
	}


	/**
	 * Prüfe, ob die Seite ein Login braucht. "true", falls die Seite 
	 * ohne "Login" sichtbar sein darf.
	 */
	private boolean istOeffentlicheSeite(HttpServletRequest req) {
		String reqString = req.getRequestURI();
		return reqString.contains("Login.xhtml");
	}


	/**
	 * Wie ist vorzugehen, wenn noch keine Session angelegt wurde?
	 */
	private void behandleLeereSession(HttpServletRequest  request,
	                                  HttpServletResponse response,
	                                  FilterChain         chain)
	             throws IOException, ServletException 
	{
		debugOut("behandleLeereSession(): Session ist null");
		if(istOeffentlicheSeite(request) || istLoginURL(request)) {
			chain.doFilter(request, response);	
		} else {
			response.sendRedirect(loginUrl);
		}
	}


	@Override
	public void destroy() {
		// must be overriden
	}

} // end class MyAuthFilter
