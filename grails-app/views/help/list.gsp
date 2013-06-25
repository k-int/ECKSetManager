<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - List</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - List</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The list action returns the following details about the records in the working set</p>
	          			<ul>
	          				<li> CMS Identifier</li>
	          				<li> Persistent Identifier</li>
	          				<li> When it was last updated (this includes conversion to EDM, Validation and being committed)</li>
	          				<li> Whether this item has been deleted</li>
	          				<li> Its validation status</li>
	          			</ul>
	            		<h4>Parameters</h4>
						<p>There are no parameters that influence the result of this call</p>
	            
	            		<h4>Response</h4>
	            		<p>The response is in json and will be an array of the brief records</p>
	            		<pre id="successResponse" name="successResponse"></pre>
	            
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK import interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
    	
		<script src="/ECKSetManager/static/js/json_syntax.js" type="text/javascript"></script>

	    <script type="text/javascript">
	
		    $(document).ready(function (){
		
		    	var successData = [{"cmsId": "00154983","persistentId": null,"lastUpdated": "2013-04-16T10:18:42Z","deleted": false,"validationStatus": "NotChecked"},{"cmsId": "20344012","persistentId": null,"lastUpdated": "2013-04-16T10:18:41Z","deleted": false,"validationStatus": "Error"},{"cmsId": "20344012,T,001","persistentId": null,"lastUpdated": "2013-04-16T20:45:39Z","deleted": false,"validationStatus": "NotChecked"},{"cmsId": "20344012,T,002","persistentId": null,"lastUpdated": "2013-04-16T22:08:56Z","deleted": false,"validationStatus": "NotChecked"}];
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
