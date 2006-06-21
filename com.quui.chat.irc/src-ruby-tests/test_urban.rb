require '../src-ruby/urban'
require 'test/unit'

class TestCalcs < Test::Unit::TestCase
  def test_urban
    puts "yo"
    assert_equal("hi","hi")
    puts ( doUrban "woot" )
  end
end
