//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.20 at 02:10:01 PM EET 
//


package md.leonis.assistant.domain.xdxf.strict;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "fullTitle",
    "description",
    "publisher",
    "authors",
    "fileVer",
    "creationDate",
    "lastEditedDate",
    "dictEdition",
    "publishingDate",
    "dictSrcUrl",
    "abbreviations"
})
@XmlRootElement(name = "meta_info")
public class MetaInfo {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(name = "full_title", required = true)
    protected String fullTitle;
    @XmlElement(required = true)
    protected String description;
    protected String publisher;
    protected Authors authors;
    @XmlElement(name = "file_ver", required = true)
    protected String fileVer;
    @XmlElement(name = "creation_date", required = true)
    protected String creationDate;
    @XmlElement(name = "last_edited_date", required = true)
    protected String lastEditedDate;
    @XmlElement(name = "dict_edition")
    protected String dictEdition;
    @XmlElement(name = "publishing_date")
    protected String publishingDate;
    @XmlElement(name = "dict_src_url")
    protected String dictSrcUrl;
    protected Abbreviations abbreviations;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the fullTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullTitle() {
        return fullTitle;
    }

    /**
     * Sets the value of the fullTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullTitle(String value) {
        this.fullTitle = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the publisher property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the authors property.
     * 
     * @return
     *     possible object is
     *     {@link Authors }
     *     
     */
    public Authors getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authors }
     *     
     */
    public void setAuthors(Authors value) {
        this.authors = value;
    }

    /**
     * Gets the value of the fileVer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileVer() {
        return fileVer;
    }

    /**
     * Sets the value of the fileVer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileVer(String value) {
        this.fileVer = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationDate(String value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the lastEditedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastEditedDate() {
        return lastEditedDate;
    }

    /**
     * Sets the value of the lastEditedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastEditedDate(String value) {
        this.lastEditedDate = value;
    }

    /**
     * Gets the value of the dictEdition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDictEdition() {
        return dictEdition;
    }

    /**
     * Sets the value of the dictEdition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDictEdition(String value) {
        this.dictEdition = value;
    }

    /**
     * Gets the value of the publishingDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublishingDate() {
        return publishingDate;
    }

    /**
     * Sets the value of the publishingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublishingDate(String value) {
        this.publishingDate = value;
    }

    /**
     * Gets the value of the dictSrcUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDictSrcUrl() {
        return dictSrcUrl;
    }

    /**
     * Sets the value of the dictSrcUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDictSrcUrl(String value) {
        this.dictSrcUrl = value;
    }

    /**
     * Gets the value of the abbreviations property.
     * 
     * @return
     *     possible object is
     *     {@link Abbreviations }
     *     
     */
    public Abbreviations getAbbreviations() {
        return abbreviations;
    }

    /**
     * Sets the value of the abbreviations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Abbreviations }
     *     
     */
    public void setAbbreviations(Abbreviations value) {
        this.abbreviations = value;
    }

}
