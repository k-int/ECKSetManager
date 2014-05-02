<!doctype html>
<html>
	<head>
		<!--  This meta line should get ignored, ripped this out from core, which has a master page ... -->
	  	<meta name="layout" content="bootstrap"/>
	  	<title>ECK SetManager - Test List Page</title>
	</head>

	<body>
    	<div class="row-fluid">
			<section id="main">
		
		  		<div class="hero-unit row">
		    		<div class="page-header span12">
		      			<h1>Test List Page</h1>
		    		</div>
		  		</div>
		  
			  	<div class="row">
			     	<div class="span12">
			       		<form id="testListForm" name="testListForm" action="update" method="POST" enctype="multipart/form-data">
			           		<table>
			               		<tr>
			                   		<th align="right">Status: </th>
			                   		<td><g:select name="status" from="${statusOptions}" optionKey="id" optionValue="description"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Live: </th>
			                   		<td><g:select name="live" from="${liveOptions}" optionKey="id" optionValue="description"/></td>
			               		</tr>
			               		<tr>
			                   		<td colspan="2">
			                   			<div class="btn btn-primary">
			                       			<g:field type="button"  name="testList" value="List"/>
                                        </div>
			                   		</td>
			               		</tr>
			           		</table>
			       		</form>
			     	</div>
		  		</div>
      		</section>

    	</div>

    	<script type="text/javascript">

    		function performAction(action) {
	        	$("#testListForm").attr("action", action);
	        	$("#testListForm").submit();
	            return false;
        	}
        	$("#testList").click(function(){
                return (performAction("List"));
            });

    	</script>

  	</body>
</html>
