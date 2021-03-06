require 'java'
include_class "com.quui.chat.commands.Leo"
include_class "com.quui.chat.commands.Babelfish"
include_class "com.quui.chat.mind.wn.WNLookup"
def init
  #"leo" => "doLeo", "babel" => "doBabel", "translate" => "doBabel",
  { 
  "stem" => "doStem", "wn" => "doDefine", "define" => "doDefine"}
end
def doLeo(incoming)
  if (incoming.length == 0)
    ["Usage: leo <word-to-translate>",nil]
  else
    [Leo::lookup(incoming),nil]
  end
end
def doBabel(incoming)
  split = incoming.split(" ")
  if (split.length < 3)
    ["Usage: babel <source-lang> <dest-lang> <text>",nil]
  else
    text = split[2..split.length].join(" ")
    [Babelfish.new.translate(split[0], split[1], text),nil]
  end
end
#these use the internal dict... attention to that...
def doDefine(incoming)
  if (incoming.length == 0  || incoming.split(" ").length > 1)
    ["Usage: define <word-to-define>",nil]
  else
    r = WNLookup.new.getDefString(incoming)
	res = "Definitions for \"#{incoming}\":##{r}";
	if(r.strip=='')
		res = 'No idea!'
	end	
    [res,nil]
  end
end
def doStem(incoming)
  if (incoming.length == 0  || incoming.split(" ").length > 1)
    ["Usage: stem <word-to-stem>",nil]
  else
    r = WNLookup.new.getStem(incoming)
    ["Stem for \"#{incoming}\": #{r}",nil]
  end
end