### Language Assistant is your personal assistant in learning foreign languages.

#### Language Assistant - твой персональный помощник в изучении иностранных языков.  

This is a model of the project, designed to simplify the study of foreign languages. For now, just a sketch of the integration of Spring Boot 2 and JavaFX 8.

### TODO list:

https://mvnrepository.com/artifact/org.apache.commons

Слова для заучивания и словарный запас - добавить колонок

*** Memorization algorithm (simplified ANKI):

watchscript - Mark as Known
update current view

v Ask to recall x meanings of word
v Increase meanings if need
LearnWordMeaningsController

TODO how to know number of meanings for each word??? En_Ru_Muller_(18Mb).7z, [m3] or Google
http://wiki.fu-lab.ru/index.php/DSL#.D0.98.D1.81.D0.BF.D0.BE.D0.BB.D1.8C.D0.B7.D0.BE.D0.B2.D0.B0.D0.BD.D0.B8.D0.B5_.D1.81.D0.BE.D0.B7.D0.B4.D0.B0.D0.BD.D0.BD.D1.8B.D1.85_.D1.81.D0.BB.D0.BE.D0.B2.D0.B0.D1.80.D0.B5.D0.B9_.D0.B2_Abbyy_Lingvo
http://lingvo.helpmax.net/ru/%d0%b2%d0%be%d0%bf%d1%80%d0%be%d1%81%d1%8b-%d0%b8-%d0%b7%d0%b0%d1%82%d1%80%d1%83%d0%b4%d0%bd%d0%b5%d0%bd%d0%b8%d1%8f/dsl-compiler/%d0%ba%d0%be%d0%bc%d0%b0%d0%bd%d0%b4%d1%8b-dsl/
https://lingvoboard.ru/store/html/DSLReference_HTML/dict.html
LingvoUniversalEnRu


TODO work with archives, unpack all sources directly

DSL - prepare all classes, domains, ...


TODO implement parser as state machine

https://habr.com/ru/post/60342/
https://github.com/Stepets/akerfsm

https://github.com/davidmoten/state-machine


TODO refactor AkerFSM + tests

Split by words. Starts from "". not from "/t"
[m1]: main block
[trn]
[m2][c brown]: part of speech
[m2][ex][c teal]: examples
[m3][c saddlebrown]: meanings

trim ['], [/'], [lang id=1033], [/lang]

[m1]:
[c lightslategray]{{t}}\[ded\]{{/t}}[/c]
[p]a[/p]

[m2]
[c brown]1.[/c]
[p]a[/p]

Вытащить:
- название
- транскрипция
[
- аббревиатура (verb)
- переводы
- примеры
]



TODO get scripts from YouTube || allow to upload them
save
use

TODO 100% get all meanings from google translate

one base with places (???), frequency (google), level (gse), ...
calculate place, level for unknown words/phrases
mark them if approximate
frequencies for phrases (google)

v UserWordBank - also store wordLevel
v Refactor RepeatWordsController.
v Use filter by level
v Show all/filtered runtime
v 20 - global value

ShowCards:
v Refactor
v Center buttons
v TODO count


Copy to LearnWordsController

* v LevelsTemplate::todo attach, test + Controller + Object for selected levels
* RepeatWordsController - beta v2
* LearnWordsController

TODO learn idea from VocabHunter

Идея такая:
- v UserWordBank - единственная база.
- v Добавить статус слова (enum)
- v Шаблон показа карточек
- v Два окна: повторять или учить новые. В первом случае фильтр
- v Ограничение 20


**ANKI**
//TODO import cards and learn words
Vanilla SQLite + files
https://github.com/ankidroid/Anki-Android/wiki/Database-Structure
https://decks.fandom.com/wiki/Anki_APKG_format_documentation
https://github.com/dae/anki
https://play.google.com/store/apps/details?id=io.lingvist.android&hl=ru


https://controlsfx.bitbucket.io/
http://fxexperience.com/
https://vocabhunter.github.io/

#### Study

https://www.youtube.com/watch?time_continue=68&v=UHGWre0Nq4g
https://ru.wikipedia.org/wiki/Mnemosyne
https://github.com/helloworld1/AnyMemo
https://4pda.ru/forum/index.php?showtopic=182901
https://www.supermemo.com/en/apps




TODO Mueller24 is truncated. Either 7, or import from En_Ru_Muller_(18Mb).7z
Other alternatives: Fora dictionaries, but need import



Hunspell - best solution for search word worms
https://github.com/LibreOffice/dictionaries
https://extensions.libreoffice.org/extensions/english-dictionaries
https://extensions.libreoffice.org/extensions/russian-dictionary-pack


## Scratch

### Done

* Splash screen (mock-up)
* Dashboard screen (mock-up)
* WordBank screen (mock-up, w/o filters)
* VideoList screen (mock-up)
* WatchScript screen (mock-up)
* WatchVideo screen (mock-up)
* Dictionary screen (mock-up)

### TODO

Lemma Importer. Problem writing: 

	write	Verb	%	400	100	0.96
	@	@	write	109	100	0.95
	@	@	writes	26	99	0.89
	@	@	writing	63	100	0.95
	@	@	written	103	100	0.96
	@	@	wrote	99	100	0.90
	writing	NoC	%	64	100	0.92
	@	@	writing	53	100	0.92
	@	@	writings	11	89	0.89

//TODO in the future
Actually we need another approach -> convert normal words to variants.
For clean results, need to study again
https://www.english.com/gse/teacher-toolkit/user/vocabulary?page=518&sort=vocabulary;asc&gseRange=10;90&audience=GL
(has grammatical categories)

//TODO need to identify and translate phrases


Need ID

TODO precache/cache translations in WatchScriptController


### TODO video

* v Allow to see different dictionaries, change by ComboBox

