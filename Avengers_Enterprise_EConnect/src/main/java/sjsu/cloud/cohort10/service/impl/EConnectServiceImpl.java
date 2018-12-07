package sjsu.cloud.cohort10.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.swing.plaf.synth.Region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import sjsu.cloud.cohort10.dao.EConnectDAO;
import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.GetUserProfileResponse;
import sjsu.cloud.cohort10.dto.JobApplied;
import sjsu.cloud.cohort10.dto.JobsCountResponse;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserDetailsDTO;
import sjsu.cloud.cohort10.dto.UserLoginRequest;
import sjsu.cloud.cohort10.dto.UserSignInRequest;
import sjsu.cloud.cohort10.helper.AWSSimpleEmailServiceHelper;
import sjsu.cloud.cohort10.service.EConnectService;

@Component
public class EConnectServiceImpl implements EConnectService
{
    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(EConnectServiceImpl.class);

    @Autowired
    public EConnectServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket)
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }
    
    @Autowired
    EConnectDAO econnectDAO;
    
    @Autowired
    AWSSimpleEmailServiceHelper sesHelper;
    
    @Override
    public String getSocialEmailId(String accessToken) {
          
           DecodedJWT jwt = JWT.decode(accessToken);
           String emailId = jwt.getClaim("sub").asString();
          
           return emailId;
    }
    
    
    @Override
	public Map<String,String> userLogin(UserLoginRequest userLoginRequest){
    	
    	//logic to get the user details to verify login information
    	Map<String, String> outputMap = econnectDAO.getUserDetails(userLoginRequest);
		return outputMap;
	}

	@Override
	public Map<String, String> adminJobsPost(JobsPostRequest jobsPostRequest) {
		
		//logic to post the jobs in DB by the admin
		Map<String, String> outputMap = econnectDAO.adminJobsPost(jobsPostRequest);
		return outputMap;
	}

	@Override
	public List<GetJobsResponse> getJobsList(String jobType, String jobTitle, String emailId) {
		
		//logic to get the jobs from DB
		List<GetJobsResponse> getAvailableJobsList = econnectDAO.getJobsList(jobType, jobTitle);
		
		//logic to get the list of jobs already applied by the customer
		List<JobApplied> getJobsAppliedList = econnectDAO.getJobsAppliedList();
		
		for(GetJobsResponse availableJobs : getAvailableJobsList) {
			for (JobApplied userAppliedJob : getJobsAppliedList) {
				if (availableJobs.getId() == Integer.valueOf(userAppliedJob.getId()) && emailId.equalsIgnoreCase(userAppliedJob.getCustomerEmailId())) {
					availableJobs.setAlreadyApplied("true");
				}
			}
		}
		return getAvailableJobsList;
	}

	@Override
	public Map<String, String> customerAppliedJobs(MultipartFile multipartFile, String firstName, String lastName,
			String education, String experience, String recruiterEmailId, String userEmailId, String jobId,
			boolean enablePublicReadAccess) {

		//logic to upload the resume to s3
		String uploadFileName = multipartFile.getOriginalFilename();
		
		 Map<String, String> outputMap = new HashMap<>();
		 try {
	            File file = new File(uploadFileName);
	            FileOutputStream fos = new FileOutputStream(file);
	            fos.write(multipartFile.getBytes());
	            fos.close();
	            
	            String key = userEmailId + "/" + uploadFileName;

	            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, key, file);

	            if (enablePublicReadAccess) {
	                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
	            }
	            this.amazonS3.putObject(putObjectRequest);
	            
	            //logic to get the document from cloud front url
	            String cloudFrontUrl = "http://d1o2tgjd3ar7cr.cloudfront.net/"+userEmailId+"/"+uploadFileName;
	            
	            //logic to send the email to recruiter of the job applied by user with the details using AWS SES
	            sesHelper.recruiterEmailMessage(firstName, lastName, userEmailId, education,
	            		experience, cloudFrontUrl, recruiterEmailId, jobId);
	            
	          //logic to post the jobs in DB by the admin
	            outputMap = econnectDAO.customerAppliedJobs(userEmailId, jobId);
	            
	            file.delete();
	            
	        } catch (IOException | AmazonServiceException ex) {
	            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + uploadFileName + "] ");
	        }
		 
		return outputMap;
	}

	@Override
	public List<GetCustomerAppliedJobs> getCustomerAppliedJobsList() {
		
		List<GetCustomerAppliedJobs> getCustomerAppliedJobsList = econnectDAO.getCustomerAppliedJobsList();
		return getCustomerAppliedJobsList;
	}

	@Override
	public Map<String, String> newUserSignInRequest(MultipartFile multipartFile, String firstName, String lastName,
			String emailId, String password, String education, String industryExperience,
			String githubLink, boolean enablePublicReadAccess) {
		
        String uploadFileName = multipartFile.getOriginalFilename();
        
        Map<String, String> outputMap = new HashMap<>();

        try {
            File file = new File(uploadFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            
            String key = emailId + "/" + uploadFileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, key, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);
            
            //logic to set the UserSignInRequest object to update the DB details
            UserSignInRequest userSignUpRequest = new UserSignInRequest();
            userSignUpRequest.setFirstName(firstName);
            userSignUpRequest.setLastName(lastName);
            userSignUpRequest.setEmailId(emailId);
            userSignUpRequest.setPassword(password);
            userSignUpRequest.setEducation(education);
            userSignUpRequest.setIndustryExperience(industryExperience);
            userSignUpRequest.setGithubLink(githubLink);
            
            //call the DB class to insert customer info 
            outputMap = econnectDAO.createUser(userSignUpRequest);
            
            //set the file name to UI purpose
            outputMap.put("fileName", uploadFileName);
            
            file.delete();
            
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + uploadFileName + "] ");
        }
		return outputMap;
    }

	@Override
	public GetUserProfileResponse getUserProfileDetails(String emailId) {

		//logic to call the DB to the user profile details
		GetUserProfileResponse getUserProfileDetails = econnectDAO.getUserProfileDetails(emailId);
		return getUserProfileDetails;
	}


	@Override
	public JobsCountResponse getJobsCount() {
		
		JobsCountResponse jobCountResponse = new JobsCountResponse();
		
		List<GetJobsResponse> getJobsResponseList = econnectDAO.getCompleteJobsList();
		
		//Internship UI Developer
		Integer iu = 0;
		//Internship Backend developer
		Integer ib = 0;
		//Internship software engineer
		Integer is = 0;
		//part time ui developer
		Integer pu = 0;
		//part time backend developer
		Integer pb = 0;
		//part time software engineer
		Integer ps = 0;
		//full time ui developer
		Integer fu = 0;
		//full time backend developer
		Integer fb = 0;
		//full time software engineer
		Integer fs = 0;
		
		for (GetJobsResponse getJobsResponse : getJobsResponseList) {
			
			if (getJobsResponse.getJobTitle() != null && getJobsResponse.getJobType() != null) {
				
			//Internship UI Developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Internship") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("UI Developer")) {
				iu++;
			}
			//Internship Backend developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Internship") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Backend Developer")) {
				ib++;
			}
			//Internship software engineer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Internship") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Software Engineer")) {
				is++;
			}
			//part time ui developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Part-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("UI Developer")) {
				pu++;
			}
			//part time backend developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Part-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Backend Developer")) {
				pb++;
			}
			//part time software engineer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Part-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Software Engineer")) {
				ps++;
			}
			//full time ui developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Full-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("UI Developer")) {
				fu++;
			}
			//full time backend developer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Full-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Backend Developer")) {
				fb++;
			}
			//full time software engineer
			if (getJobsResponse.getJobType().equalsIgnoreCase("Full-Time") && 
					getJobsResponse.getJobTitle().equalsIgnoreCase("Software Engineer")) {
				fs++;
			}
			}
		}
		jobCountResponse.setIu(iu);
		jobCountResponse.setIb(ib);
		jobCountResponse.setIs(is);
		jobCountResponse.setPu(pu);
		jobCountResponse.setPb(pb);
		jobCountResponse.setPs(ps);
		jobCountResponse.setFu(fu);
		jobCountResponse.setFb(fb);
		jobCountResponse.setFs(fs);
		return jobCountResponse;
	}

	@Override
	public Map<String, String> socialLoginUpdate(String emailId, String firstName, String lastName) {
		
		Map<String, String> outputMap = new HashMap<>();
		
		outputMap = econnectDAO.getSocialUserDetails(emailId);
		
		if (outputMap.get("dbrecord").equalsIgnoreCase("error") || outputMap.get("emailid").equals(null)) {
			outputMap = econnectDAO.createSocialLoginUser(emailId, firstName, lastName);
		}
		return outputMap;
	}

}