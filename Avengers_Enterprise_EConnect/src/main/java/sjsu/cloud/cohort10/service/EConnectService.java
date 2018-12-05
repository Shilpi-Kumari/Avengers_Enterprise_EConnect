package sjsu.cloud.cohort10.service;

import java.util.List;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.springframework.web.multipart.MultipartFile;

import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.GetUserProfileResponse;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserLoginRequest;

public interface EConnectService
{
    Map<String, String> newUserSignInRequest(MultipartFile file, String firstName, String lastName, 
    		String emailId, String password, String education, 
    		String industryExperience, String githubLink, boolean enableAccess);
    
    Map<String,String> userLogin(UserLoginRequest userLoginRequest) throws ValidationException;
    
    Map<String, String> adminJobsPost(JobsPostRequest jobsPostRequest);
    
    List<GetJobsResponse> getJobsList(String jobType, String jobTitle, String emailId);
    
    Map<String, String> customerAppliedJobs(String userEmailId, String jobId);
    
    List<GetCustomerAppliedJobs> getCustomerAppliedJobsList();
    
    GetUserProfileResponse getUserProfileDetails(String emailId);
    
    String getSocialEmailId(String accessToken);
    
}