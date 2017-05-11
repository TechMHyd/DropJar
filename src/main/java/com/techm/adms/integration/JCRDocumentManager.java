package com.techm.adms.integration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Binary;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author TechM
 * 
 * @CreatedBy TechM
 * @CreatedOn 07-Oct-2015 11:15:42 am
 */
public class JCRDocumentManager implements IDocumentManager {

    private static final String NODE_TYPE_FILE = "nt:file";
    private static final String JCR_CONTENT = "jcr:content";
    private static final String JCR_DATA = "jcr:data";
    private static final String JCR_MIME_TYPE = "jcr:mimeType";
    private static final String NODE_TYPE_RESOURCE = "nt:resource";

    private static final String SEARCH_QUERY = "select * from [nt:base] as b where name(b) = ";

    private static final Logger LOGGER = LoggerFactory.getLogger(JCRDocumentManager.class);

    @Inject
    IRepositoryConnection repositoryConnection;

    /**
     * This method used to save the input stream in the repository
     * 
     * @param stream
     * @param contentType
     * @param fileName
     * @param parentNodePath
     * @return String
     * @throws Exception
     */
    @Override
    public String saveFile(final InputStream stream, final String contentType, final String fileName, final String parentNodePath) {
        String uuId = null;
        Session session = repositoryConnection.createSession();
        Node parentNode = null;
        try {
            if (parentNodePath == null) {
                LOGGER.debug("parentNodePath is Null");
                parentNode = session.getRootNode();
            } else {
                parentNode = session.getNode(parentNodePath);
            }
            Node dataFile = parentNode.addNode(fileName, NODE_TYPE_FILE);
            Node content = dataFile.addNode(JCR_CONTENT, NODE_TYPE_RESOURCE);
            Binary binary = session.getValueFactory().createBinary(stream);
            content.setProperty(JCR_DATA, binary);
            content.setProperty(JCR_MIME_TYPE, contentType);
            session.save();
            uuId = dataFile.getIdentifier();
            LOGGER.debug("File added Successfully..!!");

        } catch (ItemExistsException | PathNotFoundException | NoSuchNodeTypeException exception) {
            ////throw new Exception(exception);
        } catch (LockException | VersionException | ConstraintViolationException exception) {
            ////throw new Exception(exception);
        } catch (RepositoryException exception) {
            ////throw new Exception(exception);
        } finally {
            session.logout();
        }
        return uuId;
    }

    /**
     * This method is used to get the file content based on uuId
     * 
     * @param uuId
     * @return InputStream
     * @throws Exception
     */
    @Override
    public InputStream getFileById(final String uuId) {
        InputStream ins = null;
        Session session = repositoryConnection.createSession();
        try {
            Node actualNode = session.getNodeByIdentifier(uuId);
            Node contentNode = actualNode.getNode(JCR_CONTENT);
            ins = contentNode.getProperty(JCR_DATA).getBinary().getStream();
        } catch (ItemNotFoundException itemNotFoundException) {
            //throw new Exception(itemNotFoundException);
        } catch (PathNotFoundException pathNotFoundException) {
            //throw new Exception(pathNotFoundException);
        } catch (ValueFormatException valueFormatException) {
            //throw new Exception(valueFormatException);
        } catch (RepositoryException repositoryException) {
            //throw new Exception(repositoryException);
        } finally {
            session.logout();
        }
        return ins;
    }

