//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.20 at 01:50:10 PM EET 
//


package md.leonis.assistant.domain.xdxf.lousy;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fullName",
    "description",
    "abbreviations",
    "ar"
})
@XmlRootElement(name = "xdxf")
public class Xdxf {

    @XmlAttribute(name = "lang_from", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String langFrom;
    @XmlAttribute(name = "lang_to", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String langTo;
    @XmlAttribute(name = "format", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String format;
    @XmlElement(name = "full_name", required = true)
    protected String fullName;
    @XmlElement(required = true)
    protected String description;
    protected Abbreviations abbreviations;
    @XmlElement(required = true)
    protected List<Ar> ar;

    /**
     * Gets the value of the langFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLangFrom() {
        return langFrom;
    }

    /**
     * Sets the value of the langFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLangFrom(String value) {
        this.langFrom = value;
    }

    /**
     * Gets the value of the langTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLangTo() {
        return langTo;
    }

    /**
     * Sets the value of the langTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLangTo(String value) {
        this.langTo = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the fullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullName(String value) {
        this.fullName = value;
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

    /**
     * Gets the value of the ar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ar }
     * 
     * 
     */
    public List<Ar> getAr() {
        if (ar == null) {
            ar = new ArrayList<Ar>();
        }
        return this.ar;
    }

    @Override
    public String toString() {
        return "Xdxf{\n" +
                "langFrom='" + langFrom + '\'' +
                ", \nlangTo='" + langTo + '\'' +
                ", \nformat='" + format + '\'' +
                ", \nfullName='" + fullName + '\'' +
                ", \ndescription='" + description + '\'' +
                ", \nabbreviations=" + abbreviations +
                "\n}";
    }
}
