package sg.edu.nus.iss.springboot.voucher.management.model;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import lombok.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.RoleType;

@Data
public class UserResponse {

	
	private String message;
	private ArrayList<ResultItem> result;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<ResultItem> getResult() {
		return result;
	}

	public void setResult(ArrayList<ResultItem> result) {
		this.result = result;
	}

	public static class ResultItem {

		private String email;
		private String username;
		private RoleType role;

		public ResultItem() {
			super();
		}

		public ResultItem(String email, String username, RoleType role) {
			super();
			this.email = email;
			this.username = username;
			this.role = role;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public RoleType getRole() {
			return role;
		}

		public void setRole(RoleType role) {
			this.role = role;
		}
		

	}

}
