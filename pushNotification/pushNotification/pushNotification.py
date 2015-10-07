#! /usr/bin/python
import simplejson,sys,urllib2,urllib
API_KEY="AIzaSyCxY6hx9Zx_0u_ANv8x6wf-e-zAEj1W_78"

def push(msg,topic):
	jGcmData={}
	jData={}
	jData["message"]= msg
	jGcmData["to"]= "/topics/"+topic
#	if len(sys.argv) > 2: jGcmData["to"]="/topics/"+sys.argv[2].strip()
	jGcmData["data"]= jData
	data=simplejson.dumps(jGcmData)

	req=urllib2.Request("https://android.googleapis.com/gcm/send",data)
	req.add_header("Authorization","key=" + API_KEY)
	req.add_header("Content-Type", "application/json");
	response = urllib2.urlopen(req)
	return  response.read()

if __name__=="__main__":
	if sys.argv[1]=="--help" or sys.argv[1]=="-h":
		print """This script sends notifications to Android Phones.
python pushNotification msg topic
"""
		exit(0)
	msg=sys.argv[1]
	topic=sys.argv[2]
	print push(msg,topic)
	print "Check your device/emulator for notification."
