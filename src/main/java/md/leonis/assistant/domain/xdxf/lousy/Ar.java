package md.leonis.assistant.domain.xdxf.lousy;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//TODO identity as RAW, get k, tr, other interpret as text formatting
//ar->  p or div
//co -> <i>
//c -> <span "color"
//dtrn -> something else
//TODO detect unknown tags
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "k", "tr", "dtrn", "value"
})
@XmlRootElement(name = "ar")
public class Ar {

    @XmlAttribute(name = "f")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String f;

    @XmlElement //TODO investigate all dictionaries if only one
    protected String k;

    @XmlElement //TODO list, investigate all dictionaries if only one
    protected String tr;

    //TODO any tag
    @XmlJavaTypeAdapter(RawTextXmlAdapter.class)
    @XmlAnyElement
    protected List<String> dtrn;

    /*@XmlJavaTypeAdapter(RawTextXmlAdapter.class)
    @XmlElement
    protected String co;*/

    @XmlMixed
    protected List<String> value;

    public String getF() {
        return f;
    }

    public void setF(String value) {
        this.f = value;
    }

    public String getValue() {
        return String.join("", value).trim();
    }

    //TODO smart formatter
    // <ar><k>асфальт</k>
    // <co><c>асфа́льт</c></co>
    // <dtrn>позднее заимств. через франц. asphalte или нем. Asphalt из лат. asphaltus, греч. ἄσφαλτος от σφάλλεσθαι &quot;опрокинуться, упасть&quot;; связующее вещество, предохраняющее стены от падения; см. Дильс, KZ 47, 207 и сл.; Кречмер, Glotta 10, 237.</dtrn>
    // <dtrn>••</dtrn>
    // <dtrn><i><co>[Ср. уже др.-русск. асфальтъ из греч. ἀσφάλτης (Иосиф Флав.; Мещерский, Виз. Врем., 13, 1958, стр. 251). - Т.]</co></i></dtrn></ar>
    public String getFullValue() {
        return Stream.of(value.stream(), /*Stream.of(co),*/ getDtrn().stream()).filter(Objects::nonNull).flatMap(s -> s)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public List<String> getDtrn() {
        if (dtrn == null) {
            return new ArrayList<>();
        }
        return dtrn;
    }

    public void setDtrn(List<String> dtrn) {
        this.dtrn = dtrn;
    }

    /*public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }*/
}
