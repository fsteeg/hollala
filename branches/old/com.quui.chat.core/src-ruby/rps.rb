#######################################################################
# A sample ruby extension for Hollaka showing user interaction from IRC
# (http://hollala.sourceforge.net)
#######################################################################

# Map commands to methods in a method called init
def init 
  { "rps-battle" => :start_battle} 
end

def start_battle incoming
  require "src-ruby/rock_paper_scissors/rock_paper_scissors"
  Dir.foreach("src-ruby/rock_paper_scissors/players") do |file|
    file = "src-ruby/rock_paper_scissors/players/" << file
    p "FILE: " << file
    
    next if file =~ /^\./
    next unless file =~ /\.rb$/
    require File.join(p, file)
  end
  r=""
  Player.each_pair do |one, two|
    game = Game.new one, two
    game.play 1000
    res = game.results.gsub(/\n/, " | ")
    p "RESULTS: " << res
    r << "#" << res
  end
  # Returns the answer and the name of the method to call next
  [r, nil]
end
