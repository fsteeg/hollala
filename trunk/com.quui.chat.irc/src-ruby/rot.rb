#######################################################################
# A sample ruby extension for Hollaka showing user interaction from IRC
# (http://hollala.sourceforge.net)
#######################################################################

# Map commands to methods in a method called init
def init 
  { "rot13" => :start_rot, "r13" => :start_rot } 
end

# The actual action in a method called as mapped to in init, 
# with one parameter (the incoming text, this is fixed)
def start_rot incoming
  # Returns the answer and the name of the method to call next
  ["Rot-13 (Demo), enter text:", :crypt]
end

def crypt text
  # Returns the answer and nil to indicate we're done (no next method)
  [text.tr("A-Za-z", "N-ZA-Mn-za-m"), nil]
end
