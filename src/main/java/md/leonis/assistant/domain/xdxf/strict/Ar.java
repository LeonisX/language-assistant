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
    "k",
    "def"
})
@XmlRootElement(name = "ar")
public class Ar {

    @XmlAttribute(name = "f")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String f;
    @XmlElement(required = true)
    protected List<K> k;
    @XmlElement(required = true)
    protected Def def;

    /**
     * Gets the value of the f property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF() {
        if (f == null) {
            return "l";
        } else {
            return f;
        }
    }

    /**
     * Sets the value of the f property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF(String value) {
        this.f = value;
    }

    /**
     * Gets the value of the k property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the k property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link K }
     * 
     * 
     */
    public List<K> getK() {
        if (k == null) {
            k = new ArrayList<K>();
        }
        return this.k;
    }

    /**
     * Gets the value of the def property.
     * 
     * @return
     *     possible object is
     *     {@link Def }
     *     
     */
    public Def getDef() {
        return def;
    }

    /**
     * Sets the value of the def property.
     * 
     * @param value
     *     allowed object is
     *     {@link Def }
     *     
     */
    public void setDef(Def value) {
        this.def = value;
    }

}
