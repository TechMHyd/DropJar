/**
 * 
 */
package com.techm.adms.integration;

import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.jcr.Node;




/**
 * This interface contain methods that perform basic content management
 * operations
 * 
 * @CreatedBy TechM
 * @CreatedOn 07-Oct-2015 11:14:16 am
 */
@ApplicationScoped
public interface IDocumentManager {

    /**
     * This method is used to save the input stream in the repository
     * 
     * @param stream
     * @param contentType
     * @param fileName
     * @param parentNodePath
     * @return String
     * @throws WhaleIntegrationException
     */
    String saveFile(final InputStream stream, final String contentType, final String fileName, final String parentNodePath);

    /**
     * This method is used to get the file content based on uuId
     * 
     * @param uuId
     * @return InputStream
     * @throws WhaleIntegrationException
     */
    InputStream getFileById(final String uuId);

    /**
     * This method is used to get the node path based on uuId
     * 
     * @param uuId
     * @return String
     * @throws WhaleIntegrationException
     */
    String getPath(final String uuId);

    /**
     * This method is used to get the node name based on uuId
     * 
     * @param uuId
     * @return String
     * @throws WhaleIntegrationException
     */
    String getFileName(final String uuId);

    /**
     * This method is used to search the files based on a query
     * 
     * @param nodeName
     * @return List of Document
     * @throws WhaleIntegrationException
     */
    List<Document> searchFiles(final String nodeName);

    /**
     * This method is used to remove the node from the repository based on uuId
     * 
     * @param uuId
     * @return boolean
     * @throws WhaleIntegrationException
     */
    boolean deleteFile(final String uuId);

    /**
     * This method is used to check whether a node exists
     * 
     * @param nodePath
     * @return boolean
     * @throws WhaleIntegrationException
     */
    boolean nodeExists(final String nodePath);

    /**
     * This method is used to create a folder under a particular node and will
     * return the GId
     * 
     * @param folderName
     * @param parentNodePath
     * @return Node Object
     */
    Node createFolder(final String folderName, String parentNodePath);

}
