package sg.edu.nus.iss.springboot.voucher.management.utility;


public class GeneralUtility {

	public static String makeNotNull(Object str) {
		if (str == null) {
			return "";
		} else if (str.equals("null")) {
			return "";
		} else {
			return str.toString();
		}
	}

}
