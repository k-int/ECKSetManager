<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Statistics</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Statistics</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The statistics returns the following details about the set</p>
	          			<ul>
	          				<li> Name</li>
	          				<li> Description</li>
	          				<li> The number items that have been committed</li>
	          				<li> The number of items that have not been committed</li>
	          			</ul>
	            		<h4>Parameters</h4>
						<p>There are no parameters</p>
	            
	            		<h4>Response</h4>
	            		<p>The response is in json</p>
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
		
		    	var successData = {"code": "first","description": "Auto generated with code: first","created": "2013-04-10T09:14:34Z","workingSet": {"status": "Committed","numberOfRecords": 4,"numberOfRecordsValid": 0,"numberOfRecordsAwaitingValidation": 4,"numberOfRecordsValidationErrors": 0,"numberOfRecordsDeleted": 0},"liveSet": {"status": "Committed","dateCommitted": "2013-04-17T14:52:53Z","numberOfRecords": 2},"queuedActions": [{"action": "Update","queued": "2013-04-17T14:53:02Z","contentType": "text/xml","recordsToBeDeleted": null,"deleteAll": false},{"action": "ConvertToEDM","queued": "2013-04-17T14:53:02Z","contentType": null,"recordsToBeDeleted": null,"deleteAll": false},{"action": "Validate","queued": "2013-04-17T14:53:02Z","contentType": null,"recordsToBeDeleted": null,"deleteAll": false},{"action": "Commit","queued": "2013-04-17T14:53:02Z","contentType": null,"recordsToBeDeleted": null,"deleteAll": false}],"history": [{"action": "Commit","when": "2013-04-17T14:52:53Z","numberOfRecords": 0,"duration": 140},{"action": "Update","when": "2013-04-17T14:52:52Z","numberOfRecords": 1,"duration": 203},{"action": "Update","when": "2013-04-17T14:17:43Z","numberOfRecords": 1,"duration": 235}]};
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
