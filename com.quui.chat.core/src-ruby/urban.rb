require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"

def init
  {"urban"=>"doUrban"}
end
def doUrban(incoming)
  if (incoming.strip=='')
    return ["Usage: urban <terms>",nil]
  else
    text = incoming.split(" ").join("+")
    adress = "http://www.urbandictionary.com/define.php?term=#{text}"
    res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /<p>(.+?)(?=<\/p>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return [all,nil]
end