#!/bin/bash

# Debug:
# riemann-client -T none send -s "hostgroup1" -m 0 -d "service:hostgroup, tags:host" -t aa -t bb -l 86400 -a 'h1=1' -a 'h2=1' -a h3=0
#

riemann-client send -s "hostgroup1" -m 0 -d "HOSTGROUP_INFO" -t owl-docker -t hostb -l 86400 -a h2=1 -a h3=0
riemann-client send -s "hostgroup2" -m 0 -d "HOSTGROUP_INFO" -t owl-docker -t hosta -l 86400 -a h2=1 -a h3=0
riemann-client send -s "hostgroup3" -m 0 -d "HOSTGROUP_INFO" -t hostd -t hostc -l 86400 -a h2=1 -a h3=0

