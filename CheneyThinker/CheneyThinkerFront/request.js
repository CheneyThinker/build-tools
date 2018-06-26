$.base64.utf8encode = true

function postAndGet(type, data, success, error) {
  $.ajax({
    url : 'http://192.30.255.132:9527/CheneyThinker/invoke',
    type : type,
    data : data,
    dataType : 'json'
  }).then(function(res) {
    console.log(res)
    if (200 == res.code) {
      success(res.data)
    } else {
      alert(res.msg)
      if (undefined != res.data) {
        error(res.data)
      }
    }
  })
}

function post(data, success, error) {
  postAndGet('POST', data, success, error)
}

function postJson(data, success, error) {
  post({data: JSON.stringify(data)}, success, error)
}

function postBase64Json(data, success, error) {
  post({data: $.base64.btoa(JSON.stringify(data))}, success, error)
}

function get(data, success, error) {
  postAndGet('GET', data, success, error)
}

function getJson(data, success, error) {
  get({data: JSON.stringify(data)}, success, error)
}

function getBase64Json(data, success, error) {
  get({data: $.base64.btoa(JSON.stringify(data))}, success, error)
}
