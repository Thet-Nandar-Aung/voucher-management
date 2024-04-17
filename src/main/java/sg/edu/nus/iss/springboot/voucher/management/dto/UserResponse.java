package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import lombok.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;

public class UserResponse {

	private String message;
	private ArrayList<ResultItem> result= new ArrayList<>();;

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
		private String image;

		public ResultItem() {
			super();
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

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

	}

}
