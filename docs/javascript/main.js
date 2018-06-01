if(!String.prototype.contains){
	String.prototype.contains = function(s){
		return this.indexOf(s) >= 0
	}
}
$(function(){
	$("[data-url]").each(function(){
		var url = $(this).data("url")
		if(url){
			var a = $('<a></a>')
			a.attr("href", url)
			var target = $(this).data("target")
			if(target){
				a.attr("target", target)
			}
			$("body").append(a)
			$(this).click(function(e){
				if(e.target.nodeName.toLowerCase() == 'a'){
					return
				}
				a[0].click()
			})
		}
	})
})