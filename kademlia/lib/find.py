#! /usr/bin/env python
# coding: utf-8

import subprocess
import os

word=raw_input()

for filename in os.listdir("."):
	print filename
	shell_line = "jar tf %s | grep %s" % (filename,word)
	p = subprocess.Popen(shell_line, shell=True)
	p.wait()


#-guice-3.0.jar
