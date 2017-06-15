#!/bin/bash

riemann-client send -s "net.if.in.bits" -m 0 -h AA
riemann-client send -s "net.if.in.bits" -m 10 -h BB
riemann-client send -s "net.if.in.bits" -m 20 -h CC
riemann-client send -s "net.if.in.bits" -m 30 -h DD
riemann-client send -s "net.if.in.bits" -m 40
riemann-client send -s "net.if.in.bits" -m 50 -h AA
riemann-client send -s "net.if.in.bits" -m 60 -h BB
riemann-client send -s "net.if.in.bits" -m 70 -h CC
riemann-client send -s "net.if.in.bits" -m 80 -h DD
riemann-client send -s "net.if.in.bits" -m 90
riemann-client send -s "net.if.in.bits" -m 100 -h RR
riemann-client send -s "net.if.in.bits" -m 110 -h JJ
riemann-client send -s "net.if.in.bits" -m 120 -h YY
