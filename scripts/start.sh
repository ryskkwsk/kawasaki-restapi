##!/usr/bin/env bash
source ~/.bash_profile
nohup java \
 -Dspring.profiles.active=production \
 -jar /home/ec2-user/kawasaki-restapi/kawasaki-restapi-0.0.1-SNAPSHOT.jar >> /var/log/kawasaki-restapi/nohup.out 2>&1 < /dev/null &