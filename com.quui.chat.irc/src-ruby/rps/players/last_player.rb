#---
# Excerpted from "Best of Ruby Quiz"
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/fr_quiz for more book information.
#---
class PickLastPlayer < Player
  
  def initialize( opponent_name )
    @pick = :rock
  end
  
  def choose
    @pick
  end
  
  def result( your_choice, opponents_choice, win_lose_or_draw )
    @pick = opponents_choice
  end
end
