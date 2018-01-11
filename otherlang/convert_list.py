#!/usr/bin/python3.5

import urllib.request
import re
import requests
import json
import time
import traceback

try: 
    from BeautifulSoup import BeautifulSoup
except ImportError:
    from bs4 import BeautifulSoup
	
print ('Start')

with open('constants.xml', 'r', encoding="utf-8") as myfile:
    data=myfile.read()

constants_list = BeautifulSoup(data) #, "html-parser")
	
print(constants_list)

en_list = BeautifulSoup().new_tag("array")
es_list = BeautifulSoup().new_tag("array")
fr_list = BeautifulSoup().new_tag("array")

for art in constants_list.array.find_all('item'):

	print (art)
	
	startPage = "https://it.wikipedia.org/wiki/" + art.text
	
	print ("CURRENT="+startPage)
	wp = urllib.request.urlopen(startPage)
	html = wp.read()

	#print (html)

	parsed_html = BeautifulSoup(html) #, "html-parser")
	#print (parsed_html)

	elements = parsed_html.find('div', attrs={'id':'p-lang'}).find_all('li')
	#print (elements)

	for i in range(len(elements)):
		el = elements[i]
		
		#print (el.a)
		if(el.a['lang'] == 'en'):
			print (el.a['lang'] + " = " + el.a['href'])
			item = BeautifulSoup().new_tag("item")
			item.string = el.a['href']
			en_list.append(item)
		elif(el.a['lang'] == 'es'):
			print (el.a['lang'] + " = " + el.a['href'])
			item = BeautifulSoup().new_tag("item")
			item.string = el.a['href']
			es_list.append(item)
		elif(el.a['lang'] == 'fr'):
			print (el.a['lang'] + " = " + el.a['href'])
			item = BeautifulSoup().new_tag("item")
			item.string = el.a['href']
			fr_list.append(item)
	
	print (en_list)
	print (es_list)
	print (fr_list)
	
	time.sleep(3)


