# Use Riemann to replace Open-Falcon's Judge 

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
