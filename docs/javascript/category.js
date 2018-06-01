(function(){

$(function(){
	function search(){
		var val = $("#search").val()
		if(val){
			$("#table-projects .row").each(function(){
				var name = $(this).find(".project-name").html()
				if(name.contains(val)){
					$(this).show()
				}
				else{
					$(this).hide()
				}
				$("#search-icon").removeClass("fa-search").addClass("fa-close")
			})
		}
		else{
			$("#table-projects .row").show()
			$("#search-icon").removeClass("fa-close").addClass("fa-search")
		}
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
})

})();