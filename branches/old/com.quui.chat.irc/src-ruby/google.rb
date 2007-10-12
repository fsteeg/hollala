require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"

def init
  {"google"=>"doGoogle"}
end
def doGoogle(incoming)
  if (incoming.strip=='')
    return ["Usage: google <terms>",nil]
  else
    text = incoming.split(" ").join("+")
    adress = "http://www.google.com/search?q=#{text}"
    res = WebsiteLookup.new(adress,"UTF-8").text
    regex = /<h2 class=r>(.+?)(?=<\/h2>)/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.join.strip.gsub(/<([^>]+)>/,""))
        #all<<" " <<url.join
        url.join.scan /"([^"]+)"/m do |link|
                all << ": " << link.join
        end
    end
  end
  p "ALLLLLLLLLLLLLLLLL: " << all
  if(all[0..5]=="#Add a")
  p "HUHUHUHUHUHUHUHUHUHUHUHUHUHUHHUUHUHUH"
    all << " http://www.urbandictionary.com/insert.php?word=#{text}"
  end
  return [all,nil]
end