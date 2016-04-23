package org.microapp.membernet;

import java.util.List;

import javax.ws.rs.core.Response;

import org.microapp.membernet.vo.MembershipVO;


public interface MembernetManager {
	
	/**
	 * Checks if the membership exists.
	 * @param id Id of membership.
	 * @return True if membership exists.
	 */
	public boolean exists(long id);
	
	/**
	 * Check if the member with requesterId can access (view and change) the data of member with targetId.
	 * Typically one member access other member's data if:
	 *   1. Requester is the target (ids are same)
	 *   2. Requester is an admin of society the target is member of.
	 * @param requesterId Membership Id of requester member.
	 * @param targetId Membership Id of target member.
	 * @return True if requester can access, false if he can't.
	 */
	public boolean canAccess(long requesterId, long targetId);
	
	/**
	 * List all memberships in the society.
	 * @param societyId Id of the society.
	 * @return List of memberships.
	 */
	public List<MembershipVO> listAll(long societyId);
	
	/**
	 * Returns true if the membership is an admin of society.
	 * @param membershipId Id of membership.
	 * @return True if this member is also admin in society.
	 */
	public boolean isAdmin(long membershipId);
	
	/**
	 * Returns a membership for the desired Id.
	 * @param memberId Id of membership.
	 * @return Membership or null if it doesn't exist.
	 */
	public MembershipVO getMembership(long memberId);
	
	/**
	 * Public for testing purposes, remove in product version.
	 * @param link
	 * @return
	 */
	public Response getResponse(String link); 
}
