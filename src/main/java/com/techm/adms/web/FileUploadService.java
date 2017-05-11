package com.techm.adms.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techm.adms.integration.Document;
import com.techm.adms.integration.IDocumentManager;

@Path("/uploadfile")
@RequestScoped
public class FileUploadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileUploadService.class);
	@Inject
	IDocumentManager documentManager;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document")
	public Document uploadFile(MultipartFormDataInput multiPartInput) {
		Document document = new Document();
		Map<String, List<InputPart>> uploadForm = multiPartInput
				.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		InputPart inputPart = inputParts.get(0);
		try {
			String fileName = "";
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			String[] contentDispositionHeader = headers.getFirst(
					"Content-Disposition").split(";");
			for (String name : contentDispositionHeader) {
				if (name.trim().startsWith("filename")) {
					String[] tmp = name.split("=");
					fileName = FilenameUtils.getName(tmp[1]).replace("\"", "");
				}
			}
			LOGGER.info("fileName$$$$$$$" + fileName);
			InputStream inputStream = inputPart
					.getBody(InputStream.class, null);
			LOGGER.info("documentManager$$$$$$$" + documentManager);
			document = uploadFile(inputStream, fileName);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return document;
	}

	private Document uploadFile(InputStream inputStream, String fileName)
			throws Exception {
		Document document = new Document();
		byte[] isStreamArray = null;
		try {
			isStreamArray = IOUtils.toByteArray(inputStream);
		} catch (IOException ioExcp) {
			LOGGER.error("IOException", ioExcp);
			throw new Exception("Unable to convert stream to byte array");
		}
		LOGGER.info("documentManager>>>>>" + documentManager);
		InputStream inputStreamObj = new ByteArrayInputStream(isStreamArray);
		String contentType = "application/octet-stream";
		String documentId = documentManager.saveFile(inputStreamObj,
				contentType, fileName, "/DropJars");
		document.setDocumentName(fileName);
		document.setDocumentIdentifier(documentId);

		LOGGER.info("documentId>>>>>" + documentId);
		return document;
	}
}
