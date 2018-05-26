$(function(){
	$("tr").click(function(e){
		if(e.target.nodeName.toLowerCase() == 'a'){
			return
		}
		var url = $(this).data("url")
		if(url){
			location.href = url
		}
	})
})