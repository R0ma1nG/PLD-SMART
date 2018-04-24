#!/bin/bash

echo "### Installing nescessary packages ###"
apt-get update
apt-get install autossh python3-setuptools python3-dev build-essential -y 
easy_install3 pip
pip3 install --upgrade virtualenv
pip3 install requests
pip3 install psutil

echo -e "\n### Copying SSH tunnel scripts ###"
chmod 744 ./tunnel
cp ./tunnel /etc/init.d/

chmod 744 ./tunnel.py
cp ./tunnel.py /usr/bin/

echo -e "\n### Setup SSH tunnel service ###"
update-rc.d tunnel defaults
service tunnel start
/bin/systemctl daemon-reload
service tunnel restart

echo -e "Done."

