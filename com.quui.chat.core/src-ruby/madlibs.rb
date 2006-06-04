#######################################################################
# A sample ruby extension for Hollaka showing user interaction from IRC
# (http://hollala.sourceforge.net)
#######################################################################

# Map commands to methods in a method called init
def init 
  { "madlibs" => :start_mad , "madlibs-patterns" => :list_patterns}
end

# matches "((stuff like this))", capturing "stuff like this"
@regex = /\(\((.+?)\)\)/
# file where the patterns are stored
@file = "src-ruby/madlibs"
# mapping of variables used
@map = {}

def list_patterns incoming
  [open(@file,"r"){ |file| file.collect{ |line| line << "#" } }.join(" "),nil]
end

def start_mad(incoming) 
  if incoming != ''
    # adds the given line to the file
    open(@file,"a+"){ |file| file << "\n" << incoming }
    ["OK.", nil]
  else
    # the text line to use
    @text = random_line  << "#" << ":)"
    insert_next
  end
end

# requests the next missing element
def insert_next
  @text.scan(@regex) {|match|
    question = match.join(" ");
    # check the mapping:
    if(@map.has_key?(question))
      insert_answer(@map[question])
    else
      if(question.include?(":"))
        question = question.split(":")[1]
      end
      # request an input and specify which method to use for processing the input
      return ["Please enter #{question}.", :insert_answer]
    end
  }
  # using the # will cause the bot to start a new line, the nil says we're done
  return [@text, nil]
end

# inserts the given answer and goes on
def insert_answer insert 
  @text.scan(@regex){|match| 
    m=match.join(" ")
    if m.include?(":")
      k=m.split(":")[0]
      @map[k]=insert
    end
  }
  @text.sub!(@regex, insert)
  insert_next
end

# use a random pattern from the file
def random_line
  lines = open(@file,"r"){ |file| file.collect }
  lines[rand(lines.length)]
end
