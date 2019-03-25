### Language Assistant is your personal assistant in learning foreign languages.

#### Language Assistant - твой персональный помощник в изучении иностранных языков.  

This is a model of the project, designed to simplify the study of foreign languages. For now, just a sketch of the integration of Spring Boot 2 and JavaFX 8.

## Scratch

### Done

* Splash screen (mock-up)
* Dashboard screen (mock-up)
* WordBank screen (mock-up, w/o filters)
* VideoList screen (mock-up)
* WatchScript screen (mock-up)
* WatchVideo screen (mock-up)
* Dictionary screen (mock-up)

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