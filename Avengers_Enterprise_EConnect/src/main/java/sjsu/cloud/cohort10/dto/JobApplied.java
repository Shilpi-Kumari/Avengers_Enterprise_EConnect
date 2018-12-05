package sjsu.cloud.cohort10.dto;

import java.io.Serializable;

public class JobApplied implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String customerEmailId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	
}
