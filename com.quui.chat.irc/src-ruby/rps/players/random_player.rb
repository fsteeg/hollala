#!/usr/bin/env ruby
#---
# Excerpted from "Best of Ruby Quiz"
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/fr_quiz for more book information.
#---

class RandomPlayer < Player
  CHOICES = [ :rock, :scissors, :paper ]

  def choose
    choice = CHOICES[rand 3]
  end
end
