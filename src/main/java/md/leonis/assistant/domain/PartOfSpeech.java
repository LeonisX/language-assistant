package md.leonis.assistant.domain;

// http://engblog.ru/parts-of-speech
public enum PartOfSpeech {

    NOUN, // Существительное (название, имя)
    PRONOUN, // Местоимение (заменяет) I, You, He, She, It, We, They, My, Our, Herself, Nobody
    VERB, // Глагол (описывает действие, опыт, состояние)
    ADVERB, // Наречие (описывает действие) Here, Today, Suddenly, Usually, Rather
    ADJECTIVE, // Прилагательное (описывает существительное)

    CONJUNCTION, // Союз (объединяет) And, But, Because, So, Or
    PREPOSITION, // Предлог Of, In, Under, Between, At, Towards
    INTERJECTION, // Междометия (усиливает) Oh! Wow! Ah! Yes! Huh! Eek!, Hey

    ARTICLE, // Артикль (article) The, A, An
    NUMERAL, // Числительные (numerals)

    CLAUSE_OPENER, // In order [that/to], so at [to]
    DETERMINER, // a, an, every, no, the !!!!!!!!!! Article
    DETERMINER_PRONOUN, // this, these, those, some, all
    EXISTENTIAL_PARTICLE, // there in, there is, there are
    FOREIGN_WORD,
    FORMULA, // 2x + z
    GENITIVE, // 's, '
    INFINITE_MARKER, // to
    LETTER, // a, b, c
    NEGATIVE_MARKER, // not, -n't
    NOUN_COMMON,
    NOUN_PROPER,
    NOUN_PROPER_PART,
    NUMERAL_CARDINAL, // one
    NUMERAL_ORDINAL, // first
    UNCLASSIFIED,
    MODAL_AUXILIARY_VERB // can, will, would, could, may, must, should

}
