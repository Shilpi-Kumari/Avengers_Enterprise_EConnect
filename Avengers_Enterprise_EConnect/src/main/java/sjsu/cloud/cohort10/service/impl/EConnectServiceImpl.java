package sjsu.cloud.cohort10.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.Region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import sjsu.cloud.cohort10.dao.EConnectDAO;
import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.GetUserProfileResponse;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserLoginRequest;
import sjsu.cloud.cohort10.dto.UserSignInRequest;
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
	public List<GetJobsResponse> getJobsList(String jobType, String jobTitle) {
		
		//logic to get the jobs from DB
		List<GetJobsResponse> getJobsList = econnectDAO.getJobsList(jobType, jobTitle);
		return getJobsList;
	}

	@Override
	public Map<String, String> customerAppliedJobs(String userEmailId, String jobId) {

		//logic to post the jobs in DB by the admin
		Map<String, String> outputMap = econnectDAO.customerAppliedJobs(userEmailId, jobId);
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

}