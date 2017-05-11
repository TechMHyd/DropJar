package com.techm.adms.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techm.adms.integration.Document;
import com.techm.adms.integration.IDocumentManager;

@Path("/download")
@RequestScoped
public class FileDownloadService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileDownloadService.class);
	@Inject
	IDocumentManager documentManager;
	
	@GET
    @Produces( MediaType.APPLICATION_OCTET_STREAM)
    @Path("/documentFile/{documentId}")
    public Response fileDownload(@PathParam("documentId") final String documentId) {
        Response response = null;
        try {
        	LOGGER.info("documentId$$$$$$$"+documentId);

        	InputStream inputStream = documentManager.getFileById(documentId);

        	LOGGER.info("inputStream$$$$$$$"+inputStream);
        	ResponseBuilder responseBuilder = Response.ok((Object) inputStream);
            responseBuilder.header("Content-Disposition", "attachment; filename="+ documentManager.getFileName(documentId));
            response = responseBuilder.build();
            
        } catch (Exception e) {
        	LOGGER.error("Unable to get download pack", e);
        } 
        return response;
    }
	
	@GET
    @Produces( MediaType.APPLICATION_JSON)
    @Path("/getList")
    public List<Document> getList(@QueryParam("nodeName") final String nodeName) {
		List<Document> documents = new ArrayList<Document>(); 
        try {
        	LOGGER.info("nodeName$$$$$$$"+nodeName);
        	LOGGER.info("nodeName$$$$$$$"+nodeName);
        	documents = documentManager.searchFiles(nodeName);
        	LOGGER.info("documents$$$$$$$"+documents);
        	if(documents != null && documents.size() >0){
        		LOGGER.info("documents$$$$$$$"+documents.size());
        		for(Document document : documents){
        			LOGGER.info("document>>>>>>>>>"+document);
        		}
        	}
        	
        } catch (Exception e) {
        	LOGGER.error("Unable to get download pack", e);
        } 
        return documents;
    }
	
	@GET
    @Path("/createFolder/{folderName}")
    public void createFolder(@PathParam("folderName") final String folderName) {
	    try {
        	LOGGER.info("nodeName$$$$$$$"+folderName);
        	Node node = documentManager.createFolder(folderName, null);
        	LOGGER.info("nodeName$$$$$$$"+node.getName());
        	
        } catch (Exception e) {
        	LOGGER.error("Unable to get download pack", e);
        } 
    }

}
