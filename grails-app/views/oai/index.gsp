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
                <h1>OAI-PMH Server</h1>
            </div>
        </div>

        <div class="row">
            <div class="span12">
                <h4>Parameters / invocation</h4>
                <p>The url for the module takes the form <b>/Oai/&lt;provider&gt;?parameters</b> Where:</p>
                <table class="parameters">
                    <tr>
                        <th>URL Part</th>
                        <th align="left">Description</th>
                    </tr>
                    <tr>
                        <td>provider </td>
                        <td>Is the code for the provider of the data (it will be restricted by client IP address as to which machines can provide/request data for a provider) </td>
                    </tr>
                </table>

                <p>The possible parameters are:</p>
                <table class="parameters">
                    <tr>
                        <th align="left">Parameter</th>
                        <th align="left">Description</th>
                    </tr>
                    <tr>
                        <td>verb</td>
                        <td>This tels the server which method to run, the available methods are: </br> identify, listMetadataFormats, listSets, listIdentifiers, listRecords, getRecord. </br> The parameters for each method are explained below</td>
                    </tr>
                    <tr>
                        <td>identifier</td>
                        <td>This identifies a single record using a unique identifier example: oai:http://oai.com:someProvider:1 </td>
                    </tr>
                    <tr>
                        <td>from</td>
                        <td>This specifies the from date to be applied to the search. The expected format of the date can be obtained my calling the identify method.</td>
                    </tr>
                    <tr>
                        <td>until</td>
                        <td>This specifies the until date to be applied to the search. The expected format of the date can be obtained my calling the identify method.</td>
                    </tr>
                    <tr>
                        <td>metadataPrefix</td>
                        <td>This will specify which metadata types are available. To all the metadata types can be obtained my calling the identify method.</td>
                    </tr>
                    <tr>
                        <td>set</td>
                        <td>This is used to limit the search to a specific set. The default set is "default".</td>
                    </tr>
                    <tr>
                        <td>resumptionToken</td>
                        <td>This is a token given by the server to allow continuation of a search that holds more than 100 results.</td>
                    </tr>
                </table>

                <p>The available methods/verbs are:</p>
                <table class="parameters">
                    <tr>
                        <th align="left">Method/Verb</th>
                        <th align="left">Parameters</th>
                    </tr>
                    <tr>
                        <td>Identify</td>
                        <td>Parameters: </br> NONE.</td>
                    </tr>
                    <tr>
                        <td>listMetadataFormats</td>
                        <td>Parameters: </br> identifier.</td>
                    </tr>
                    <tr>
                        <td>listSets</td>
                        <td>Parameters: </br> resumptionToken.</td>
                    </tr>
                    <tr>
                        <td>listIdentifiers</td>
                        <td>Parameters: </br> from, until, metadataPrefix(required), set, resumptionToken.</td>
                    </tr>
                    <tr>
                        <td>listRecords</td>
                        <td>Parameters: </br> from, until, metadataPrefix(required), set, resumptionToken.</td>
                    </tr>
                    <tr>
                        <td>getRecord</td>
                        <td>Parameters: </br> identifier(required), metadataPrefix(required).</td>
                    </tr>
                </table>

                <p>All searches will return structured XML</p>

            </div>
        </div>
    </section>
</div>
</body>
</html>
