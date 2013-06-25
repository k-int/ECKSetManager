<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Record</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Record</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The record action returns the xml file that was posted to the set</p>
	            		<h4>Parameters</h4>
						<p>The recodId part of the url path needs to be specified or if it contains characters that are not valid for a url path then the recordId can be specified as a parameter, the record identifier is the identifier of the record from the CMS</p>
	            
	            		<h4>Responses</h4>
	            		<p>The response will be the record that was supplied to us. If the record was deleted or no record with that identifier is found then the http response 404 is returned</p>
	            
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK import interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
	</body>
</html>
