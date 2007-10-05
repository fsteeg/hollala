require 'src-ruby/calcs'
require 'test/unit'

class TestCalcs < Test::Unit::TestCase
  def test_kbit
    assert_equal(doKbit("512"),["512 kbit are 64.0 KByte (1 byte = 8 bit).",nil])  
  end
  def test_cost
    assert_equal(doCost("50 12 365"),
    ["Running 50.0 Watts 12.0 hours for 365.0 days at a price of 0.16 will cost about 35.",true]
    )  
  end
end
