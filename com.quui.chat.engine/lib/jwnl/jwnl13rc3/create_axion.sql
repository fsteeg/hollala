CREATE TABLE IndexWord (
  index_word_id   integer(10),
  lemma           varchar(255),
  pos             char
);

CREATE TABLE Synset (
  synset_id       integer(10),
  file_offset     integer(10),
  pos             char,
  is_adj_cluster  boolean,
  gloss           varchar(255)
);

CREATE TABLE SynsetWord (
  synset_word_id  integer(10),
  synset_id       integer(10),
  word            varchar(255),
  word_index      integer(10)
);

CREATE TABLE SynsetPointer (
  synset_pointer_id integer(10),
  synset_id         integer(10),
  pointer_type      varchar(2),
  target_offset     integer(10),
  target_pos        char,
  source_index      integer(10),
  target_index      integer(10)
);

CREATE TABLE SynsetVerbFrame (
  synset_verb_frame_id  integer(10),
  synset_id             integer(10),
  frame_number          integer(10),
  word_index            integer(10)
);

CREATE TABLE IndexWordSynset (
  index_word_synset_id  integer(10),
  index_word_id         integer(10),
  synset_id             integer(10)
);

CREATE TABLE Exception (
  exception_id    integer(10),
  pos             char,
  exception       varchar(255),
  lemma           varchar(255)
);