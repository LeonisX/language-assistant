//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.20 at 02:10:01 PM EET 
//


package md.leonis.assistant.domain.xdxf.strict;

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
    "abbrK",
    "abbrV"
})
@XmlRootElement(name = "abbr_def")
public class AbbrDef {

    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlElement(name = "abbr_k", required = true)
    protected List<AbbrK> abbrK;
    @XmlElement(name = "abbr_v", required = true)
    protected String abbrV;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the abbrK property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abbrK property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbbrK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbbrK }
     * 
     * 
     */
    public List<AbbrK> getAbbrK() {
        if (abbrK == null) {
            abbrK = new ArrayList<AbbrK>();
        }
        return this.abbrK;
    }

    /**
     * Gets the value of the abbrV property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbbrV() {
        return abbrV;
    }

    /**
     * Sets the value of the abbrV property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbbrV(String value) {
        this.abbrV = value;
    }

}
