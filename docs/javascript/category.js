(function(){

$(function(){
	function search(){
		var val = $("#search").val()
		function match(dom){
			var d_name = dom.find(".project-name").html()
			if(val && !d_name.contains(val)){
				return false
			}
			
			if(!tag){
				return true;
			}
			
			var tags = dom.find(".badge")
			for(var i=0; i<tags.length; i++){
				if(tags[i].innerHTML === tag){
					return true
				}
			}
			
			return false
		}
		
		if(val){
			$("#search-icon").removeClass("fa-search").addClass("fa-close")
		}
		else{
			$("#search-icon").removeClass("fa-close").addClass("fa-search")
		}
		$("#table-projects .row").each(function(){
			if(match($(this))){
				$(this).show()
			}
			else{
				$(this).hide()
			}
		})
	}
	$("#search").on("input", function(e){
		search()
	})
	$("#search-icon").click(function(){
		if($(this).hasClass("fa-close")){
			$("#search").val("")
			search()
		}
	})
	
	var tag = null
	
	$("#tag-all").hide().click(function(){
		tag = null
		$("#tag-search").html($(this).text())
		$(this).hide()
		search()
	})
	
	$(".tag").click(function(){
		var t_name = $(this).find(".tag-name").html()
		tag = t_name
		$("#tag-search").html(t_name)
		$("#tag-all").show()
		search()
	})
})

})();