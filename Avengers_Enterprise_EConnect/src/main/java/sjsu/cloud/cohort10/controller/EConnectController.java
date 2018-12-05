package sjsu.cloud.cohort10.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.GetUserProfileResponse;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserLoginRequest;
import sjsu.cloud.cohort10.service.EConnectService;

@RestController
public class EConnectController {

    @Autowired
    private EConnectService econnectService;
    
  //send email to friend for referral scenario
    @RequestMapping(value = "/getSocialEmailId", method = RequestMethod.GET, produces = "application/javascript")
       @ResponseBody
       public String getSocialEmailId(@RequestParam String accessToken) {
       String emailId = null;
       try {
                      emailId = this.econnectService.getSocialEmailId(accessToken);
              } catch (Exception e) {
                      e.printStackTrace();
              }
              return emailId;
       }


    //User sign up request mapping from CUSTOMER_INFO table
    @RequestMapping(value = "/userSignUp", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> userSignUp(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "emailId", required = true) String emailId,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "education", required = true) String education,
			@RequestParam(value = "industryExperience", required = true) String industryExperience,
			@RequestParam(value = "githubLink", required = true) String githubLink) {
    	Map<String, String> responseMap = null;
		try {
			responseMap = this.econnectService.newUserSignInRequest(file, firstName, lastName, emailId, password,
					education, industryExperience, githubLink, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}
    
    //User login in request mapping from CUSTOMER_INFO table
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = "application/json")
   	@ResponseBody
   	public Map<String, String> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
    	Map<String, String> responseMap = null;
   		try {
   			responseMap = this.econnectService.userLogin(userLoginRequest);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   		return responseMap;
   	}
    
    //logic to add the jobs by admin to JOBS table
	@RequestMapping(value = "/adminJobsPost", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> adminJobsPost(@RequestBody JobsPostRequest jobsPostRequest) {
		//System.out.println(jobsPostRequest.toString());
		Map<String, String> responseMap = null;
		try {
			responseMap = this.econnectService.adminJobsPost(jobsPostRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}
    
	//logic to get the list of jobs from JOBS table to display to the user
	@RequestMapping(value = "/getJobsList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<GetJobsResponse> getJobsList(@RequestParam String jobType, 
			@RequestParam String jobTitle,@RequestParam String emailId) {
    	List<GetJobsResponse> getJobsResponseList = null;
		try {
			
			getJobsResponseList = this.econnectService.getJobsList(jobType,jobTitle,emailId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getJobsResponseList;
	}
	
	//logic to insert the customer applied jobs to table JOBS_APPLIED
	@RequestMapping(value = "/postCustomerAppliedJobs", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> customerAppliedJobs(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "education", required = true) String education,
			@RequestParam(value = "experience", required = true) String experience,
			@RequestParam(value = "recruiterEmailId", required = true) String recruiterEmailId,
			@RequestParam(value = "userEmailId", required = true) String userEmailId,
			@RequestParam(value = "jobId", required = true) String jobId) {
		Map<String, String> responseMap = null;
		try {
			responseMap = this.econnectService.customerAppliedJobs(file, firstName, lastName,
					education, experience, recruiterEmailId, userEmailId, jobId, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(responseMap);
		return responseMap;
	}
	
	//logic to get the list of jobs applied by the customer for admin reference
	@RequestMapping(value = "/getCustomerAppliedJobsList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<GetCustomerAppliedJobs> getCustomerAppliedJobsList() {
    	List<GetCustomerAppliedJobs> getCustomerAppliedJobsList = null;
		try {
			
			getCustomerAppliedJobsList = this.econnectService.getCustomerAppliedJobsList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getCustomerAppliedJobsList;
	}
	
	//logic to the user profile information once he logs in
	@RequestMapping(value = "/getUserProfileDetails", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public GetUserProfileResponse getUserProfileDetails(@RequestParam String emailId) {
    	GetUserProfileResponse getUserProfileDetails = null;
		try {
			
			getUserProfileDetails = this.econnectService.getUserProfileDetails(emailId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getUserProfileDetails;
	}
	
}