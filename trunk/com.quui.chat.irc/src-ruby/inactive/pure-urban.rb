#require 'jruby'
load 'open-uri'
require 'cgi'
def init
  {"urbanp"=>"doPureUrban"}
end
def doPureUrban(incoming)
  split = incoming.split(" ")
  if (split.length < 1)
    "Usage: !urban <term>";
  else
    text = split[0..split.length].join("+")
    #puts text
    all = ''
    adress = "http://www.urbandictionary.com/define.php?term=#{text}"
    puts adress
    Kernel::open( adress ) do |html|
      text = html.read
      #puts text
      counter = 0
      regex = /<p>(.+?)(?=<\/p>)/m
      longregex = /<div class="def_p">(:?[^<]+)<p>(.+)(?=<\/p>)/m
      text.scan regex do |url|
        all << "#" << CGI.unescapeHTML(url.join).strip
        #puts all
      end
    end
    #all
  end
  return all
end