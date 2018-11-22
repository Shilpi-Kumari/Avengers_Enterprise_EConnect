package sjsu.cloud.cohort10.dto;

import java.io.Serializable;

public class GetCustomerAppliedJobs implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String customerEmailId;
	private GetJobsResponse getCustomerJobResponse;
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public GetJobsResponse getGetCustomerJobResponse() {
		return getCustomerJobResponse;
	}
	public void setGetCustomerJobResponse(GetJobsResponse getCustomerJobResponse) {
		this.getCustomerJobResponse = getCustomerJobResponse;
	}
	
}
