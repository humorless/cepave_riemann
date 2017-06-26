#!/bin/bash

riemann-client send -s "service.lvs.httping.vip" -m 1 -h hostAA
