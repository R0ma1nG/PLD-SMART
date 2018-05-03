#!/bin/bash

echo "### Install nescessary packages ###"
apt-get update
apt-get install git open-ssh 
sudo apt-get install python-picamera
sudo apt-get install python-picamera-docs

cd ./ssh_tunnel_service/
./install.sh

cd ../sensor_service/
./install.sh

cd ..

echo -e "\n### Setup Wifi connection (make sur Wifi dongle is connected) ###"
echo -n "Please enter your Wifi SSID: "
read ssid
echo -ne "\nPlease enter your Wifi password: "
read -s password
echo
echo "auto lo
 
iface lo inet loopback
iface eth0 inet dhcp
 
auto wlan0
allow-hotplug wlan0
iface wlan0 inet dhcp
   wpa-scan-ssid 1
   wpa-ap-scan 1
   wpa-key-mgmt WPA-PSK
   wpa-proto RSN WPA
   wpa-pairwise CCMP TKIP
   wpa-group CCMP TKIP
   wpa-ssid "$ssid"
   wpa-psk "$password"

iface default inet dhcp" >> /etc/network/interfaces

echo -e "\n### Install sensor daemon python dependencies ###"
python setup.py install

echo -e "\n### Setup sensor daemon ###"

