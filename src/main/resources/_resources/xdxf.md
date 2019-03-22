# XDXF file format

XDXF stands for XML Dictionary Exchange Format, and specifies a semantic format for storing dictionaries.

## Links

https://github.com/soshial/xdxf_makedict
https://github.com/soshial/xdxf_makedict/blob/master/format_standard/xdxf_description.md
https://github.com/soshial/xdxf_makedict/blob/master/format_standard/xdxf_strict.dtd


### XDXF

https://github.com/soshial/xdxf_makedict
https://github.com/kysko/xdxf-dictionary
https://github.com/hlavki/xdxf-parser

http://www.dicto.org.ru/xdxf.html
https://sourceforge.net/projects/xdxf/files/

https://ru.wikipedia.org/wiki/XDXF

Atlantida Multilingual Dictionary + Source + Sound

https://web.archive.org/web/20061207035813/http://atla.revdanica.com/ru/
https://sourceforge.net/projects/atla/files/

fluentizer.com
https://www.apkandroid.ru/fluent-english-pro-old/fluentizer.narr/


### DSL, Mova, PtkDict/phpMyLingvo MySQL dumps, Sdictionary
JaLingo

http://jalingo.sourceforge.net/
http://jalingo.sourceforge.net/dictionaries/
https://sourceforge.net/projects/jalingo/files/JaLingo/0.6.0/

### Other

???
https://code.google.com/archive/p/prs-plus/source/dictionary/source


### StarDict

https://code.google.com/archive/p/stardict-3/downloads
http://download.huzheng.org/
https://archive.org/details/stardict_collections


### ABBYY Lingo

Freedict
https://freedict.org/downloads/
https://github.com/freedict/fd-dictionaries/
https://github.com/freedict/fd-dictionaries/wiki/Dictionary-Clients


https://4pda.ru/forum/index.php?showtopic=189915&st=360#entry7315024
https://4pda.ru/forum/index.php?showtopic=189915&st=620#entry8942509


http://mueller-dic.chat.ru/Mueller24_koi.html


https://sourceforge.net/projects/freedict/

## DTD to Java Classes

We will use the utility `xcj` from `JAXB`

Unpack `/files/xdxf/jaxb-ri-2.3.0.zip`

`~/jaxb-ri/bin$ xjc -dtd xdxf_lousy.dtd`

`~/jaxb-ri/bin$ xjc -dtd xdxf_strict.dtd`

`~/jaxb-ri/bin$ xjc -dtd -p md.leonis.assistant.domain.xdxf.lousy xdxf_lousy.dtd`

This examples places the generated Java classes in a subdirectory called `md.leonis.assistant.domain.xdxf.lousy` and creates subdirectories `lousy` so that these generated classes are part of `lousy` package.

* [JAXB home page](https://javaee.github.io/jaxb-v2/)
* [Running the binding compiler (XJC)](https://javaee.github.io/jaxb-v2/doc/user-guide/ch04.html#tools-xjc-ant-task)
* [JAXB/xjc Java Generation with DTD](https://www.javaworld.com/article/2074057/jaxb-xjc-java-generation-with-dtd.html)