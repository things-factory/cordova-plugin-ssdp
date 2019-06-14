# cordova-plugin-ssdp

## `listen`:
`ssdp.listen(searchTarget, successCallback, errorCallback)`
ex:
```javascript
  ssdp.listen('urn:domain-name:device:deviceType:ver', (message1, message2) => {
    console.log('listen success:', message1, message2);
  }, (error) => {
    console.warn('listen error:', error);
  });
```

## `search`: 
`ssdp.search(searchTarget, successCallback, errorCallback)`
ex:
```javascript
  ssdp.search('urn:domain-name:device:deviceType:ver', (message) => {
    try {
      let response = JSON.parse(message);
      console.log(response)
    } catch (e) {
      console.warn(e);
    }
  }, (error) => {
    console.log('search error:', error);
  });
```

## Supported Platforms
- Android
