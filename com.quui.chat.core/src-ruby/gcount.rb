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
  if(all.strip=='') 
    all = "0"
  end
  return [lcd(all) << " " << URI.escape(adress),nil] 
end

def lcd number
  puts "Number: " + number
  s1 = "";s2 = "";s3 = ""
  number.to_s.split(//).each do |char|
    puts char
    case char
    when "," :
      s1 << " . "
      s2 << " . " 
      s3 << " . "
    when "1" : 
      s1 << " . "
      s2 << " | " 
      s3 << " | "
    when "2" : 
      s1 << " _ " 
      s2 << " _|" 
      s3 << "|_ "
    when "3" : 
      s1 << " _ "
      s2 << " _|" 
      s3 << " _|"
    when "4" : 
      s1 << ". ." 
      s2 << "|_|" 
      s3 << ". |"
    when "5" : 
      s1 << " _ "
      s2 << "|_ " 
      s3 << " _|"
    when "6" : 
      s1 << " _ " 
      s2 << "|_ " 
      s3 << "|_|"
    when "7" : 
      s1 << " _ " 
      s2 << "| |" 
      s3 << "  |"
    when "8" : 
      s1 << " _ "
      s2 << "|_|" 
      s3 << "|_|"
    when "9" : 
      s1 << " _ " 
      s2 << "|_|" 
      s3 << " _|"
    when "0" : 
      s1 << " _ "
      s2 << "| |" 
      s3 << "|_|"
    end
  end
  res = [s1, s2 << " Hits at",s3].join("#")
  res.split("#").each{|line| puts line}
  return res
end