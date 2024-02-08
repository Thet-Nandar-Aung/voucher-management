package sg.edu.nus.iss.springboot.voucher.management.model;

public class ResetPasswordRequest {

	private String email;
	private String password;

	public ResetPasswordRequest() {
		super();
	}

	public ResetPasswordRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
