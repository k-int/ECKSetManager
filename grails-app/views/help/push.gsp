<!doctype html>
<html>
  	<head>
    	<meta name="layout" content="bootstrap"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'index.css')}" type="text/css">
    	<title>ECKSetManager - Data Push</title>
  	</head>

  	<body>
    	<div class="row-fluid">

	      	<section id="main">
	
	        	<div class="hero-unit row">
	          		<div class="page-header span12">
	            		<h1>Set Management - Data Push</h1>
	          		</div>
	        	</div>
	        
	        	<div class="row">
	          		<div class="span12">
	          			<h4>Description</h4>
	          			<p> The Data Push action performs the following task: Retrieves committed live records, and pushes them to
                        CultureGrid SWORD server.</p>

	            		<h4>Parameters</h4>
						<p>The provider set and the provider</p>
	            
	            		<h4>Responses</h4>
	            		<p> The response is in JSON, and shows the number of successful and failed deposits, and a list of errors.</p>
	                    <pre id="failedDeposit" name = "failedDeposit"></pre>
	            		<h4>Testing</h4>
	            		<p>In order to allow simple testing of the ECK Set Manager interfaces, etc. a test form is available <a href="/ECKSetManager/Set/default/default/test">here</a> which actions data for the default provider using the default set.</p>
	          		</div>
	        	</div>
	      	</section>
    	</div>
    <script src="/ECKSetManager/static/js/json_syntax.js" type="text/javascript"></script>

    <script type="text/javascript">

        $(document).ready(function (){

            var failedDeposit =[{"Illegal argument":"Host name may not be null","Error Messages": [],"Failed Depostis":0,"Successfull deposits":0}];
            $("#failedDeposit").html(syntaxHighlight(failedDeposit));

        });

    </script>
	</body>
</html>
