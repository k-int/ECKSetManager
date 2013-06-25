<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Validate</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Validate</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The validate action returns the validation errors for the either the specified recordId or if no recordId is specified it returns the errors for the whole set</p>
	          			<ul>
	          				<li> CMS Identifier</li>
	          				<li> Array of validation errors
		          				<ul>
		          					<li> Error Code</li>
		          					<li> Additional Information</li>
		          				</ul>
		          			</li>
	          			</ul>
	            		<h4>Parameters</h4>
						<p>The parameter recordId if used specifies that we only want the validation errors for that recordId, if it is not specified then we return the errors for the whole set</p>
	            
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
		
		    	var successData = [{"cmsId": "00154983","persistentId": null,"lastUpdated": "2013-04-16T10:18:42Z","errors": [{"errorCode": "err999","additionalInformation": "Big disaster here"}]}];
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
