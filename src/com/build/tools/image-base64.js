(function(w, d) {
  var id = 1;
  w.ImageBase64 = function(imgFile, selector, width, height) {
    var pattern = /(\.*.png$)|(\.*.jpg$)|(\.*.jpeg$)|(\.*.gif$)|(\.*.bmp$)/;
    if (!pattern.test(imgFile.value)) {
      alert("Please Upload png/jpg/jpeg/gif/bmp Format's Image!");
      imgFile.focus();
    } else {
      this.id = id++;
      this.width = width || 200;
      this.height = height || 200;
      $(selector).append("<div class='img-item'><div id='" + this.id + "'><img src='' width='" + this.width + "' height='" + this.height + "'/></div><span class='delete-hook' onclick='deleteImg(this)'>DELETE</span></div>");
      if (document.all) {
        this.ieBase64(imgFile.value, this.id, this.width, this.height);
      } else {
        this.mainBase64(imgFile.files[0], this.id, this.width, this.height);
      }
      this.resetForm(imgFile);
    }
  };
  ImageBase64.prototype = {
    ieBase64: function(file, id, width, height) {
      var xmlHttp, xml_dom, tmpNode, imgBase64Data;
      xmlHttp = new ActiveXObject("MSXML2.XMLHTTP");
      xmlHttp.open("POST", file, false);
      xmlHttp.send("");
      xml_dom = new ActiveXObject("MSXML2.DOMDocument");
      tmpNode = xml_dom.createElement("tmpNode");
      tmpNode.dataType = "bin.base64";
      tmpNode.nodeTypedValue = xmlHttp.responseBody;
      imgBase64Data = "data:image/bmp;base64," + tmpNode.text.replace(/\n/g, "");
      document.getElementById(id).innerHTML = "<img src='" + imgBase64Data + "' width='" + width + "' height='" + height + "'/>";
    },
    mainBase64: function(file, id, width, height) {
      var fileReader, imgBase64Data;
      fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = function () {
        imgBase64Data = this.result;
        document.getElementById(id).innerHTML = "<img src='" + imgBase64Data + "' width='" + width + "' height='" + height + "'/>";
      }
    },
    resetForm: function(imgFile) {
      $(imgFile).parent()[0].reset();
    }
  }
})(window, document);
function deleteImg(img) {
  $(img).parent().remove();
}