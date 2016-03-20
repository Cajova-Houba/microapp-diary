package org.microapp.membernet;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
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
	
	@Test
	public void testCanAccess() {
		//member with id = 2L is admin of society the member with id = 6l is member of
		long requesterId = 2l;
		long targetId = 6l;
		
		assertTrue(membernetManager.canAccess(requesterId, targetId));
	}
}
