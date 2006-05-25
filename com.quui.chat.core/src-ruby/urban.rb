require 'java'
require 'cgi'
include_class "com.quui.chat.commands.RegExLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"urban"=>"doUrban"}
end
def doUrban(incoming)
  if (incoming.strip=='')
    return "Usage: #{GlobalProperties::getInstance.getCommandPrefix}urban <terms>";
  else
    text = incoming.split(" ").join("+")
    adress = "http://www.urbandictionary.com/define.php?term=#{text}"
    res = RegExLookup::open(adress,"UTF-8")
    regex = /<p>(.+?)(?=<\/p>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return all
end