package com.techm.adms.integration;

/**
 * <Comments Here>
 * 
 * @CreatedBy TechM
 * @CreatedOn 07-Oct-2015 11:13:45 am
 */
public class Document {

    private String documentPath;
    private String documentName;
    private String documentIdentifier;
    private int documentIndex;

    /**
     * @return the documentPath
     */
    public String getDocumentPath() {
        return documentPath;
    }

    /**
     * @param documentPath
     *            the documentPath to set
     */
    public void setDocumentPath(final String documentPath) {
        this.documentPath = documentPath;
    }

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName
     *            the documentName to set
     */
    public void setDocumentName(final String documentName) {
        this.documentName = documentName;
    }

    /**
     * @return the documentIdentifier
     */
    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    /**
     * @param documentIdentifier
     *            the documentIdentifier to set
     */
    public void setDocumentIdentifier(final String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    /**
     * @return the documentIndex
     */
    public int getDocumentIndex() {
        return documentIndex;
    }

    /**
     * @param documentIndex
     *            the documentIndex to set
     */
    public void setDocumentIndex(final int documentIndex) {
        this.documentIndex = documentIndex;
    }

}
