var request = require('request');
var fs = require('fs');
// Local defaults
var host = 'localhost:8080';
var basePath = '/api';

function InitOptions() {
  return {
    url: 'http://' + host + basePath + '/init',
    headers: {'accept': 'application/json'}
  }
};

function ProcessOptions(uuid) {
  return {
    url: 'http://' + host + basePath + '/process/' + uuid,
    headers: {'accept': 'application/json'},
    method: 'POST'
  }
};

function ProgressOptions(uuid) {
  return {
    url: 'http://' + host + basePath + '/process/state/' + uuid,
    headers: {'accept': 'application/json'}
  }
};

function UploadOptions(uuid, data) {
  return {
    url: 'http://' + host + basePath + '/file/' + uuid,
    method: 'POST',
    headers: {
      'content-type': 'multipart/form-data',    
      'accept': 'application/json'
    },
    formData: data
  }
}

var init = function() {
  console.log("In init");
  return new Promise(function (fulfill, reject) {
    request(new InitOptions(), function(error, response, body) {
      if (response.statusCode == 200) {
        fulfill(JSON.parse(body));
      } else {
        reject(error);
      }
    });
  });
};

var upload = function (uuid, file) {
  console.log("In upload with uuid " + uuid + " and file " + file);
  return new Promise(function (fulfill, reject) {
    var form = {
      "files[]": fs.createReadStream(file)
    };
    
    request(new UploadOptions(uuid, form), function(error, response, body) {
      if (response.statusCode == 200) {
        var result = JSON.parse(body);
        console.log("Upload Success: download available at" + result.downloadUrl);
        fulfill(result);
      } else {
        reject(error);
      }
      
    })
    
  })
  
}

var process = function (uuid) {
  console.log("In process with uuid " + uuid);
  return new Promise(function (fulfill, reject) {
    request(new ProcessOptions(uuid), function(error, response, body) {
      if (response.statusCode == 200) {
        console.log ("Process success: " + body);
        fulfill(JSON.parse(body));
      } else {
        reject(error);
      }
    });
  });
}

var progress = function (uuid) {
  return new Promise(function (fulfill, reject) {
    request(new ProgressOptions(uuid), function(error, response, body) {
      console.log ("Current Progress: " + body);
      if (response.statusCode == 200) {
        fulfill(JSON.parse(body));
      } else {
        reject(uuid, JSON.parse(body));
      }
    });
  });
}

// Interface
this.init = init;
this.upload = upload;
this.process = process;
this.progress = progress;