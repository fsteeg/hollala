require 'java'
include_class "com.quui.chat.commands.PP"
include_class "com.quui.chat.wordgame.WordGameScores"
def init
  {"pp" => "doPP", "score" => "doScore", "scores" => "doScore"}
end
def doPP(incoming)
  PP.new.getPP
end
def doScore(incoming)
  WordGameScores.new.getScores(incoming.to_i)
end