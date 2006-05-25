require 'java'
require 'cgi'
include_class "com.quui.chat.commands.RegExLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"diced"=>"doDiced"}
end
def doDiced(incoming)
  if (incoming.strip=='')
    return "Usage: #{GlobalProperties::getInstance.getCommandPrefix}diced <terms>";
  else
    text = incoming.split(" ").join("+")
    adress = "http://dict.die.net/?q=#{text}"
    res = RegExLookup::open(adress,"UTF-8")
    regex = /<pre>(.+?)(?=<\/pre>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return all
end