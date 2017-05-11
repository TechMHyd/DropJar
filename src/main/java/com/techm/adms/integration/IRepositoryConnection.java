package com.techm.adms.integration;


import javax.enterprise.context.ApplicationScoped;
import javax.jcr.Session;


/**
 * This interface acts as a connection holder for document management system
 * 
 * @CreatedBy TechM
 * @CreatedOn 07-Oct-2015 11:14:40 am
 */
@ApplicationScoped
public interface IRepositoryConnection {

    /**
     * This method is used to create the repository connection and provide the
     * session object
     * 
     * @return
     * @throws WhaleIntegrationException
     */
    Session createSession();
}
