require 'java'
require 'cgi'
require 'uri'
include_class "com.quui.chat.commands.WebsiteLookup"
include_class "com.quui.chat.GlobalProperties"
def init
  {"gcount"=>"doGCount"}
end
def doGCount(incoming)
  if (incoming.strip=='')
    return ["Usage: gcount <terms>",nil]
  else
    text = incoming.split(" ").join("+")
    adress = "http://www.google.com/search?q=#{text}"
    res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /about <b>(.+?)(?=<\/b>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return [all << " " << URI.escape(adress),nil]
end