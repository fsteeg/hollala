require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"
def init
  {"diced"=>"doDiced"}
end
def doDiced(incoming)
  if (incoming.strip=='')
    return ["Usage: diced <terms>",nil];
  else
    text = incoming.split(" ").join("+")
    adress = "http://dict.die.net/?q=#{text}"
    res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /<pre>(.+?)(?=<\/pre>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return [all,nil]
end