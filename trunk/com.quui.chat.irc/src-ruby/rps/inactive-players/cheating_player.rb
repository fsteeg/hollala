#---
# Excerpted from "Best of Ruby Quiz"
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/fr_quiz for more book information.
#---
class CheatingPlayer < Player
  
  def initialize( opponent_name )
    Object.const_get(opponent_name).class_eval do
      alias_method :old_choose, :choose
      def choose
        :paper
      end
    end
  end
  
  def choose
    :scissors
  end
  
end
