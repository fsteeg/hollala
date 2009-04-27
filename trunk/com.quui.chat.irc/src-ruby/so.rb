include Java
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"

def init
  {"so"=>"doSo"}
end
def doSo(incoming)
  adress = "http://stackoverflow.com"
  regex = /<h3><a href="([^"]+)"[^>]*>([^<]+)<\/a><\/h3>/m
  if (incoming.strip=='')
   adress = adress
  else
    text = incoming.split(" ").join("+")
    adress = adress<<"/questions/tagged/#{text}"
   end
    res = WebsiteLookup.new(adress,"UTF-8").text
   	
    all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.reverse.join(": http://stackoverflow.com").strip.gsub(/<([^>]+)>/,""))
  end
  p "All in doSo via tags: " << all
  if(all.strip=='')
  	adress = "http://stackoverflow.com/search?q=#{text}"
	 res = WebsiteLookup.new(adress,"UTF-8").text
	 all=''
    res.scan regex do |url|
      all << "#" << CGI.unescapeHTML(url.reverse.join(": http://stackoverflow.com").strip.gsub(/<([^>]+)>/,""))
	  end
  end
  p "All in doSo after search: " << all
  if(all.strip=='')
  	all = 'No idea!'
  end
  return [all,nil]
end