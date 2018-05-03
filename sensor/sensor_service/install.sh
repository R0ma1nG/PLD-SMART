#!/bin/bash

echo -e "\n### Copying sensor service scripts ###"
chmod 744 ./sensor
cp ./sensor /etc/init.d/

echo -e "\n### Setup sensor service ###"
update-rc.d sensor defaults
service sensor start
/bin/systemctl daemon-reload
service sensor restart

echo -e "Done."

