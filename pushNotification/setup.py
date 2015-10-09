#!/usr/bin/env python

from distutils.core import setup

setup(name='pushNotification',
      version='0.75',
      description='Push Notification to Android Phones using GCM',
      author='Mohamed El-Kalioby',
      author_email='mkalioby@mkalioby.com',
      url='https://github.com/mkalioby/Python_Notifications',
      download_url="https://github.com/mkalioby/Python_Notifications/archive/0.1.tar.gz",
      packages=['pushNotification'],
      keywords = ['android', 'notificatio'],
      data_files=[('/etc/',['pushNotification/pushNotification.cfg'])]
     )
