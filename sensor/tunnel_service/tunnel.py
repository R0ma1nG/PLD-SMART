#!/usr/bin/python3
# -*- coding: utf-8 -*-
import os
import time
from threading import Timer
import psutil
import requests
import subprocess

ssh_tunnel_cmd = "/usr/bin/autossh -M 0 -f -i /home/pi/.ssh/raspi_tunnel -R 2223:localhost:22 ubuntu@52.47.138.165 -N"

def ssh_tunnel():
    # We first verify that ssh tunnel isn't already running
    for proc in psutil.process_iter():
        if 'ubuntu@52.47.138.165' in ' '.join(proc.cmdline()):
            return
    # try to run SSH tunnel as non-root user
    def set_user():
        os.setgid(1000)
        os.setuid(1000)
    subprocess.run(ssh_tunnel_cmd, shell=True, preexec_fn=set_user)

if __name__ == '__main__':
    while True:
        ssh_tunnel()
        time.sleep(20)

