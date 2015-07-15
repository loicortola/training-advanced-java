var httpClient = require('./httpClient');

function loop(promiseFn, uuid) {
  return promiseFn(uuid).catch(function(uuid, result) {
    console.log("Will retry");
    setTimeout(function() {
      return new loop(promiseFn, uuid);
    }, 1000);
    
  });
}
// Bot Scenario
httpClient.init().then(function (result) {
  console.log(result.uuid);
  return httpClient.upload(result.uuid, "archives/file1.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file2.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file3.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file4.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file5.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file6.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file7.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file8.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file9.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file10.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file11.zip");
}).then(function (result) {
  return httpClient.upload(result.archiveUuid, "archives/file12.zip");
})
.then(function (result) {
  return httpClient.process(result.archiveUuid);
}).then(function (result) {
  return new loop(function(uuid) { return httpClient.progress(uuid)}, result.archive.uuid);
});