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

1. HOSTGROUP_INFO event describes hostgroup/host as :tags/:host field
```
=> { :service "HOSTGROUP_INFO",
     :tags ["hostgroup1", "hostgroup2"]
     :host "owl-docker" }
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

2. Query only the index which :service is HOSTGROUP_INFO and :tags array contains 'owl-group'
   ```
   riemann-client query 'tagged "owl-group" and service = "HOSTGROUP_INFO"'

   ```
   or simply
   ```
   ./query.sh
   ```

### Use [riemann-tools](https://github.com/riemann/riemann-tools) to inject metrics

1. Use riemann-net to select only `eth0`, `eth1` interfaces.
   ```
   riemann-net -n eth0 eth1
   ```

2. Set riemann.config to catch only `bytes`, `packets` fields
```
   (streams
     (where* (fn [e]
               (re-matches #".*(bytes|packets)" (:service e)))
             prn))
```

3. Sample logs
```
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx bytes", :state "ok", :description nil, :metric 96.0, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx packets", :state "ok", :description nil, :metric 1.6, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx bytes", :state "ok", :description nil, :metric 148.8, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx packets", :state "ok", :description nil, :metric 1.6, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth1 rx bytes", :state "ok", :description nil, :metric 0.0, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth1 rx packets", :state "ok", :description nil, :metric 0.0, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth1 tx bytes", :state "ok", :description nil, :metric 0.0, :tags nil, :time 1497866517, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth1 tx packets", :state "ok", :description nil, :metric 0.0, :tags nil, :time 1497866517, :ttl 10.0}
```
