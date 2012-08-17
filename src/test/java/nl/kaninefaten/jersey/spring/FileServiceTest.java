package nl.kaninefaten.jersey.spring;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class FileServiceTest extends JerseyTest {

	public FileServiceTest() throws Exception {
		super(
				new WebAppDescriptor.Builder(
						"com.sun.jersey.samples.springannotations.resources.jerseymanaged")
						.contextPath("spring")
						.contextParam("contextConfigLocation",
								"classpath:/nl/kaninefaten/jersey/spring/applicationContext.xml")
						.servletClass(SpringServlet.class)
						.contextListenerClass(ContextLoaderListener.class)
						.build());
	}

	@Test
	public void doTestApplicationWadl() {
		WebResource webResource = resource();

		String wadl = webResource.path("application.wadl")
				.accept(MediaTypes.WADL).get(String.class);

		assertTrue(
				"Method: doTestApplicationWadl \nMessage: Something wrong, the returned "
						+ "WADL's length is not > 0", wadl.length() > 0);

	}
	
	
	@Test
	public void doTestHelloString() {

		WebResource webResource = resource();
		String returnedString = null;
		try{
			returnedString = webResource.path("/sample/file")
				.accept(MediaType.TEXT_PLAIN).get(String.class);
		}catch (Exception e){
			fail(e.getMessage());
		}
		assert(returnedString.startsWith("Hello"));;

	}
	
	@Test
	public void doTestUploadFile() {

		WebResource webResource = resource();

		File  testFile= new File("./src/test/resources/Test.doc");
		File returnedFile = new File("./src/test/test-fetched.doc");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(testFile
					);

			String parameterValue = "iets"  + System.currentTimeMillis();
			
			FormDataMultiPart part = new FormDataMultiPart().field("file",
					inputStream, MediaType.MULTIPART_FORM_DATA_TYPE).field("p",
							parameterValue);
			
			String response = webResource.path("/sample/upload")
					.type(MediaType.MULTIPART_FORM_DATA)
					.post(String.class, part);
			assertTrue(response.contains(parameterValue));
			
			assertTrue(testFile.length() == returnedFile.length());
			return;
		} catch (Throwable t) {
			try {
				inputStream.close();
			} catch (IOException e) {
				fail(e.toString());
			}
			fail(t.toString());
		}
		assertTrue(true);
	}

// Some what helpful when uploading files.	
//	 {
//			FormDataMultiPart form = new FormDataMultiPart();
//	        File file = new File("/tmp/1.txt");
//	        form.field("username", "ljy");
//	        form.field("password", "password");
//	        form.field("filename", file.getName());
//	        form.bodyPart(new FileDataBodyPart("file", file, MediaType.MULTIPART_FORM_DATA_TYPE));
//	        
//	        String response = webResource.path("/sample/file/upload").type(MediaType.MULTIPART_FORM_DATA).post(String.class, form);
//	    	
//	        System.out.println(response);
//	 }

	
}
