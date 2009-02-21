# Needs to be run with JRuby

require 'src-ruby/misc'
require 'test/unit'

class TestCalcs < Test::Unit::TestCase
  def test_urban
  	rep_1 = getPP('')[0];
	rep_2 = getPP('design')[0];
	puts rep_1
	puts rep_2
	puts getPP('cockoo')[0]
    assert_equal(true, rep_1 != nil)
	assert_equal(true, rep_2 != nil)
	puts rep_2.downcase.include?("design")
  end
end
