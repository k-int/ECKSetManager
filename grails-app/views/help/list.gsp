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
						<p>The list action accepts the following parameters that filters the records that are returned
						</p>
						<table class="parameters">
							<tr>
								<th>Parameter</th>
								<th align="left">Meaning</th>
								<th align="left">Valid Values</th>
							</tr>
							<tr>
								<td valign="top">status</td>
								<td valign="top">Filters the results so that only records with the specified value are returned</td>
								<td>
									<table class="noBorder">
										<tr>
											<td>all</td>
											<td>No filtering applied  (default)</td>
										</tr>
										<tr>
											<td>deleted</td>
											<td>Only return the deleted records</td>
										</tr>
										<tr>
											<td>error</td>
											<td>Only return records that have validation errors</td>
										</tr>
										<tr>
											<td>pending</td>
											<td>Only return records that are awaiting validation</td>
										</tr>
										<tr>
											<td>valid</td>
											<td>Only return records that have passed validation</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td valign="top">live</td>
								<td valign="top">Only returns records from the live set</td>
								<td>
									<table class="noBorder">
										<tr>
											<td>no</td>
											<td>returns records from the working set (default)</td>
										</tr>
										<tr>
											<td>yes</td>
											<td>Only returns records from the live set</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>	            
	            		<h4>Response</h4>
	            		<p>The response is in json and will be an array of the brief records</p>
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
		
		    	var successData = [{"cmsId": "00154983","persistentId": null,"lastUpdated": "2013-04-16T10:18:42Z","deleted": false,"validationStatus": "NotChecked"},{"cmsId": "20344012","persistentId": null,"lastUpdated": "2013-04-16T10:18:41Z","deleted": false,"validationStatus": "Error"},{"cmsId": "20344012,T,001","persistentId": null,"lastUpdated": "2013-04-16T20:45:39Z","deleted": false,"validationStatus": "NotChecked"},{"cmsId": "20344012,T,002","persistentId": null,"lastUpdated": "2013-04-16T22:08:56Z","deleted": false,"validationStatus": "NotChecked"}];
		        $("#successResponse").html(syntaxHighlight(successData));
		        
		    });
	    
    	</script>
	</body>
</html>
