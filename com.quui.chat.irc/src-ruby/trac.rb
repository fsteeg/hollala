# Some basic stuff for accesssing a trac repository.
require 'java'
require 'cgi'
include_class "com.quui.chat.commands.WebsiteLookup"
def init
  # We define which command should trigger which method
  {"ticket"=>"linkTicket", "change"=>"linkChange", "trac" => "linkTrac"}
end

def linkTrac(incoming)
  in_toks = incoming.split(" ")
  incoming = in_toks[1..in_toks.length-1].collect{|word|word.capitalize!}.join(" ")
  if (in_toks.length < 1)
    return ["Usage: trac <ticket> | <changeset> (in trac syntax)", nil];
  else
    if(in_toks[0]=~/#([0-9]+)/)
        return ["http://my-trac.assembla.com/quui/" + "ticket/#{$1}", nil]
    elsif(in_toks[0]=~/\[([0-9]+)\]/)
      return ["http://my-trac.assembla.com/quui/" + "changeset/#{$1}", nil]
    end
  end
end

def linkTicket(incoming)
  in_toks = incoming.split(" ")
  incoming = in_toks[1..in_toks.length-1].collect{|word|word.capitalize!}.join(" ")
  if (in_toks.length < 1)
    return ["Usage: ticket <id>", nil];
  else
    return ["http://my-trac.assembla.com/quui/" + "ticket/#{in_toks[0]}", nil] 
  end
end

def linkChange(incoming)
  in_toks = incoming.split(" ")
  incoming = in_toks[1..in_toks.length-1].collect{|word|word.capitalize!}.join(" ")
  if (in_toks.length < 1)
    return ["Usage: change <id>", nil];
  else
    return ["http://my-trac.assembla.com/quui/" + "changeset/#{in_toks[0]}", nil]
  end
end