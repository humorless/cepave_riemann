#/bin/bash

# find out all the index that have owl-docker as tag
riemann-client query 'tagged "owl-docker" and description = "HOSTGROUP_INFO"'

