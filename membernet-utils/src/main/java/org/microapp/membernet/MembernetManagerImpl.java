package org.microapp.membernet;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.util.privilegedactions.GetAnnotationParameter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yoso.dev.membernet.membership.domain.Membership;
import com.yoso.dev.membernet.membership.service.MembershipManager;

public class MembernetManagerImpl implements MembernetManager{

	@Autowired
	private MembershipManager membershipManager;
	
	/**
	 * 
	 */
	public boolean canAccess(long requesterId, long targetId) {
		//check if the ids are same
		if (requesterId == targetId) {
			return true;
		}
		
		//get society ids
		long requesterSId = membershipManager.get(requesterId).getSocietyId();
		long targetSId = membershipManager.get(targetId).getSocietyId();
		
		//check if societies are the same, if not, return false
		if (requesterSId != targetSId) {
			return false;
		}
		
		//check if requester is an admin of the society, if not, return false
		if (membershipManager.get(requesterId).isSocietyAdmin()) {
			return true;
		} else {
			return false;
		}
		
	}

	public List<Membership> listAll() {
		return membershipManager.getAll();
	}

	public List<Membership> listAll(long societyId) {
		//this should be done by direct database query in DAO
		List<Membership> all = membershipManager.getAll();
		List<Membership> res = new ArrayList<Membership>();
		
		for(Membership m : all) {
			if (m.getSocietyId() == societyId) {
				res.add(m);
			}
		}
		
		return res;
		
	}

	public boolean exists(long id) {
		return membershipManager.exists(id);
	}

	public boolean isAdmin(long membershipId) {
		if (membershipManager.exists(membershipId)) {
			return membershipManager.get(membershipId).isSocietyAdmin();
		} else {
			return false;
		}
	}

	public Membership getMembership(long memberId) {
		return membershipManager.get(memberId);
	}

	
	
}
