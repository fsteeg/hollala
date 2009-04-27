#---
# Excerpted from "Best of Ruby Quiz"
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/fr_quiz for more book information.
#---
BEATS = {:rock=>:paper,:paper=>:scissors,:scissors=>:rock}
class PsychicPlayer < Player
 
  def initialize( opponent_name )
    #    @pick = :stone
    super
    @enemy = Object.const_get(opponent_name).new(self)
  end
  
  def choose
    BEATS[@enemy.choose]
  end
  
  def result( your_choice, opponents_choice, win_lose_or_draw )
    @enemy.result( opponents_choice, your_choice, win_lose_or_draw )
  end
end
