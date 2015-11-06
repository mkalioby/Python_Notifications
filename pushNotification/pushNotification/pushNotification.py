#! /usr/bin/python

from __future__ import print_function
try:
	import ConfigParser
except:
	import configparser as ConfigParser
import os
import simplejson,sys
from future.standard_library import install_aliases
install_aliases()

from urllib.parse import urlparse, urlencode
from urllib.request import urlopen, Request
from urllib.error import HTTPError
import urllib
class NoAPIKey(Exception):
	def __init__(self, message):
		self.message = message

def getAPIKey():
	config = ConfigParser.RawConfigParser()
	if os.path.exists(os.path.expanduser("~/.pushNotification.cfg")):
		config.read(os.path.expanduser("~/.pushNotification.cfg"))
	else:
		if os.path.exists("/etc/pushNotification.cfg"):
			config.read("/etc/pushNotification.cfg")
		else:
			return ""
	return config.get("API","key")


def push(msg,topic,CUSTOM_API_KEY=""):
	API_KEY=getAPIKey()
	if CUSTOM_API_KEY!="":
		API_KEY=CUSTOM_API_KEY
	if API_KEY=="":
		raise NoAPIKey("You have to set the API KEY at /etc/pushNotification.cfg or ~/.pushNotification.cfg")
	jGcmData={}
	jData={}
	jData["message"]= msg
	jGcmData["to"]= "/topics/"+topic
#	if len(sys.argv) > 2: jGcmData["to"]="/topics/"+sys.argv[2].strip()
	jGcmData["data"]= jData
	data=simplejson.dumps(jGcmData)

	req=Request("http://android.googleapis.com/gcm/send",data)
	req.add_header("Authorization","key=" + API_KEY)
	req.add_header("Content-Type", "application/json");
	try:
		response = urlopen(req)
	except:
		req=Request("https://android.googleapis.com/gcm/send",data.encode('utf8'))
		req.add_header("Authorization","key=" + API_KEY)
		req.add_header("Content-Type", "application/json");
		response= urllib.request.urlopen(req)
	return  response.read()

if __name__=="__main__":
	if sys.argv[1]=="--help" or sys.argv[1]=="-h":
		print ("""This script sends notifications to Android Phones.
python pushNotification msg topic
""")
		exit(0)
	msg=sys.argv[1]
	topic=sys.argv[2]
	print (push(msg,topic))
	print ("Check your device/emulator for notification.")
