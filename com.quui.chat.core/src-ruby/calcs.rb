def init
  {"cost" => "doCost", "kbit" => "doKbit", "kb" => "doKbit"}
end
def doCost(incoming)
  split = incoming.split(" ")
  if (split.length < 3)
      "Usage: cost <watts> <hours> <days>";
  else
    elems = incoming.split(" ")
    cost = 0.16
    watts = elems[0].to_f
    hours = elems[1].to_f
    days = elems[2].to_f
    result = (watts / 1000) * hours * cost * days
  "Running #{watts} Watts #{hours} hours for #{days} days at a price of #{cost} will cost about #{result.to_i}."
  end
end

def doKbit(incoming)
  if (incoming.length == 0 || incoming.split(" ").length > 1)
    "Usage: kbit <kbits>"
  else
    "#{incoming} kbit are #{(incoming.to_f / 8)} KByte (1 byte = 8 bit)."
  end
end
