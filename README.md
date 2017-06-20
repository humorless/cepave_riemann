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
### Alternative to riemann-tools: [Goshin!](https://github.com/ippontech/goshin)

1. Goshin mimics riemann-tools metrics gathering and for now covers the following riemann-tools metric :

 *  riemann-health : cpu, memory and load
 *  riemann-net : network usage
 *  riemann-diskstats: disk statistics

2. Debug Goshin:
```
sudo tail -f /var/log/syslog
```
Note that Goshin use udp port to inject metric into riemann

3. Sample logs
```
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx bytes", :state "ok", :description nil, :metric 275.53, :tags ["goshin"], :time 1.49794291635E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx packets", :state "ok", :description nil, :metric 3.2, :tags ["goshin"], :time 1.497942916352E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx errs", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916354E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx drop", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916358E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx frame", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.49794291636E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx compressed", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916363E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 rx multicast", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916365E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx bytes", :state "ok", :description nil, :metric 329.66, :tags ["goshin"], :time 1.49794291637E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx packets", :state "ok", :description nil, :metric 3.3, :tags ["goshin"], :time 1.49794291638E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx errs", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916388E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx drop", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.49794291639E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx fifo", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916398E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx colls", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916401E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx carrier", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916408E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "eth0 tx compressed", :state "ok", :description nil, :metric 0.0, :tags ["goshin"], :time 1.497942916414E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "load", :state "ok", :description "1-minute load average/core is 0.025000", :metric 0.03, :tags ["goshin"], :time 1.49794291642E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "disk /", :state "warning", :description "95% used", :metric 0.95, :tags ["goshin"], :time 1.497942916449E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "memory", :state "ok", :description "used\n\n 6.8  3658 java\n 6.1 23641 java\n 1.1   948 docker\n 0.8  1373 puppet\n 0.3  6612 falcon-agent\n 0.3  4520 docker\n 0.3  3636 exe\n 0.3  3628 exe\n 0.3  3609 exe\n 0.2  3620 exe\n", :metric 0.2, :tags ["goshin"], :time 1.497942916464E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "cpu", :state "ok", :description "user+nice+system\n\n18.0  4783 git-remote-http\n11.0  3658 java\n 0.6  3151 launcher\n 0.6 23641 java\n 0.5   948 docker\n 0.4  6612 falcon-agent\n 0.3  3644 docker-containe\n 0.3  3135 kworker/u4:2\n 0.2  4520 docker\n 0.1  3636 exe\n", :metric 0.05, :tags ["goshin"], :time 1.497942916482E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "cpuwait", :state "ok", :description "user+nice+system\n\n19.0  4783 git-remote-http\n11.0  3658 java\n 0.6  3151 launcher\n 0.6 23641 java\n 0.5   948 docker\n 0.4  6612 falcon-agent\n 0.3  3644 docker-containe\n 0.3  3135 kworker/u4:2\n 0.2  4520 docker\n 0.1  3636 exe\n", :metric 0.0, :tags ["goshin"], :time 1.497942916522E9, :ttl 10.0}
#riemann.codec.Event{:host "owl-docker", :service "load", :state "ok", :description "1-minute load average/core is 0.020000", :metric 0.02, :tags ["goshin"], :time 1.497942926288E9, :ttl 10.0}
```
