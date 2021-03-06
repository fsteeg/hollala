require 'java'
require 'cgi'
require 'uri'
include_class "com.quui.chat.commands.WebsiteLookup"
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
  return [lcd(all) << " Hits: " << URI.escape(adress),nil] 
end

def lcd number
  puts "Number: " + number
  rows = ["","",""]
  number.to_s.split(//).each do |char|
    puts char
    case char
    when "," :
      rows[0] << " . "
      rows[1] << " . " 
      rows[2] << " . "
    when "1" : 
      rows[0] << " . "
      rows[1] << " | " 
      rows[2] << " | "
    when "2" : 
      rows[0] << " _ " 
      rows[1] << " _|" 
      rows[2] << "|_ "
    when "3" : 
      rows[0] << " _ "
      rows[1] << " _|" 
      rows[2] << " _|"
    when "4" : 
      rows[0] << ". ." 
      rows[1] << "|_|" 
      rows[2] << ". |"
    when "5" : 
      rows[0] << " _ "
      rows[1] << "|_ " 
      rows[2] << " _|"
    when "6" : 
      rows[0] << " _ " 
      rows[1] << "|_ " 
      rows[2] << "|_|"
    when "7" : 
      rows[0] << " _ " 
      rows[1] << "| |" 
      rows[2] << "  |"
    when "8" : 
      rows[0] << " _ "
      rows[1] << "|_|" 
      rows[2] << "|_|"
    when "9" : 
      rows[0] << " _ " 
      rows[1] << "|_|" 
      rows[2] << " _|"
    when "0" : 
      rows[0] << " _ "
      rows[1] << "| |" 
      rows[2] << "|_|"
    end
  end
  return rows.join("#")
end