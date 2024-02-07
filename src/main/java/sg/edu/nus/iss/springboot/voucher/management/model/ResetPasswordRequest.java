package sg.edu.nus.iss.springboot.voucher.management.model;

public class ResetPasswordRequest {
	private String password;

	public ResetPasswordRequest() {
		super();
	}

	public ResetPasswordRequest(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
