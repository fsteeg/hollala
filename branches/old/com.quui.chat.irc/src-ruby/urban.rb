include Java
require "hollala.jar" 
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
  p "ALLLLLLLLLLLLLLLLL: " << all
  if(all[0..5]=="#Add a")
  p "HUHUHUHUHUHUHUHUHUHUHUHUHUHUHHUUHUHUH"
    all << " http://www.urbandictionary.com/insert.php?word=#{text}"
  end
  if(all.strip=='')
  	all = 'No idea!'
  end
  return [all,nil]
end