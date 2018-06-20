$.base64.utf8encode = true

function postAndGet(type, handler, data, success, error) {
  $.ajax({
    url : 'http://192.30.255.132:9527/CheneyThinker/' + handler,
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

function post(handler, data, success, error) {
  postAndGet('POST', handler, data, success, error)
}

function postJson(handler, data, success, error) {
  post(handler, {data: JSON.stringify(data)}, success, error)
}

function postBase64Json(handler, data, success, error) {
  post(handler, {data: $.base64.btoa(JSON.stringify(data))}, success, error)
}

function get(handler, data, success, error) {
  postAndGet('GET', handler, data, success, error)
}

function getJson(handler, data, success, error) {
  get(handler, {data: JSON.stringify(data)}, success, error)
}

function getBase64Json(handler, data, success, error) {
  get(handler, {data: $.base64.btoa(JSON.stringify(data))}, success, error)
}
