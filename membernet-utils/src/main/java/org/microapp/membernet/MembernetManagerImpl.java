package org.microapp.membernet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.microapp.membernet.vo.MembershipVO;
import org.microapp.membernet.vo.MembershipVOReader;
import org.microapp.membernet.vo.MembershipVOs;
import org.microapp.membernet.vo.MembershipVOsReader;

public class MembernetManagerImpl implements MembernetManager{

	/**
	 * File with links to REST API
	 */
	private static final String PROPERTY_FILE_NAME = "rest.properties";
	
	//keys to those links
	private final String MEMBERSHIP_GET = "membership.get";
	private final String MEMBERSHIP_IS_SOCIETY_ADMIN = "membership.isSocietyAdmin";
	private final String MEMBERS_OF_SOCIETY = "membership.societyMembers";
	
	private Properties properties;
	
	public MembernetManagerImpl() {
		System.setProperty("javax.xml.bind.context.factory","org.eclipse.persistence.jaxb.JAXBContextFactory");
		
		//load the configuration from property file
		InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
		if(input != null) {
			try {
				properties = new Properties();
				properties.load(input);
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				properties = null;
			}
		}
	}
	
	public boolean exists(long id) {
		
		if (properties == null) {
			return false;
		}
		
		String link = properties.getProperty(MEMBERSHIP_GET);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",id);
		Response r = getResponse(link,params);
		
		if(r.getStatus() == Response.Status.OK.getStatusCode()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canAccess(long requesterId, long targetId) {
		// TODO Auto-generated method stub
		
		//check if the ids are same
		if (requesterId == targetId) {
			return true;
		}
		
		//get membership objects
		MembershipVO requesterMvo = getMembership(requesterId);
		MembershipVO targetMvo = getMembership(targetId);
		
		//check if societies are the same, if not, return false
		if (requesterMvo.getSociety().getId() != targetMvo.getSociety().getId()) {
			return false;
		}
		
		//check if requester is an admin of the society, if not, return false
		if (requesterMvo.isIsSocietyAdmin()) {
			return true;
		} else {
			return false;
		}
		
	}

	public List<MembershipVO> listAll(long societyId) {
		
		List<MembershipVO> memberships = new ArrayList<MembershipVO>();
		
		if (properties == null) {
			return memberships;
		}
		
		String link = properties.getProperty(MEMBERS_OF_SOCIETY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",societyId);
		Response r = getResponse(link,params);
		
		if(r.getStatus() == Response.Status.OK.getStatusCode()) {
			try {
				//should contain either true or false
				MembershipVOs map = r.readEntity(MembershipVOs.class);
				if(!map.isEmpty()) {
					memberships.addAll(map.getItems());
				}
				
				return memberships;
			} catch (Exception e) {
				e.printStackTrace();
				return memberships;
			}
		} else {
			return memberships;
		}
		
	}

	public boolean isAdmin(long membershipId) {
		if (properties == null) {
			return false;
		}
		
		String link = properties.getProperty(MEMBERSHIP_IS_SOCIETY_ADMIN);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",membershipId);
		Response r = getResponse(link,params);
		
		if(r.getStatus() == Response.Status.OK.getStatusCode()) {
			try {
				//should contain either true or false
				String resp = r.readEntity(String.class);
				return Boolean.parseBoolean(resp);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public MembershipVO getMembership(long memberId) {
		if (properties == null) {
			return null;
		}
		
		String link = properties.getProperty(MEMBERSHIP_GET);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",memberId);
		Response r = getResponse(link,params);
		
		if(r.getStatus() == Response.Status.OK.getStatusCode()) {
			try {
				MembershipVO m = r.readEntity(MembershipVO.class);
				return m;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Response getResponse(String link, Map<String, Object> params) {
		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic("zdenek.vales@yoso.fi", "123456789");
		Client client = ClientBuilder.newClient();
		client.register(auth);
		client.register(MembershipVOReader.class);
		client.register(MembershipVOsReader.class);
		WebTarget resource = client.target(link);
		if(!params.isEmpty()) {
			resource = resource.resolveTemplates(params);
		}
		Builder request = resource.request(MediaType.APPLICATION_JSON);
		
		return request.get();
	}
	
	public Response getResponse(String link) {
		return getResponse(link, new HashMap<String, Object>(0));
	}
	
	
	
}
