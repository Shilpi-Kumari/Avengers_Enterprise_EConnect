package sjsu.cloud.cohort10.test.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import sjsu.cloud.cohort10.AbstractClass;
import sjsu.cloud.cohort10.dto.JobsPostRequest;
import sjsu.cloud.cohort10.dto.UserLoginRequest;

public class EConnectControllerTest extends AbstractClass {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testGetSocialEmailId() throws Exception {
		String uri = "/getSocialEmailId?accessToken=sample";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testuserLogin() throws Exception {
		String uri = "/userLogin";
		
		UserLoginRequest userLoginRequest = new UserLoginRequest();
		
		userLoginRequest.setEmailId("sunny");
		userLoginRequest.setPassword("sunny");
		
		String inputJson = super.mapToJson(userLoginRequest);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(inputJson)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testAdminJobsPost() throws Exception {
		String uri = "/adminJobsPost";
		
		JobsPostRequest jobsPostRequest = new JobsPostRequest();
		jobsPostRequest.setJobType("Intern");
		jobsPostRequest.setJobTitle("Developer");
		jobsPostRequest.setOrganization("Google");
		jobsPostRequest.setLocation("US");
		jobsPostRequest.setDueDate("07-13-2019");
		jobsPostRequest.setJobDescription("Intern Role");
		jobsPostRequest.setRecruiterEmail("sample@gmail.com");
		
		String inputJson = super.mapToJson(jobsPostRequest);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(inputJson)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testGetJobsList() throws Exception {
		String uri = "/getJobsList?jobType=Intern&jobTitle=Developer&emailId=sample@gmail.com";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testGetCustomerAppliedJobsList() throws Exception {
		String uri = "/getCustomerAppliedJobsList";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testGetUserProfileDetails() throws Exception {
		String uri = "/getUserProfileDetails?emailId=sample@gmail.com";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testGetJobsCount() throws Exception {
		String uri = "/getJobsCount";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
	
	@Test
	public void testSocialLoginUpdate() throws Exception {
		String uri = "/socialLoginUpdate?emailId=sample@gmail.com&firstName=sample&lastName=sample";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)).andReturn();
		int statusCode = mvcResult.getResponse().getStatus();
		assertEquals(statusCode, 200);
	}
}

