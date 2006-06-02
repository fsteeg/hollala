#######################################################################
# A sample ruby extension for Hollaka showing user interaction from IRC
# (http://hollala.sourceforge.net)
#######################################################################

# Map commands to methods in a method called init
def init 
  { "madlibs" => :start_mad } 
end

# matches "((stuff like this))", capturing "stuff like this"
@regex = /\(\((.+?)\)\)/

def start_mad(incoming) 
  if incoming != ''
    @text = incoming
  else
    @text = "I had an ((an adjective)) sandwich for lunch toaday. It dripped all over ((a body part)) and ((a noun))."
  end
  insert_next
end

# requests the next missing element
def insert_next
  @text.scan(@regex) {|match|
    # request an input and specify which method to use for processing the input
    return ["Please enter #{match.join(" ")}.", :insert_answer]
  }
  # using the # will cause the bot to start a new line, the nil says we're done
  return [@text << "#" << ":)", nil]
end

# inserts the given answer and goes on
def insert_answer insert 
  @text.sub!(@regex, insert)
  insert_next
end
