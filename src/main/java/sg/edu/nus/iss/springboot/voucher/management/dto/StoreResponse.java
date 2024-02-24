package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.util.ArrayList;

public class StoreResponse {

	private String message;

	private ArrayList<ResultStore> result;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<ResultStore> getResult() {
		return result;
	}

	public void setResult(ArrayList<ResultStore> result) {
		this.result = result;
	}

	public static class ResultStore {
		private String storeId;
		private String storeName;

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

	}

}
