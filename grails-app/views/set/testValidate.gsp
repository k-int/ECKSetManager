<!doctype html>
<html>
	<head>
		<!--  This meta line should get ignored, ripped this out from core, which has a master page ... -->
	  	<meta name="layout" content="bootstrap"/>
	  	<title>ECK SetManager - Test Validate Page</title>
	</head>

	<body>
    	<div class="row-fluid">
			<section id="main">
		
		  		<div class="hero-unit row">
		    		<div class="page-header span12">
		      			<h1>Test Validate Page</h1>
		    		</div>
		  		</div>
		  
			  	<div class="row">
			     	<div class="span12">
			       		<form id="testValidateForm" name="testValidateForm" action="update" method="POST" enctype="multipart/form-data">
			           		<table>
			               		<tr>
			                   		<th align="right">CMS ID: </th>
			                   		<td><g:field type="text" name="recordId"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Option: </th>
			                   		<td><g:select name="option" from="${options}" optionKey="id" optionValue="description"/></td>
			               		</tr>
			               		<tr>
			                   		<td colspan="2">
			                   			<div class="btn btn-primary">
			                       			<g:field type="button"  name="testValidate" value="Validate"/>
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
	        	$("#testValidateForm").attr("action", action);
	        	$("#testValidateForm").submit();
	            return false;
        	}
        	$("#testValidate").click(function(){
                return (performAction("Validate"));
            });

    	</script>

  	</body>
</html>
