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

