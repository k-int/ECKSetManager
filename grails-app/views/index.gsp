<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Management of Sets and Records</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	            		<h4>Parameters / invocation</h4>
						<p>The url for the module takes the form <b>/Set/&lt;provider&gt;/&lt;setName&gt;/&lt;action&gt;/&lt;recordId&gt;?parameters</b> Where:</p>
						<table class="parameters">
							<tr>
								<th>URL Part</th>
								<th align="left">Description</th>
							</tr>
							<tr>
								<td>provider </td>
								<td>Is the code for the provider of the data (it will be restricted by client IP address as to which machines can provide/request data for a provider) </td>
							</tr>
							<tr>
								<td>setName</td>
								<td>The set that is to be manipulated or information is to be provided for, if the set does not exist, it will be created, for compatibility with iteration 1 the set name of “default” will be created for any data persisted in iteration 1.</td>
							</tr>
							<tr>
								<td>action</td>
								<td>The action to be performed on the set</td>
							</tr>
							<tr>
								<td>recordId</td>
								<td>is the record to be actioned, this is not applicable for all actions and will be ignored if supplied where it is not relevant.</td>
							</tr>
						</table>

						<p>The possible parameters are:</p>
						<table class="parameters">
							<tr>
								<th align="left">Parameter</th>
								<th align="left">Description</th>
							</tr>
							<tr>
								<td>delete</td>
								<td>A comma separated list of records to be deleted from the set</td>
							</tr>
							<tr>	
								<td>deleteAll</td>
								<td>Delete all records from the set first (ie. Recreate the set from the supplied data)	Yes / No</td>
							</tr>
							<tr>	
								<td>historyItems</td>
								<td>Number of items to show in the history section of the Status action</td>
							</tr>
							<tr>
								<td>setDescription</td>
								<td>A description that can be used for the set, if the set is being created</td>
							</tr>
							<tr>
								<td>statisticsDetail</td>
								<td>How much detail to provide in the statistics report for this set, no values are defined for this yet, this will be revisited when we look at what statistics are required and presented</td>
							</tr>	
						</table>

						<p>All posted files where the content type contains zip or xml for the actions Update and Commit will be examined to see if they contain records that need to be added to the set, it is assumed the files will be in LIDO format</p>

	            		
						<p>The possible actions are:</p>
						<ul>
							<li><g:link controller="Help" action="commit">Commit</g:link></li>
							<li><g:link controller="Help" action="list">List</g:link></li>
							<li><g:link controller="Help" action="preview">Preview</g:link></li>
							<li><g:link controller="Help" action="record">Record</g:link></li>
							<li>Statistics</li>
							<li><g:link controller="Help" action="status">Status</g:link></li>
							<li><g:link controller="Help" action="update">Update</g:link></li>
							<li><g:link controller="Help" action="validate">Validate</g:link></li>
						</ul>
								
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK import interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
	</body>
</html>
