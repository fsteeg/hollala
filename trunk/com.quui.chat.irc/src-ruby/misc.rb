require 'java'
include_class "com.quui.chat.commands.PP"
include_class "com.quui.chat.wordgame.WordGameScores"
def init
  {"pp" => "getPP", "ppadd" => "addPP", "score" => "doScore", "scores" => "doScore"}
end
def getPP(incoming)
    pp = PP.new
    [pp.getPP(incoming),nil]
end
def addPP(incoming)
        if (incoming.strip=='')
                return ["Usage: ppadd <quote to add>",nil]
        else
                [PP.new.addPP(incoming),nil]
        end
end
def doScore(incoming)
  [WordGameScores.new.getScores(incoming.to_i),nil]
end
