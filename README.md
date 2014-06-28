HTTP-REQUESTS
=============
  JAVA HTTP requests library inspired from python requests uses commons http

### LICENCE

   Apache License
   Version 2.0, January 2004 http://www.apache.org/licenses/


### EXAMPLES 

#### 1. GET

```java
    Response response = Requests.get("http://datoin.com").execute();
    String text = response.getContentAsString();
```

For More examples checkout test cases.
 
