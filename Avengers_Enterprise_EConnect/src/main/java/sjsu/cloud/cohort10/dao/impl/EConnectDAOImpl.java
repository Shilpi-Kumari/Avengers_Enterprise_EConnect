
package sjsu.cloud.cohort10.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sjsu.cloud.cohort10.dao.EConnectDAO;
import sjsu.cloud.cohort10.dto.GetCustomerAppliedJobs;
import sjsu.cloud.cohort10.dto.GetJobsResponse;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserDetailsDTO;
import sjsu.cloud.cohort10.dto.UserLoginRequest;
import sjsu.cloud.cohort10.dto.UserSignInRequest;

@Repository("mysql")
public class EConnectDAOImpl implements EConnectDAO{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, String> createUser(UserSignInRequest userRequest){
		
		HashMap<String, String> outputMap = new HashMap<>();
		
		try {
			String sql = "INSERT INTO CUSTOMER_INFO (FirstName, LastName, EmailId, Password,"
					+ "Education, IndustryExperience, GithubLink) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			jdbcTemplate.update(sql, userRequest.getFirstName(), userRequest.getLastName(), 
					userRequest.getEmailId(), userRequest.getPassword(), userRequest.getEducation(),
					userRequest.getIndustryExperience(), userRequest.getGithubLink());
			
			outputMap.put("status", "true");
			
		}catch (Exception e)
		{
			outputMap.put("status", "false");
		}
		return outputMap;
	}
	
	@Override
	public Map<String, String> getUserDetails(UserLoginRequest userLoginRequest){
		
		HashMap<String, String> outputMap = new HashMap<>();
		
		try {
			String sql = "SELECT * FROM CUSTOMER_INFO WHERE EmailId = ? AND Password = ?";
			
			UserDetailsDTO userDetailsDTO = (UserDetailsDTO) jdbcTemplate.queryForObject(
					sql, new Object[] { userLoginRequest.getEmailId(), userLoginRequest.getPassword() }, 
					new BeanPropertyRowMapper(UserDetailsDTO.class));
			
			outputMap.put("status", "true");
			outputMap.put("firstname", userDetailsDTO.getFirstName());
			outputMap.put("lastname", userDetailsDTO.getLastName());
			outputMap .put("emailid", userDetailsDTO.getEmailId());
		}catch (Exception e)
		{
			outputMap.put("status", "false");
		}
			
		return outputMap;
	}

	@Override
	public Map<String, String> adminJobsPost(JobsPostRequest jobsPostRequest) {
		
		HashMap<String, String> outputMap = new HashMap<>();
		
		try {
			String sql = "INSERT INTO CUSTOMER_INFO (JobType, JobTitle, Organization, "
					+ "Location, DueDate, JobDescription) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			
			jdbcTemplate.update(sql, jobsPostRequest.getJobType(), jobsPostRequest.getJobTitle(),
					jobsPostRequest.getOrganization(), jobsPostRequest.getOrganization(), jobsPostRequest.getDueDate(),
					jobsPostRequest.getJobDescription());
			
			outputMap.put("status", "true");
			
		}catch (Exception e)
		{
			outputMap.put("status", "false");
		}
		return outputMap;
	}

	@Override
	public List<GetJobsResponse> getJobsList(String jobType, String jobTitle) {
		
		String sql = "SELECT * FROM JOBS where JobType = ? and JobTitle = ?";
		
		List<GetJobsResponse> getJobsList = new ArrayList<GetJobsResponse>();
		
		List<java.util.Map<String, Object>> result = jdbcTemplate.queryForList(sql, jobType, jobTitle);
		
		for(java.util.Map<String, Object> obj : result)
		{
			GetJobsResponse getJobs = new GetJobsResponse();
			
			getJobs.setId((Integer)obj.get("ID"));
			getJobs.setJobType((String)obj.get("JobType"));
			getJobs.setJobTitle((String)obj.get("JobTitle"));
			getJobs.setOrganization((String)obj.get("Organization"));
			getJobs.setLocation((String)obj.get("Location"));
			getJobs.setDueDate((Timestamp)obj.get("DueDate"));
			getJobs.setJobDescription((String)obj.get("JobDescription"));
			
			getJobsList.add(getJobs);
		}
		return getJobsList;
	}

	@Override
	public Map<String, String> customerAppliedJobs(String userEmailId, String jobId) {
		
		HashMap<String, String> outputMap = new HashMap<>();
		
		try {
			String sql = "INSERT INTO JOBS_APPLIED (CustomerEmailId, JobId) "
					+ "VALUES (?, ?)";
			
			jdbcTemplate.update(sql, userEmailId, jobId);
			
			outputMap.put("status", "true");
			
		}catch (Exception e)
		{
			outputMap.put("status", "false");
		}
		return outputMap;
	}

	@Override
	public List<GetCustomerAppliedJobs> getCustomerAppliedJobsList() {
		
		String sql = "SELECT * FROM JOBS_APPLIED INNER JOIN JOBS ON JOBS_APPLIED.JobId = JOBS.ID";
		
		List<GetCustomerAppliedJobs> getCustomerAppliedJobsList = new ArrayList<GetCustomerAppliedJobs>();
		
		List<java.util.Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		
		for(java.util.Map<String, Object> obj : result)
		{
			GetCustomerAppliedJobs getCustomerAppliedJobs = new GetCustomerAppliedJobs();
			getCustomerAppliedJobs.setCustomerEmailId((String)obj.get("CustomerEmailId"));
			
			GetJobsResponse getJobs = new GetJobsResponse();
			getJobs.setId((Integer)obj.get("ID"));
			getJobs.setJobType((String)obj.get("JobType"));
			getJobs.setJobTitle((String)obj.get("JobTitle"));
			getJobs.setOrganization((String)obj.get("Organization"));
			getJobs.setLocation((String)obj.get("Location"));
			getJobs.setDueDate((Timestamp)obj.get("DueDate"));
			getJobs.setJobDescription((String)obj.get("JobDescription"));
			
			getCustomerAppliedJobs.setGetCustomerJobResponse(getJobs);
			
			getCustomerAppliedJobsList.add(getCustomerAppliedJobs);
		}
		return getCustomerAppliedJobsList;
	}

}