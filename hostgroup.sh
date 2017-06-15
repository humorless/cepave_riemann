#!/bin/bash


riemann-client send -s "HOSTGROUP_INFO" -m 0 -d "(:host/:tags) => (host/hostgrouop) relationship" -t hostgroup_1 -t hostgroup_AA -l 86400 -h AA
riemann-client send -s "HOSTGROUP_INFO" -m 0 -d "(:host/:tags) => (host/hostgrouop) relationship" -t hostgroup_2 -t hostgroup_BB -l 86400 -h BB
riemann-client send -s "HOSTGROUP_INFO" -m 0 -d "(:host/:tags) => (host/hostgrouop) relationship" -t hostgroup_OWL -t hostgroup_CC -l 86400

