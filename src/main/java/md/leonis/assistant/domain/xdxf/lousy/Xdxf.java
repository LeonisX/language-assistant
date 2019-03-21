package md.leonis.assistant.domain.xdxf.lousy;

import md.leonis.assistant.domain.standard.Dictionary;

import java.io.File;
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

    public List<Ar> getAr() {
        if (ar == null) {
            ar = new ArrayList<Ar>();
        }
        return this.ar;
    }

    public Dictionary toDictionary(File file) {
        return new Dictionary(
                null,
                langFrom,
                langTo,
                format,
                null, // revision
                fullName,
                file.length(),
                getAr().size(),
                file.getAbsolutePath()
        );
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
