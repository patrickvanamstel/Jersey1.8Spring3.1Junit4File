package nl.kaninefaten.jersey.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.multipart.FormDataParam;


@Component
@Path("/sample")
public class FileService {

	@Autowired
	@Qualifier(value = "SpringSampleBeanImpl")
	private SpringSampleBean springSampleBean;

	@GET
	@Path("/file")
	public Response hello() {
		return Response
				.status(Response.Status.OK.getStatusCode())
				.entity("HelloworldService"
						+ springSampleBean.getClass().getName()).build();
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("p") String p) {

		File outputFile = new File("./src/test/test-fetched.doc");
		// save it
		try {
			writeToFile(uploadedInputStream, outputFile);
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(" message " + e.getMessage()).build();
		}

		String output = "File uploaded to : " + outputFile.getAbsolutePath()
				+ " : extra parameter " + p;

		return Response.status(200).entity(output).build();

	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			File uploadedFileLocation) throws IOException {

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(uploadedFileLocation);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			throw (e);
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}
}
