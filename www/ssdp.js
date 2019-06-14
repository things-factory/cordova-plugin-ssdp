var argscheck = require('cordova/argscheck')
var exec = require('cordova/exec')

var ssdpExport = {}

ssdpExport.search = (st, successCallback, errorCallback) => {
  //argscheck.checkArgs('fFO', 'Camera.getPicture', arguments)
  exec(successCallback, errorCallback, 'SSDP', 'search', [st])
}

ssdpExport.listen = (st, successCallback, errorCallback) => {
  exec(successCallback, errorCallback, 'SSDP', 'listen', [st])
}

module.exports = ssdpExport