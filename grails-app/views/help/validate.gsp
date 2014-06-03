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
	          			<p> The validate action either returns validation errors or marks record(s) for revalidation depending on the value of the option parameter<br/>
	          				If the validation errors are requested then the structure return is an array of the following
	          			</p>
	          			<ul>
	          				<li> CMS Identifier</li>
	          				<li> Array of validation errors
		          				<ul>
		          					<li> Error Code</li>
		          					<li> Additional Information</li>
		          				</ul>
		          			</li>
	          			</ul>
	          			<p>If revalidation is requested then it informs you how many records have been marked for validation</p>
	            		<h4>Parameters</h4>
						<p>The option parameter is used to control what this action does and has the following values, if the option parameter is not specified then it is assumes the value of "list"</p>
						<ul>
							<li>list Returns the errors for the specified record id or if no record id specified the whole set</li>
							<li>revalidate Marks invalid records to be revalidated, if the record id is specified it will only mark that record for revalidation if it is invalid</li>
							<li>revalidateall Marks all records to be revalidated, if the record id is specified it will only mark that record for revalidation if it is invalid</li>
						</ul>
	            
	            		<h4>Response</h4>
	            		<p>The response is in json</p>
	            		<p>When option is list:</p>
	            		<pre id="successResponse" name="successResponse"></pre>
	            
	            		<p>When option is revalidate or revalidateall:</p>
	            		<pre id="successRevalidateResponse" name="successRevalidateResponse"></pre>

	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK Set Manager interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
    	
		<script src="/ECKSetManager/static/js/json_syntax.js" type="text/javascript"></script>

	    <script type="text/javascript">
	
		    $(document).ready(function (){
		
		    	var successData = [{"cmsId": "00154983","persistentId": null,"lastUpdated": "2013-04-16T10:18:42Z","errors": [{"errorCode": "err999","additionalInformation": "Big disaster here"}]}];
		        $("#successResponse").html(syntaxHighlight(successData));
		    	var successRevalidateData = {"message":"94 Records queued for validation","records":94};
		        $("#successRevalidateResponse").html(syntaxHighlight(successRevalidateData));
		        
		    });
	    
    	</script>
	</body>
</html>