    /**
     * This method is used to get the node path based on uuId
     * 
     * @param uuId
     * @return String
     * @throws Exception
     */
    @Override
    public String getPath(final String uuId) {
        String nodePath = null;
        Session session = repositoryConnection.createSession();
        try {
            Node actualNode = session.getNodeByIdentifier(uuId);
            nodePath = actualNode.getPath();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Node path::" + actualNode.getPath());
            }
        } catch (ItemNotFoundException itemNotFoundException) {
            //throw new Exception(itemNotFoundException);
        } catch (RepositoryException repositoryException) {
            //throw new Exception(repositoryException);
        } finally {
            session.logout();
        }
        return nodePath;
    }

    /**
     * This method is used to get the node name based on uuId
     * 
     * @param uuId
     * @return String
     * @throws Exception
     */
    @Override
    public String getFileName(final String uuId) {
        String nodeName = null;
        Session session = repositoryConnection.createSession();
        try {
            Node actualNode = session.getNodeByIdentifier(uuId);
            nodeName = actualNode.getName();
            LOGGER.debug("Parent Node Name::" + actualNode.getParent().getPath());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Node Name::" + actualNode.getName());
            }
        } catch (ItemNotFoundException itemNotFoundException) {
            //throw new Exception(itemNotFoundException);
        } catch (RepositoryException repositoryException) {
            //throw new Exception(repositoryException);
        } finally {
            session.logout();
        }
        return nodeName;
    }

    /**
     * This method is used to search the files based on a query
     * 
     * @param nodeName
     * @return List of Document
     * @throws Exception
     */
    @Override
    public List<Document> searchFiles(final String nodeName) {

        if (null == nodeName) {
            //throw new Exception("NullPointerException: Node name is null ");
        }
        List<Document> documents = new ArrayList<Document>();
        Session session = repositoryConnection.createSession();
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            StringBuilder queryBuilder = new StringBuilder(SEARCH_QUERY);
            queryBuilder.append("'").append(nodeName).append("'");
            Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);
            QueryResult result = query.execute();
            NodeIterator nodes = result.getNodes();
            if (nodes != null) {
                Document document = null;
                Node node = null;
                while (nodes.hasNext()) {
                    node = nodes.nextNode();
                    document = getDocObj();
                    document.setDocumentPath(node.getPath());
                    document.setDocumentName(node.getName());
                    document.setDocumentIdentifier(node.getIdentifier());
                    document.setDocumentIndex(node.getIndex());
                    documents.add(document);
                }
            }
        } catch (InvalidQueryException invalidQueryException) {
            //throw new Exception(invalidQueryException);
        } catch (RepositoryException repositoryException) {
            //throw new Exception(repositoryException);
        } finally {
            session.logout();
        }
        return documents;
    }

    private Document getDocObj() {
        return new Document();
    }

    /**
     * This method is used to remove the node from the repository based on uuId
     * 
     * @param uuId
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean deleteFile(final String uuId) {
        boolean isFileDeleted = false;
        Session session = repositoryConnection.createSession();
        try {
            Node actualNode = session.getNodeByIdentifier(uuId);
            actualNode.remove();
            session.save();
            isFileDeleted = true;
        } catch (RepositoryException repositoryException) {
            //throw new Exception("Invalid id: File not available in the repository", repositoryException);
        } finally {
            session.logout();
        }
        return isFileDeleted;
    }

    /**
     * This method is used to check whether a node exists
     * 
     * @param nodePath
     * @return boolean
     * @throws Exception
     */
    //@Override
    public boolean nodeExists(String nodePath) {
        Session session = repositoryConnection.createSession();
        boolean nodeExist = false;
        try {
            nodeExist = session.itemExists(nodePath);
        } catch (RepositoryException repositoryException) {
            //throw new Exception(repositoryException);
        } finally {
            session.logout();
        }
        return nodeExist;
    }

    /**
     * This method is used to create a folder under a particular node and will
     * return the GId
     * 
     * @param folderName
     * @param parentNodePath
     * @return Node Object
     */
    @Override
    public Node createFolder(String folderName, String parentNodePath) {
        Node folderNode = null;
        Session session = repositoryConnection.createSession();
        Node parentNode = null;
        try {
            if (null == parentNodePath) {
                parentNode = session.getRootNode();
            } else {
                parentNode = session.getNode(parentNodePath);
            }
            folderNode = parentNode.addNode(folderName);
            session.save();
        } catch (ItemExistsException | PathNotFoundException exception) {
            ////throw new Exception(exception);
        } catch (VersionException | ConstraintViolationException | LockException exception) {
            ////throw new Exception(exception);
        } catch (RepositoryException exception) {
            ////throw new Exception(exception);
        } finally {
            session.logout();
        }
        return folderNode;
    }
}