* WatchScript: Tab with table & words frequency & level & sort (as in WordBank)

* Templates to separate folder

* v Users WordBank (randomly data for now)
* v WatchScript: connect to it

https://github.com/akuznetsov/russianmorphology
https://mvnrepository.com/artifact/org.apache.lucene.morphology

TODO sourcerer - 

git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch PATH-TO-YOUR-FILE-WITH-SENSITIVE-DATA' --prune-empty --tag-name-filter cat -- --all

git push origin --force --all

TODO sourcerer - followers, following 2/2 -> 0/0 :(


### TODO DBs

* v 2 DataSources, 2 files
* v Grab site
* v Raw to WL, analyze

* v Word::frequency
* v Word::place

* v Word::level
* Match levels & wordbanks from different sources

* Video - find the most matched videos (level, unknown words)


* v English::Russian

* v Dictionary importer


#TODO bugs

Shut down sound from browser when window is closed


### Online translators:

Google
https://www.thefreedictionary.com/appetitively
Many others. Investigate lingualeo google chrome plugin


LeoLingo

https://dic.1963.ru/

https://www.readbeyond.it/aeneas/

https://stackoverflow.com/questions/46598539/how-to-synchronize-mp3-audio-with-text
https://stackoverflow.com/questions/40206029/android-sync-audio-with-text
https://stackoverflow.com/questions/13422673/looking-for-a-working-example-of-addtimedtextsource-for-adding-subtitle-to-a-vid
https://en.wikipedia.org/wiki/LRC_(file_format)

https://github.com/synalp/jtrans

https://stackoverflow.com/questions/6970013/getting-current-youtube-video-time

https://github.com/IonicaBizau/text-to-speech

https://github.com/pettarin/forced-alignment-tools

https://sourceforge.net/projects/cmusphinx/files/sphinx4/5prealpha/
https://github.com/cmusphinx/sphinx4

http://labbcat.sourceforge.net/

https://www.geeksforgeeks.org/converting-text-speech-java/

### Dictionaries

#### Libre Office, Firefox

https://github.com/marcoagpinto/aoo-mozilla-en-dict
https://github.com/LibreOffice/dictionaries
https://en.wiktionary.org/wiki/recruit



#### English

https://www.universeofmemory.com/how-many-words-you-should-know/

http://testyourvocab.com/

* https://www.efset.org/english-score/
* https://www.efset.org/ru/english-score/cefr/

https://www.universeofmemory.com/how-many-words-you-should-know/
https://apps.ankiweb.net/

https://puzzle-english.com/vocabulary
https://puzzle-english.com/vocabulary/6737816?r=b096eea74f
Полноценная платформа для изучения инглиша. Довольно круто. Много моих идей уже реализовано



##### Word/Text Banks

https://www.english-corpora.org/coca/
https://www.corpusdata.org/purchase.asp
Полная база стоит денег, но можно искать, смотреть тест где используется

https://www.victoria.ac.nz/lals/resources/academicwordlist
База академических слов


##### Old word banks

http://web.archive.org/web/20070214114211/http://www.dcs.shef.ac.uk/research/ilash/Moby/




##### Word banks links

**http://www.manythings.org/vocabulary/lists/a/**
Слова, разнесённые по категориям


**http://gen.lib.rus.ec/search.php?&req=Wordlist&phrase=1&view=simple&column=def&sort=def&sortmode=ASC&page=2**
Cutting Edge 3 Edition Elementary Wordlist

**https://www.wordfrequency.info/purchase.asp**
Бесплатно 5000 слов. Платно - все банки. 

**http://martinweisser.org/corpora_site/word_lists.html**

http://www.bmanuel.org/clr2_et.html#Moby_Shakespeare
http://www.bmanuel.org/clr/clr2_et.html

http://iteslj.org/links/ESL/Vocabulary/Lists/
http://iteslj.org/links/ESL/Vocabulary/Lists/p2.html

##### Word banks

**https://en.wiktionary.org/wiki/Wiktionary:Frequency_lists#English**
Частота слов

**http://www.lexically.net/downloads/e_lemma.zip**
Примерно 12000 слов (нормальная форма -> связи)
book -> books,booking,booked

http://ucrel.lancs.ac.uk/bncfreq/
**http://ucrel.lancs.ac.uk/bncfreq/flists.html**
Слова и их формы, так же частота, тип. Очень здорово
	book	NoC	%	374	100	0.95
	@	@	book	243	100	0.95
	@	@	books	131	100	0.94

**http://storage.googleapis.com/books/ngrams/books/datasetsv2.html**
Тут есть распределение слов по годам (Google Books), громадные банки слов для многих распространенных языков. Очень много шлака, но можно вычислить популярность слов.
Так же можно видеть фразы до 5 слов.

**https://github.com/hackerb9/gwordlist**
Обработанные списки, шлака почти нет.

**http://norvig.com/google-books-common-words.txt**
Ещё список с частотами

**https://github.com/first20hours/google-10000-english**
10000-20000 слов из того же банка, есть фильтры по матерным словам

**http://number27.org/assets/misc/words.txt**
(the, 6.510891, 0), примерно 87000 слов

**http://www.kilgarriff.co.uk/bnc-readme.html**
BNC database and word frequency lists. Adam Kilgarriff
4907 1222 worldwide adv: ~6000 слов (без мн. чисел)

13073 books nn2 2186
70 books nn2-vvz 64
3 books np0 3
10 books vvz 10 - все слова, указаны их состояния, частота но без связи с основным словом

TODO very cool
**https://www.english.com/gse/teacher-toolkit/user/vocabulary?page=1&sort=gse;asc&gseRange=10;90&audience=GL**
Просто очень крутой сайтъ. Уважаю.
Слова, частоты, уровни, даже проценты.