
package sjsu.cloud.cohort10.dao;

import java.util.List;
import java.util.Map;

import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.GetUserProfileResponse;
import sjsu.cloud.cohort10.dto.JobApplied;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserLoginRequest;
import sjsu.cloud.cohort10.dto.UserSignInRequest;

public interface EConnectDAO {
	
	Map<String, String> createUser(UserSignInRequest userRequest);
	
	Map<String, String> getUserDetails(UserLoginRequest userLoginRequest);
	
	Map<String, String> adminJobsPost(JobsPostRequest jobsPostRequest);
	
	List<GetJobsResponse> getJobsList(String jobType, String jobTitle);
	
	Map<String, String> customerAppliedJobs(String userEmailId, String jobId);
	
	List<GetCustomerAppliedJobs> getCustomerAppliedJobsList();
	
	GetUserProfileResponse getUserProfileDetails(String emailId);
	
	List<JobApplied> getJobsAppliedList();
	
	List<GetJobsResponse> getCompleteJobsList();
	
}
