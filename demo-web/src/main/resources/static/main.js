(function() {
	var handleUpload = function(file) {
		var $newTile = $("<div class='uploading content-tile paper col-xs-12 col-sm-4 col-md-3'></div>");
		var reader = new FileReader();
		reader.onload = (function(img) {
			return function(e) {
				img.src = e.target.result;
				$newTile.append(img);
				$newTile.insertBefore(".upload-tile");
			};
		})(document.createElement("img"));
		reader.readAsDataURL(file);
	};

	var onFileSelected = function() {
		for(var i=0; i < this.files.length; i++) {
			handleUpload(this.files[i]);
		}
		$(this).val(null);
	};

	$(document).ready(function() {
		$upload = $(".upload-form input[type='file']");

		$(".upload-form a").click(function() {
			$upload.click();
		});
		$upload.change(function() {
			var files = this.files;
			if(files.length > 0) {
				var data = new FormData();
				data.append("content", files[0]);
				$.ajax({
					url: '/api/images',
					data: data,
					cache: false,
					contentType: false,
					processData: false,
					method: 'POST',
				}).then(handleUpload(files[0]));
			}
		});
	});
})();