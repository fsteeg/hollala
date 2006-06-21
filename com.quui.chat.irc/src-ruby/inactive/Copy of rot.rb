#######################################################################
# A sample ruby extension for Hollaka showing user interaction from IRC
# (http://hollala.sourceforge.net)
#######################################################################

# Map commands to methods in a method called init
def init {"rot13"=>:start_rot, "r13"=>:start_rot} end

# The actual action in a method called as mapped to in init, 
# with one parameter (the incoming text)
def start_rot incoming
  # The command without any arguments, to start
  if (incoming.strip=='')
    # Returns false in any case to indicate we're not done
    ["Rot-13 (Demo), enter text:",false]
  else
    # Returns true to indicate we're done
    [incoming.tr("A-Za-z", "N-ZA-Mn-za-m"),true]
  end
end
