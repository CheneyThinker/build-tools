$('.upload-hook').click(function() {
  $(".file-hook").click();
})
function previewImage(imgFile) {
	new ImageBase64(imgFile, $('.img-list'));
}