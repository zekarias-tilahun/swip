import csv

header = "INSERT INTO echocardiogram (A,B,C,D,E,F,G,H,I,J,K,L,M) VALUES "
with open('echocardiogram.csv','rb') as fin: # `with` statement available in 2.5+
    # csv.DictReader uses first line in file for column headings by default
    dr = csv.DictReader(fin) # comma is default delimiter
    
    to_db = [(i['A'], i['B'], i['C'], i['D'], i['E'], i['F'], i['G'], i['H'], i['I'], i['J'], i['K'], i['L'], i['M']) for i in dr]

output = open("echocardiogram.sql", "wb")
for i in to_db:
	output.write(header + str(i) + "\n")

output.close()