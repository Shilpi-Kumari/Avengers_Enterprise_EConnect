package sjsu.cloud.cohort10.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class GetJobsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String jobType;
	private String jobTitle;
	private String organization;
	private String location;
	private String dueDate;
	private String jobDescription;
	private String alreadyApplied;
	
	public String getAlreadyApplied() {
		return alreadyApplied;
	}
	public void setAlreadyApplied(String alreadyApplied) {
		this.alreadyApplied = alreadyApplied;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

}
