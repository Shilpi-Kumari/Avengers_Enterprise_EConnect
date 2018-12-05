package sjsu.cloud.cohort10.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest; 


@Component
public class AWSSimpleEmailServiceHelper {
	
	public void recruiterEmailMessage (String firstName, String lastName, String userEmailId, 
			String education, String experience, String cloudFrontUrl, String recruiterEmailId, String jobId)  {
		

		String FROM = "sumanthravipatirr@gmail.com";

		String TO = recruiterEmailId;
		
		// The subject line for the email.
		 String SUBJECT = "E-CONNECT PORTAL: JOB APPLICATION";
		  
		  // The email body for recipients with non-HTML email clients.
		  String TEXTBODY = firstName +" "+ lastName + " has applied for the below Job: "
		  		+ "\r\n\r\n"
		  		+ "Please find the associate details below:\r\n"
		  		+ "Job ID: "+jobId+"\r\n"
		  		+ "Name: "+firstName+" "+ lastName +"\r\n"
		  				+ "Email Id: "+userEmailId+"\r\n"
		  						+ "Highest Qualification: "+education+"\r\n"
		  								+ "Industry Experience: "+experience+"\r\n"
		  										+ "Resume Link: "+cloudFrontUrl+"";
		  
		  try {
		      AmazonSimpleEmailService client = 
		          AmazonSimpleEmailServiceClientBuilder.standard()
		          // Replace US_WEST_2 with the AWS Region you're using for
		          // Amazon SES.
		            .withRegion(Regions.US_EAST_1).build();
		      SendEmailRequest request = new SendEmailRequest()
		          .withDestination(
		              new Destination().withToAddresses(TO))
		          .withMessage(new Message()
		              .withBody(new Body()
		                  .withText(new Content()
		                      .withCharset("UTF-8").withData(TEXTBODY)))
		              .withSubject(new Content()
		                  .withCharset("UTF-8").withData(SUBJECT)))
		          .withSource(FROM);
		      client.sendEmail(request);
		    } catch (Exception ex) {
		      System.out.println("The email was not sent. Error message: " 
		          + ex.getMessage());
		    }
	}
}
