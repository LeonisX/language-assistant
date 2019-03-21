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

* Dictionary importer


#TODO bugs

Shut down sound from browser when window is closed



### TODO levels

* https://ru.wikipedia.org/wiki/%D0%9E%D0%B1%D1%89%D0%B5%D0%B5%D0%B2%D1%80%D0%BE%D0%BF%D0%B5%D0%B9%D1%81%D0%BA%D0%B8%D0%B5_%D0%BA%D0%BE%D0%BC%D0%BF%D0%B5%D1%82%D0%B5%D0%BD%D1%86%D0%B8%D0%B8_%D0%B2%D0%BB%D0%B0%D0%B4%D0%B5%D0%BD%D0%B8%D1%8F_%D0%B8%D0%BD%D0%BE%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%BD%D1%8B%D0%BC_%D1%8F%D0%B7%D1%8B%D0%BA%D0%BE%D0%BC
* https://en.wikipedia.org/wiki/Common_European_Framework_of_Reference_for_Languages
* https://tracktest.eu/english-levels-cefr/
* http://vocabularypreview.englishprofile.org/staticfiles/about.html
* Far perspective: keep in DB as language settings
* https://www.efset.org/english-score/

Minimal:

A1   A2    B1    B2    C1    C2
12%  24%   48%   72%   90%   100%
480, 960,  1920, 2880, 3600, 4000
500, 1000, 2000, 3000, 3750, 4167
600, 1200, 2400, 3600, 4500, 5000

Realistic (my view):

A1   A2    B1    B2    C1    C2
500, 1000, 2000, 4000, 8000, 16000


### TODO levels -> words

https://www.universeofmemory.com/how-many-words-you-should-know/
https://www.quora.com/What-approximate-number-of-vocabulary-could-one-assign-to-the-CEFR-levels-with-European-languages
https://languagelearning.stackexchange.com/questions/3061/what-are-estimates-of-vocabulary-size-for-each-cefr-level
http://erfoundation.org/wordpress/graded-readers/


LeoLingo



### Dictionaries

#### English

https://www.cambridgeenglish.org/learning-english/exam-preparation/
http://testyourvocab.com/
http://www.manythings.org/vocabulary/lists/a/
https://www.universeofmemory.com/how-many-words-you-should-know/
https://www.flocabulary.com/wordlists/
https://www.ef.com/wwen/english-resources/english-vocabulary/
https://www.wordfrequency.info/top5000.asp
http://martinweisser.org/corpora_site/word_lists.html
http://www.kilgarriff.co.uk/bnc-readme.html
https://www.victoria.ac.nz/lals/resources/academicwordlist
http://number27.org/assets/misc/words.txt
https://www.wordandphrase.info/frequencyList.asp
https://github.com/first20hours/google-10000-english
http://www.naturalenglish.club/esl/1000.php
http://norvig.com/google-books-common-words.txt
http://ucrel.lancs.ac.uk/bncfreq/
http://ucrel.lancs.ac.uk/bncfreq/flists.html
http://www.kilgarriff.co.uk/bnc-readme.html


TODO very cool
https://www.english.com/gse/teacher-toolkit/user/vocabulary?page=1&sort=gse;asc&gseRange=10;90&audience=GL

##### Levels

http://www.eltcloseup.com/a1-student-zone/alphabetical-word-list
https://www.toe.gr/pluginfile.php?file=%2F2142%2Fmod_resource%2Fcontent%2F1%2FLevel%20B2%20Word%20List.pdf
https://www.vocabulary.com/lists/search?query=a1
https://www.cambridgeenglish.org/Images/22105-ket-vocabulary-list.pdf
https://quizlet.com/46488736/esl-vocabulary-b2-wordlist-english-upper-intermediate-flash-cards/
https://www.pearson.ch/LanguageTeaching/tab2740/Wordlists



#### Russian

https://www.myvocab.info/howitworks
https://www.myvocab.info/articles
http://www.speakrus.ru/dict/
http://linguists.narod.ru/downloads5.html#slav
https://pishu-pravilno.livejournal.com/591215.html
http://www.ruscorpora.ru/corpora-freq.html
http://ruscorpora.ru/corpora-freq.html

#### Other

https://puzzle-english.com/vocabulary
