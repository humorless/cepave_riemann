# Use Riemann to replace Open-Falcon's Judge 

### Documentation
* [Chinese](https://humorless.gitbooks.io/riemann/)

### Prequisted
*  Riemann python client
   ``` 
   sudo pip install riemann-client
   ```
   take a look at [riemann python client](https://github.com/borntyping/python-riemann-client)

### Invoke Riemann

1. Invoke
    ```
    docker-compose up -d riemann
    ```
2. Understand what happening
   ```
    docker logs -f riemann
   ```
3. Use python client to send events
   ```
   riemann-client send -s "net.if.in.bits" -m 6969
   ```
### Reload Riemann config

1. Use network nrepl to connect Riemann
   ```
   lein repl :connect 127.0.0.1:5557
   ```

2. After connect successfully, give reload command
   ```
   (riemann.bin/reload!)
   ```

### Use events to inject hostgroup info Riemann

1. HOSTGROUP_INFO event describes hostgroup/host as :service/:tags field
```
=> { :service "hostgroup2",
     :tags ["owl-docker", "hosta"]
     :description "HOSTGROUP_INFO" }
```

2. Use python client to send events

   ```
   ./hostgroup.sh
   ```
### Query Riemann to show the index

1. Query all the index
   ```
   riemann-client query 'true'
   ```

2. Query only the index related to HOSTGROUP_INFO and certain host named as 'owl-docker'
   ```
   riemann-client query 'tagged "owl-docker" and description = "HOSTGROUP_INFO"'

   ```
   or simply
   ```
   ./query.sh
   ```
