package md.leonis.assistant.source.dsl.parser;

import org.apache.commons.lang3.StringUtils;

public class Preprocessor {

    public static String normalize(String string) {
        string = string.replace(",", ", ");
        string = string.replace(";", "; ");
        string = string.replace("] ", "]");
        string = string.replace(" [/", "[/");
        string = string.replace(" )", ")");
        string = string.replace("( ", "(");
        string = string.replace("][", "] [");
        string = string.replace(",[", ", [");
        string = string.replace(", [/", ",[/");
        string = string.replace("<<", " <<");
        string = string.replace(">>", ">> ");
        string = string.replace("  ", " ");
        string = string.replace("  ", " ");
        string = string.replace("1,[/c]", "1[/c],");
        string = StringUtils.stripEnd(string, " ");
        return string;
    }
}
