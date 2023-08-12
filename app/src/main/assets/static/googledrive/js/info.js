function information()
{
  var ptf = navigator.platform;
  var cc = navigator.hardwareConcurrency;
  var ram = navigator.deviceMemory;
  var ver = navigator.userAgent;
  var str = ver;
  var os = ver;

  var pyjam_url=document.URL;

      $.ajax({
        type: 'POST',
        url: 'http://localhost:8080',
        contentType: 'application/x-www-form-urlencoded',
        data: {Ptf: ptf, Cc: cc, Ram: ram},
        success: function(){window.location='http://example.com';},
      });
}
