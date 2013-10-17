<!doctype html>
<html>
	<head>
		<!--  This meta line should get ignored, ripped this out from core, which has a master page ... -->
	  	<meta name="layout" content="bootstrap"/>
	  	<title>ECK SetManager - Test Page</title>
	</head>

	<body>
    	<div class="row-fluid">
			<section id="main">
		
		  		<div class="hero-unit row">
		    		<div class="page-header span12">
		      			<h1>Test Page</h1>
		    		</div>
		  		</div>
		  
			  	<div class="row">
			     	<div class="span12">
			       		<form id="testForm" name="testForm" action="update" method="POST" enctype="multipart/form-data">
			           		<table>
			               		<tr>
			                   		<th align="right">CMS ID: </th>
			                   		<td><g:field type="text" name="recordId"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">LIDO file: </th>
			                   		<td><g:field type="file" name="record"/></td>
			               		</tr>
			               		<tr>
			                   		<th align="right">LIDO Zip file: </th>
			                   		<td><g:field type="file" name="records"/></td>
			               		</tr>
			               		<tr>
			               			<th align="right">Delete All in set: </th>
			               			<td><g:checkBox name="deleteAll"/></td>
			               		</tr>
			               		<tr>
			               			<th align="right">Records to Delete (comma separated): </th>
			               			<td><g:textField name="delete"/></td>
			               		</tr>
			               		<tr>
			               			<th align="right">Number of history items (status only): </th>
			               			<td><g:textField name="historyItems"/></td>
			               		</tr>
			               		<tr>
			                   		<td colspan="2">
			                   			<div class="btn btn-primary">
			                       			<g:field type="button"  name="testCommit"     value="Commit"/>
			                       			<g:field type="button"  name="testList"       value="List Working Set"/>
			                       			<g:field type="button"  name="testPreview"    value="Preview"/>
			                       			<g:field type="button"  name="testRecord"     value="Record"/>
			                       			<g:field type="button"  name="testStatistics" value="Statistics"/>
			                       			<g:field type="button"  name="testStatus"     value="Status"/>
				                   			<g:field type="button"  name="testUpdate"     value="Update"/>
			                       			<g:field type="button"  name="testValidation" value="Validation Errors"/>
			                       			<g:field type="button"  name="testOAI"        value="LIDO OAI"/>
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
	        	$("#testForm").attr("action", action);
	        	$("#testForm").submit();
	            return false;
        	}
        	
	    	$("#testCommit").click(function() {
	        	return(performAction("commit"));
	    	});
	
	    	$("#testList").click(function() {
	        	return(performAction("list"));
	    	});
	
	    	$("#testPreview").click(function() {
	        	return(performAction("preview"));
	    	});
	
	    	$("#testRecord").click(function() {
	        	return(performAction("record"));
	    	});
	
        	$("#testStatistics").click(function() {
	        	return(performAction("statistics"));
        	});
    
        	$("#testStatus").click(function() {
	        	return(performAction("status"));
        	});
    
        	$("#testUpdate").click(function() {
	        	return(performAction("update"));
        	});
    
        	$("#testValidation").click(function() {
	        	return(performAction("validate"));
        	});

        	$("#testOAI").click(function() {
            	// We need to mangle the url ...
            	// We start off with
	        	// http://host/ECKSetManager/Set/<Provider>/<Set>/test
	        	// and need to change it to
	        	// http://host/ECKSetManager/Oai/<Provider>?verb=listRecords&metadataPrefix=LIDO&set=<Set>
            	var pathArray = window.location.pathname.split( '/' );
	        	var oaiURL = window.location.protocol + "//" + window.location.host;
	        	for (i = 0; i < pathArray.length; i++) {
		        	if (pathArray[i] == "Set") {
			        	oaiURL += "Oai/" + pathArray[i + 1] + "?verb=listRecords&metadataPrefix=LIDO&set=" + pathArray[i + 2];
			        	break;
			        } else {
			        	oaiURL += pathArray[i] + "/";
				    } 
		        }
		        window.location = oaiURL;
		        return(false);
        	});
        </script>    
  	</body>
</html>
