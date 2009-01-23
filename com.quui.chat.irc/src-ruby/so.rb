include Java
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"

def init
  {"so"=>"doSo"}
end
def doSo(incoming)
  adress = "http://stackoverflow.com"
  if (incoming.strip=='')
   adress = adress#"http://stackoverflow.com"#return ["Usage: so (<tags>)",nil]
  else
    text = incoming.split(" ").join("+")
    adress = adress<<"/questions/tagged/#{text}"
   end
    res = WebsiteLookup.new(adress,"UTF-8").text
   	regex = /<h3><a href="([^"]+)"[^>]*>([^<]+)<\/a><\/h3>/m
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.reverse.join(": http://stackoverflow.com").strip.gsub(/<([^>]+)>/,""))
  end
  p "All: " << all
  if(all.strip=='')
  	all = 'No idea!'
  end
  return [all,nil]
end