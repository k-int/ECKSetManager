<!doctype html>
<html>
	<head>
		<!--  This meta line should get ignored, ripped this out from core, which has a master page ... -->
	  	<meta name="layout" content="bootstrap"/>
	  	<title>ECK SetManager - Test Edit Page</title>
	</head>

	<body>
    	<div class="row-fluid">
			<section id="main">
		
		  		<div class="hero-unit row">
		    		<div class="page-header span12">
		      			<h1>Test Edit Page</h1>
		    		</div>
		  		</div>
		  
			  	<div class="row">
			     	<div class="span12">
			       		<form id="testEditForm" name="testEditForm" action="update" method="POST" enctype="multipart/form-data">
			           		<table>
			               		<tr>
			                   		<th align="right">Collection Europeana Id: </th>
			                   		<td><g:field type="text" name="europeanaId" value="${set.provider.europeanaId}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Provider Description: </th>
			                   		<td><g:field type="text" name="providerDescription" value="${set.provider.description}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Push Collection Identifier: </th>
			                   		<td><g:field type="text" name="collectionId" value="${set.collectionId}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Push Password: </th>
			                   		<td><g:field type="text" name="password"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Push Provider Ientifier: </th>
			                   		<td><g:field type="text" name="providerId" value="${set.provider.onBehalf}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Push Username: </th>
			                   		<td><g:field type="text" name="username" value="${set.provider.usrname}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Set Description: </th>
			                   		<td><g:field type="text" name="setDescription" value="${set.description}"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">Sword URL: </th>
			                   		<td><g:field type="text" name="swordURL" value="${set.provider.swordURL}"/></td>
			               		</tr>
			               		<tr>
			                   		<td colspan="2">
			                   			<div class="btn btn-primary">
			                       			<g:field type="button"  name="testEdit" value="Save"/>
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
	        	$("#testEditForm").attr("action", action);
	        	$("#testEditForm").submit();
	            return false;
        	}
        	$("#testEdit").click(function(){
                return (performAction("edit"));
            });

    	</script>

  	</body>
</html>
