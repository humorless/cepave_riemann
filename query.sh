#/bin/bash

echo QueryString: tagged "hostgroup_OWL" and service = "HOSTGROUP_INFO"
riemann-client query 'tagged "hostgroup_OWL" and service = "HOSTGROUP_INFO"'

