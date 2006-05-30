require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"wiktion"=>"doWictionary"}
end
def doWictionary(incoming)
  if (incoming.strip=='')
    return "Usage: wiktion <terms>";
  else
    text = incoming.split(" ").join("_")
    adress = "http://en.wiktionary.org/wiki/#{text}"
    res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /<li>(.+?)(?=<\/li>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,"")) << " "
    end
  end
  if all ==""
    return "[ nothing found ]"
  end
  return all
end