#! /usr/bin/env python
# -*- coding: utf-8 -*-
import sys
import os
from gmail import Gmail

def main():
	# Bob is sender, Alide is destination.
	from_address = "bob@domain.com"
	to_address = "alice@domain.com"
	file_info = {"type":"pdf", "subtype":"pdf"}
	a_title = u"Research: slide of today" 
	a_body = u'''Dear Alice.
	
Good afternoon, I'm Bob.
I attach today's slide(2015-04-01.pdf) to this mail.

-----------
Bob Marley.
Kyoto Sangyo University in Japan.
bob@domain.com.
'''
	files = []
	for a_file in os.listdir('.'):
		if ".pdf" in a_file:
			files.append(a_file)

	if len(files) >= 2:
		print "PDF is only one."
		for a_file in files:
			print a_file.decode('utf-8')
	elif len(files) == 0:
		print "PDF is not found."
	elif len(files) == 1:
		a_file = files[0]
		file_info["name"] = a_file
		file_info["path"] = "./" + a_file
		comment = raw_input("comment > ").decode('utf-8')
		if len(comment) == 0:
			a_body = a_body % (a_file.decode('utf-8'), comment)
		else:
			a_body = a_body % (a_file.decode('utf-8'), "\n"+comment)
		print "\n"
		print a_title
		print "*" * 50
		print a_body
		print "*" * 50
		
		if raw_input("Is This OK? ") == 'y':
			a_gmail = Gmail()
			a_message = a_gmail.create_message(from_address, to_address, a_title, file_info, a_body)
			a_gmail.send(from_address, to_address, a_message)
			print u"Sent!"
		else:
			print u"Sending miss."

if __name__ == '__main__':
	sys.exit(main())
