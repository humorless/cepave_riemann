#!/bin/bash

riemann-client send -s "service.lvs.httping.vip" -m 1 -h virtual-cnc-61.133.127.123
sleep 1
riemann-client send -s "service.lvs.httping.vip" -m 0 -h virtual-cnc-61.133.127.123
