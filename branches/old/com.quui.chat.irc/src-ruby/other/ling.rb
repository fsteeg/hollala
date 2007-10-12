def transform
  puts "Enter the language material: "
  material = gets.split(" ")
  puts "Enter the glosses: "
  glosses = gets.split(" ")
  puts "Enter the meaning "
  meaning = gets.strip
  ls = material.collect{|w| 'l'}.join
  full = ['\\begin{tabular}{'<<ls<<'}'] 
  full << material.collect{|word|"\\textit{#{word}}"}.join(" & ")
  full << glosses.join(" & ")
  full << "\\end{tabular}"
  full << "'#{meaning}'"
  
  puts full.join(" \\\\ \n")
end