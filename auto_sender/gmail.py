#!/usr/bin/ python
# -*- coding: utf-8 -*-

import sys
import smtplib
from email import Encoders
from email.MIMEText import MIMEText
from email.MIMEBase import MIMEBase
from email.MIMEMultipart import MIMEMultipart
from email.Header import Header
from email.Utils import formatdate

class Gmail(object):
	def create_message(self, from_address, to_address, a_title, file_info, a_body):
		a_message = MIMEMultipart()
		a_body = MIMEText(a_body, _charset='iso-2022-jp')
		a_message['Subject'] = a_title
		a_message['From'] = from_address
		a_message['To'] = to_address
		a_message['Date'] = formatdate()
		a_message.attach(a_body)

		attachment = MIMEBase(file_info['type'], file_info['subtype'])
		with open(file_info['path']) as a_file:
			attachment.set_payload(a_file.read())
		Encoders.encode_base64(attachment)
		a_message.attach(attachment)
		attachment.add_header("Content-Disposition", "attachment", filename=file_info['name'])
		return a_message

	def send(self, from_address, to_address, a_message):
		a_smtp = smtplib.SMTP('smtp.gmail.com', 587)
		a_smtp.ehlo()
		a_smtp.starttls()
		a_smtp.ehlo(0)
		a_smtp.login(from_address,'4?SiFLV=tY')
		a_smtp.sendmail(from_address, to_address, a_message.as_string())
		a_smtp.close()
