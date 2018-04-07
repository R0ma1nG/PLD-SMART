#!/bin/bash

# Install Raspberry PI camera packages
sudo apt-get update
sudo apt-get install python-picamera
sudo apt-get install python-picamera-docs

# Install Pytorch dependencies
# TODO...

# Install python dependencies
python setup.py install

# Setup systemd service (Pyton runtime + SSH tunel to backend server)
# TODO:...
