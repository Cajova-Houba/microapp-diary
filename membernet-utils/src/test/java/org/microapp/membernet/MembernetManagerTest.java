package org.microapp.membernet;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.microapp.membernet.vo.MembershipVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/applicationContext-membernet.xml"
})
@Transactional
public class MembernetManagerTest {
	
	@Autowired
	private MembernetManager membernetManager;
	
	//=====================================
	// tests for membership REST services
	//=====================================
	@Test
	public void testGetResponse1() {
		String link = "http://google.com";
		Response r = membernetManager.getResponse(link);
		assertEquals(r.getStatusInfo().getFamily(), Family.SUCCESSFUL);
	}
	
	@Test(expected = ProcessingException.class)
	public void testGetResponse2(){
		String link = "asdasdasdsad";
		Response r = membernetManager.getResponse(link);
	}
	
	@Test
	public void testExists1() {
		long membershipId = -100;
		
		assertFalse(membernetManager.exists(membershipId));
	}
	
	@Test
	public void testExists2() {
		long membershipId = 1;
		
		assertTrue(membernetManager.exists(membershipId));
	}
	
	@Test
	public void testIsAdmin1() {
		long membershipId = 1;
		
		assertTrue(membernetManager.isAdmin(membershipId));
	}
	
	@Test
	public void testIsAdmin2() {
		long membershipId = -2;
		
		assertFalse(membernetManager.isAdmin(membershipId));
	}
	
	@Test
	public void testGet1() {
		long membershipId = 1;
		
		MembershipVO m = membernetManager.getMembership(membershipId);
		assertEquals(membershipId, m.getId().longValue());
		assertTrue(m.isIsSocietyAdmin());
	}
	
	@Test
	public void testGet2() {
		long membershipId = -100;
		
		MembershipVO m = membernetManager.getMembership(membershipId);
		assertNull(m);
	}
	
	/**
	 * Target and requester is the same person, result should be true.
	 */
	@Test
	public void testCanAccess1() {
		long targetId = 1;
		long requesterId = 1;
		
		assertTrue(membernetManager.canAccess(requesterId, targetId));
	}
	
	/**
	 * Requester is admin of the society the target is a member of. Result should be true.
	 */
	@Test
	public void testCanAccess2() {
		long targetId = 6;
		long requesterId = 1;
		
		assertTrue(membernetManager.canAccess(requesterId, targetId));
	}
	
	/**
	 * Requester and target are in the same society, but the requester isn't admin. Result should be false.
	 */
	@Test
	public void testCanAccess3() {
		long targetId = 1;
		long requesterId = 6;
		
		assertFalse(membernetManager.canAccess(requesterId, targetId));
	}
	
	/**
	 * Requester isn't in the same society as target. Result should be false.
	 */
	@Test
	public void testCanAccess4() {
		long targetId = 6;
		long requesterId = -4;
		
		assertFalse(membernetManager.canAccess(requesterId, targetId));
	}
	
	@Test
	public void testListAll1() {
		long societyId = 1;
		
		List<MembershipVO> memberships = membernetManager.listAll(societyId);
		assertFalse(memberships.isEmpty());
	}
	
	@Test
	public void testListAll2() {
		long societyId = -100;
		
		List<MembershipVO> memberships = membernetManager.listAll(societyId);
		assertTrue(memberships.isEmpty());
	}
}

