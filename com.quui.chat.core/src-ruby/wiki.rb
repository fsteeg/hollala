require 'java'
require 'cgi'
include_class "com.quui.chat.commands.RegExLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"wiki"=>"doWikiEn", "wikid" => "doWikiDe"}
end
def doWikiDe(incoming)
  doWiki("de",incoming)
end

def doWikiEn(incoming)
  doWiki("en",incoming)
end

def doWiki(lang, incoming)
  incoming = incoming.split(" ").collect{|word|word.capitalize!}.join(" ")
  if (incoming.strip=='')
    return "Usage: #{GlobalProperties::getInstance.getCommandPrefix}wiki <terms>";
  else
    text = incoming.split(" ").join("_")
    adress = "http://#{lang}.wikipedia.org/wiki/#{text}"
    res = RegExLookup::open(adress,"UTF-8")
    regex = /<p>(.+?)(?=<\/p>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return all
end