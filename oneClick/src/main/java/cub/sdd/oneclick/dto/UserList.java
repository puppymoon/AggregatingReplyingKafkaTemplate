package cub.sdd.oneclick.dto;

public class UserList {

	String userA;
	String userB;
	String userC;

	String[] queryTopicA;
	String[] queryTopicB;
	String[] queryTopicC;

	public String getUserA() {
		return userA;
	}

	public void setUserA(String userA) {
		this.userA = userA;
	}

	public String getUserB() {
		return userB;
	}

	public void setUserB(String userB) {
		this.userB = userB;
	}

	public String getUserC() {
		return userC;
	}

	public void setUserC(String userC) {
		this.userC = userC;
	}

	public String[] getQueryTopicA() {
		return queryTopicA;
	}

	public void setQueryTopicA(String[] queryTopicA) {
		this.queryTopicA = queryTopicA;
	}

	public String[] getQueryTopicB() {
		return queryTopicB;
	}

	public void setQueryTopicB(String[] queryTopicB) {
		this.queryTopicB = queryTopicB;
	}

	public String[] getQueryTopicC() {
		return queryTopicC;
	}

	public void setQueryTopicC(String[] queryTopicC) {
		this.queryTopicC = queryTopicC;
	}

}