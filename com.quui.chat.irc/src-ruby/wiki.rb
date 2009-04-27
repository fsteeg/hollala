require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"
def init
  {"wiki"=>"doWiki"}
end

def doWiki(incoming)
  in_toks = incoming.split(" ")
  incoming = in_toks[1..in_toks.length-1].collect{|word|word.capitalize!}.join(" ")
  if (in_toks.length < 2)
    return ["Usage: wiki <lang> <terms>'",nil];
  else
    text = incoming.split(" ").join("_")
    adress = "http://#{in_toks[0]}.wikipedia.org/wiki/#{text}"
   res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /(?:<p>(.+?)(?=<\/p>))|(?:<li>(.+?)(?=<\/li>))/m
    all=adress<<': '
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
    end
  end
  return [all,nil]
end