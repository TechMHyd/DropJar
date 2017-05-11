/**
 * 
 */
package com.techm.adms.integration;

import java.net.MalformedURLException;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * Implementation class to get the repository connection
 * 
 * @CreatedBy TechM
 * @CreatedOn 07-Oct-2015 11:15:09 am
 */
public class JCRConnection implements IRepositoryConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(JCRConnection.class);

    /**
     * This method is used to create the repository connection and provide the
     * session object
     * 
     * @return Session Object
     * @throws WhaleIntegrationException
     */
    @Override
    public Session createSession() {
        Session session = null;
        try {
            Repository repository = getRepositoryConnection("http://172.27.23.101:58080/jackrabbit-webapp-2.10.1/rmi");
            session = repository.login(new SimpleCredentials("admin","admin".toCharArray()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Repository Session [ " + session.getUserID() + " ]");
            }
        } catch (RepositoryException repositoryException) {
            //throw new RuntimeException(repositoryException);
        }
        return session;
    }

    /**
     * This method is used to create the repository connection and provide the
     * repository object
     * 
     * @return Repository Object
     * @throws WhaleIntegrationException
     */
    private Repository getRepositoryConnection(final String serverUrl) {
        Repository repository = null;
        try {
            repository = new URLRemoteRepository(serverUrl);
        } catch (MalformedURLException mue) {
           // throw new RuntimeException(mue);
        }

        return repository;
    }
}
