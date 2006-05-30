require 'java'
require 'cgi'
include_class "com.quui.chat.commands.RegExLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"wiki"=>"doWiki"}
end

def doWiki(incoming)
  in_toks = incoming.split(" ")
  incoming = in_toks[1..in_toks.length-1].collect{|word|word.capitalize!}.join(" ")
 # puts "incoming: " << incoming
  if (in_toks.length < 2)
    return "Usage: '#{GlobalProperties::getInstance.getCommandPrefix}wiki <lang> <terms>'";
  else
    text = incoming.split(" ").join("_")
    adress = "http://#{in_toks[0]}.wikipedia.org/wiki/#{text}"
   # puts "opening: " + adress
    res = RegExLookup::open(adress,"UTF-8")
    regex = /<p>(.+?)(?=<\/p>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return all
end