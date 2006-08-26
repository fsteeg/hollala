require 'java'
include_class "com.quui.chat.commands.PP"
include_class "com.quui.chat.wordgame.WordGameScores"
def init
  {"pp" => "doPP", "score" => "doScore", "scores" => "doScore"}
end
def doPP(incoming)
  pp = PP.new
#  answer = PP.new.getPP
#  if(incoming!=nil)
#    print "INCOMING: " + incoming
#    (incoming.to_i)-1.times {
#      answer = answer + "#" + PP.new.getPP
#    }
#  end
  ["You might find it useful to ponder on these:#" + pp.getPP + "#" + pp.getPP + "#" + pp.getPP,nil]
end
def doScore(incoming)
  [WordGameScores.new.getScores(incoming.to_i),nil]
end