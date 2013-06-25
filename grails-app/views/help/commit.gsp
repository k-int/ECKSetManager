<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Commit</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Commit</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The commit action schedules the following tasks:</p>
	          			<ul>
	          				<li> Performs an update with any supplied data (same as the Update action)</li>
	          				<li> Converts the LIDO records to EDM</li>
	          				<li> Validates the EDM records</li>
	          				<li> Makes all Validated records live (ie. they are available for harvesting)</li>
	          				<li> Schedules a Post to be initiated</li>
	          			</ul>
	            		<h4>Parameters</h4>
						<p>The commit action accepts posted files, the delete, deleteAll and setDescription parameters</p>
	            
	            		<h4>Responses</h4>
	            		<p>The response will always be a 202 to say the request has been queued for processing, use the Status action to determine when the commit has completed</p>
	            
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK import interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
	</body>
</html>
