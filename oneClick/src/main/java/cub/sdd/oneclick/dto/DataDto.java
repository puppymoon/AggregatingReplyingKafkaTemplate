package cub.sdd.oneclick.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@JsonInclude(Include.NON_NULL)
public class DataDto {

	@JsonProperty("obj")
	List<User> list;

	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
		this.list = list;
	}

	public static class User {
		private String userId;
		private String queryTopic1;
		private String queryTopic2;
		private String queryTopic3;

		// Getter Methods

		public String getUserId() {
			return userId;
		}

		public String getQueryTopic1() {
			return queryTopic1;
		}

		public String getQueryTopic2() {
			return queryTopic2;
		}

		public String getQueryTopic3() {
			return queryTopic3;
		}

		// Setter Methods

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public void setQueryTopic1(String queryTopic1) {
			this.queryTopic1 = queryTopic1;
		}

		public void setQueryTopic2(String queryTopic2) {
			this.queryTopic2 = queryTopic2;
		}

		public void setQueryTopic3(String queryTopic3) {
			this.queryTopic3 = queryTopic3;
		}
	}

}
