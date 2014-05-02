<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Edit</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Edit</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The edit action allows various details about the provider set to be modified,
	          			    if a parameter is not supplied or is empty then the field is not modified,
	          			     it returns the current values of the fields, except the password field or an error message if there was a problem saving the changes (eg. to much data for a field)</p>
	            		<h4>Parameters</h4>
						<p>The edit action accepts the following parameters
						</p>
						<table class="parameters">
							<tr>
								<th>Parameter</th>
								<th align="left">Meaning</th>
							</tr>
							<tr>
								<td valign="top">europeanaId</td>
								<td valign="top">The id that europeana knows this provider as</td>
							</tr>
							<tr>
								<td valign="top">providerDescription</td>
								<td valign="top">The description to be given to the provider</td>
							</tr>
							<tr>
								<td valign="top">collectionId</td>
								<td valign="top">The collection identifier to be used for the push operation</td>
							</tr>
							<tr>
								<td valign="top">password</td>
								<td valign="top">The password required for the push operation</td>
							</tr>
							<tr>
								<td valign="top">providerId</td>
								<td valign="top">The provider identifier to be used for the push operation</td>
							</tr>
							<tr>
								<td valign="top">username</td>
								<td valign="top">The username required for the push operation</td>
							</tr>
							<tr>
								<td valign="top">setDescription</td>
								<td valign="top">The description to be used for the set</td>
							</tr>
							<tr>
								<td valign="top">swordURL</td>
								<td valign="top">The base sword URL to be used for the push operation</td>
							</tr>
						</table>	            
	            		<h4>Response</h4>
	            		<p>The response is in json and returns the current values of the fields, except the password field</p>
	            		<pre id="successResponse" name="successResponse"></pre>
	            
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK Set Manager interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
    	
		<script src="/ECKSetManager/static/js/json_syntax.js" type="text/javascript"></script>

	    <script type="text/javascript">
	
		    $(document).ready(function (){
		
		    	var successData = {"europeanaId":"20223","provider":"chas","providerDescription":"This be my provider not yours","pushCollection":"Chas1","pushProviderIdentifier":"chas99","pushUsername":"username88","set":"first","setDescription":"This is the first set","swordURL":"swordurl Possibly"};
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
