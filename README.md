This is a simple python library that can send notification to Android Phone using Google Cloud Messaging (GCM).

Usage:
	1. Download and install pushNotification library
	```sh
	sudo python setup.py install
	```
	1. Download and install the [https://github.com/mkalioby/Python_Notifications/blob/master/Applications/Android/Notifier.apk](Android APK) 
	2. Open Application and enter topics to listen on (seprated by ,) e.g warning,mkalioby
	3. in your python code, write
	```python
	import pushNotification
	pushNotification.push(MESSAGE,TOPIC)
	```
	
