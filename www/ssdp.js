var argscheck = require('cordova/argscheck')
var exec = require('cordova/exec')

var ssdpExport = {}

ssdpExport.search = (successCallback, errorCallback) => {
  //argscheck.checkArgs('fFO', 'Camera.getPicture', arguments)
  exec(successCallback, errorCallback, 'SSDP', 'search', [])
}

ssdpExport.listen = (successCallback, errorCallback) => {
  exec(successCallback, errorCallback, 'SSDP', 'listen', [])
}

module.exports = ssdpExport