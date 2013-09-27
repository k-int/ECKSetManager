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
							<li> The provider code</li>
							<li> The collection code</li>
	          				<li> Description</li>
							<li> The number of items that have been accepted</li>
							<li> The number of items that have are pending</li>
							<li> The number of items that have been rejected</li>
							<li> The total number of items</li>
	          				<li> The statistics for the most recent europeana data load
	          					<ul>
	          						<li> Set identifier</li>
	          						<li> Name</li>
	          						<li> Description</li>
	          						<li> Status</li>
	          						<li> The number of items accepted</li>
	          						<li> The number of items rejected</li> 
	          					</ul>
	          				</li>
	          			</ul>
	          			<p>Note: The sum of the accepted, pending and rejected may not be equal to the total, as a record maybe counted more than once if it has been committed and subsequently been updated but not committed.</p> 
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
		
		    	var successData = {"providerCode": "DEFAULT", "collectionCode": "DEFAULT", "description": "Auto generated with code: DEFAULT", "accepted": 8, "pending": 2, "rejected": 1, "total": 9, "europeanaMostRecent": { "deletedRecords": null, "status": "Ingestion complete", "description": "Hidden", "identifier": "09405c", "publishedRecords": 1773, "success": true, "name": "09405c_Ag_UK_ELocal_LDGEF"}};
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
