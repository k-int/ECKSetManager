package com.k_int.euinside.setmanager.OAI_PMHServer

import com.k_int.euinside.setmanager.datamodel.Provider
import com.k_int.euinside.setmanager.datamodel.ProviderSet
import com.k_int.euinside.setmanager.datamodel.Record

import java.sql.Timestamp
import java.text.SimpleDateFormat

class ServerService {

    String error_msg  = '';
    String verb = ''
    String identifier = ''
    String metadataPrefix = ''
    String from = ''
    String until = ''
    String set = ''
    String resumptionToken =''
    String errorCode = ''
    String result  = ''
    String provider = ''

    def getOia(params) {
        error_msg  = '';
        verb = ''
        identifier = ''
        metadataPrefix = ''
        from = ''
        until = ''
        set = ''
        resumptionToken =''
        errorCode = ''
        result  = ''
        provider = ''


        provider = params.provider

        if(params.verb){
            verb = params.verb
        }
        if(params.identifier){
            identifier = params.identifier
        }
        if(params.metadataPrefix){
            metadataPrefix = params.metadataPrefix
        }
        else if(params.metaDataPrefix){
            metadataPrefix = params.metadataPrefix
        }
        else if(params.MetaDataPrefix){
            metadataPrefix = params.metadataPrefix
        }
        else if(params.metadataprefix){
            metadataPrefix = params.metadataPrefix
        }
        if(params.from){
            from = params.from
        }
        if(params.until){
            until = params.until
        }
        if(params.set){
            set = params.set
        }
        if(params.resumptionToken){
            resumptionToken = params.resumptionToken
        }
        else if(params.resumptiontoken){
            resumptionToken = params.resumptionToken
        }


        def verbTemp = verb.toLowerCase()
        switch(verbTemp){
           case "getrecord":
               return getRecord()
               break
            case "identify":
                return identify()
                break
            case "listidentifiers":
                return listIdentifiers()
                break
            case "listrecords":
                return listRecords()
                break
            case "listmetadataformats":
                return listMetadataFormats()
                break
            case "listsets":
                return listSets()
                break
        }
        StringBuilder xml_string  = new StringBuilder()

        errorCode = 'badArgument'
        error_msg = 'please remove any invalid arguments, bad verb'

        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if(errorCode){
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        }

        xml_string.append('</OAI-PMH>')

        return xml_string.toString()
    }

    def identify(){

      StringBuilder xml_string = new StringBuilder();

        if(from || until || metadataPrefix || set || resumptionToken || identifier){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments, no arguments allowed'
        }


        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                " <request verb=\"$verb\" ")
                if(identifier){
                    xml_string.append("identifier=\"$identifier\" ")
                }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

       if(errorCode){
           xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
       }
       else{
           xml_string.append(
                ' <Identify>\n' +
                '    <repositoryName>OAI-PHM EUInside repository</repositoryName>\n' +
                '    <baseURL>http://euinside.k-int.com/ECKSetManager/Oai</baseURL>\n' +
                '    <protocolVersion>2.0</protocolVersion>\n' +
                '    <adminEmail>oai_admin@k-int.com</adminEmail>\n' +
                '    <earliestDatestamp>erm</earliestDatestamp>\n' +
                '    <deletedRecord>persistent</deletedRecord>\n' +
                '    <granularity>YYYY-MM-DDThh:mm:ssZ</granularity>\n' +
                '    <description>\n' +
                '      <oai-identifier xmlns="http://www.openarchives.org/OAI/2.0/oai-identifier"\n' +
                '                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                '                      xsi:schemaLocation= "http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd">\n' +
                '        <scheme>oai</scheme>\n' +
                '        <repositoryIdentifier>idemt</repositoryIdentifier>\n' +
                '        <delimiter>:</delimiter>\n' +
                '        <sampleIdentifier>oai:default:1</sampleIdentifier>\n' +
                '      </oai-identifier>\n' +
                '    </description>\n' +
                '  </Identify>\n')

       }

        xml_string.append('</OAI-PMH>')

        return xml_string.toString()

    }

    def listMetadataFormats(){
        def metadata = [:]
        if(from || until || metadataPrefix || set || resumptionToken){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments, Only identifier is allowed'
        }
        if(identifier){


                def split = identifier.split(':')
            if(split.size() == 4){
                if(Record.get(split[3])){
                    def record = Record.get(split[3]);
                    if(record.convertedType == 'EDM' || record.originalType == 'EDM'){
                            metadata['edm_raw']  =     'http://www.europeana.eu/schemas/edm/EDM'
                        }
                    if(record.convertedType == 'LIDO' || record.originalType == 'LIDO'){
                        metadata['lido_raw']  =     'http://www.lido-schema.org'
                    }
                    if(!metadata){
                        errorCode = 'noMetaDataFormats'
                        error_msg = 'no metadata formats are available for the search'
                    }
                }
                else{
                    errorCode = 'idDoesNotExsist'
                    error_msg = 'identifier does not exsist'
                    metadata['temp'] = 'temp'
                }
        }
            else{
                errorCode = 'badArgument'
                error_msg = 'please remove any invalid arguments, identifier is invalid'
            }

        }
        else{
            metadata = [
                    'lido_raw' : 'http://www.lido-schema.org',
                    'edm_raw'  : 'http://www.europeana.eu/schemas/edm/EDM'
            ]
        }


        if(!metadata){

            errorCode = 'noMetaDataFormats'
            error_msg = 'no metadata formats are available for the search'
        }

        StringBuilder xml_string = new StringBuilder();

        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if(errorCode){
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        }
        else{
            xml_string.append("<ListMetadataFormats>\n")

            metadata.each{ k, v ->
                xml_string.append("<metadataFormat>\n" +
                        "<metadataPrefix>$k</metadataPrefix>" +
                        "<metadataPrefix>$v</metadataPrefix>" +
                        "</metadataFormat>\n")
            }

            xml_string.append("</ListMetadataFormats>\n")
        }

        xml_string.append('</OAI-PMH>')

        return xml_string.toString()
    }

    def listSets(){
        def sets = []
        int delivered = 0;
        if(from || until || metadataPrefix || set || identifier){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments, only resumptionToken allowed'
        }
        if(Provider.findByCode(provider)){
            Provider providerObj = Provider.findByCode(provider)
            if(providerObj.sets){
                sets = providerObj.sets
            }
            else{
                errorCode = 'noSetHierarchy'
                error_msg = 'no sets are available for that provider'
            }

        }

        if (resumptionToken){
            delivered = deStructureToken()
        }

        StringBuilder xml_string = new StringBuilder();

        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if(errorCode){
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        }
        else{
            def setOf = sets.toArray()
            xml_string.append("<ListSets>\n")
            while(delivered > 100 || delivered != sets.size()){

                ProviderSet s = setOf.getAt(delivered)

                xml_string.append("<set>\n")
                xml_string.append(
                                 "<setSpec>" + s.code + "</setSpec>" +
                                  "<setDescription>" + s.description + "</setDescription>" )
                xml_string.append("</set>\n")

                delivered++
            }

            xml_string.append("</ListSets>\n")
        }

        if(delivered < set.size()){
            xml_string.append("<resumptionToken>$from:$until:$metadataPrefix:$set:$delivered</resumptionToken>")
        }


        xml_string.append('</OAI-PMH>')




        return xml_string.toString()

    }

    def listIdentifiers(){
        int delivered = 0;
        def records = []
        def providerObj = Provider.findByCode(provider)

        if(identifier){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments'
        }
        if(!metadataPrefix){
            errorCode = 'badArgument'
            error_msg = 'no metadataPrefix defined'
        }
        else{
        if(!set){
            set = 'default'
        }

        if(resumptionToken){
           delivered = deStructureToken()
        }


             providerObj.sets.each{
                     if(it.code == set){
                         it.records.each{
                               if(it.originalType == metadataPrefix || it.convertedType == metadataPrefix){
                                       records.add(it)
                               }
                         }
                        // records = it.records

                     }
                 else{
                         errorCode = 'noSetHierarchy'
                         error_msg = 'the set returned no matches'
                     }
             }
        if(from && until && records){
           def recTemp =[]
           records.each{
               if(dateCompare(it,from)){
                   recTemp.add(it)
               }
           }
           records.removeAll()
               recTemp.each{
                   if(!dateCompare(it,until)){
                      records.add(it)
                   }

               }
           }
        else if(from && records){
            def recTemp =[]
            records.each{
                if(dateCompare(it,from)){
                    recTemp.add(it)
                }
            }
                records = recTemp
            }
        else if(until && records){
            def recTemp =[]
            records.each{
                if(!dateCompare(it,until)){
                    recTemp.add(it)
                }
            }
            records = recTemp
        }
        if(records.empty){
            if(!errorCode){
            errorCode = 'noRecordsMatch'
            error_msg = 'no matching records found'
            }
        }
        }
        StringBuilder xml_string = new StringBuilder();

        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if(errorCode){
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        }
        else{

            def recs = records.toArray()
             xml_string.append("<ListIdentifiers>\n")

                if(delivered){
                int nextDelivered =  delivered + 100
                }
                else{
                    int nextDelivered = 0
                }
                while(delivered > 100 || delivered != recs.size()){
                    xml_string.append("<header>\n")
                    Record rec = recs.getAt(delivered)
                    xml_string.append("<identifier>oai:http://euinside.k-int.com/oai/:$provider:"+ rec.id +"</identifier>\n")
                    xml_string.append("<datestamp>"+getResponseDate()+"</datestamp>\n")
                    xml_string.append("<setSpec>$set</setSpec>")

                    xml_string.append("</header>\n")

                    delivered++
                }


           xml_string.append("</ListIdentifiers>\n")
        }

        if(delivered < set.size() && delivered != 0){
            xml_string.append("<resumptionToken>$from:$until:$metadataPrefix:$set:$delivered</resumptionToken>")
        }

        xml_string.append('</OAI-PMH>')

        return xml_string.toString()
    }


    def listRecords(){
        def records = []
        int delivered = 0;
        def providerObj = Provider.findByCode(provider)

        if(identifier){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments'
        } else {
	        if(!metadataPrefix){
	            errorCode = 'badArgument'
	            error_msg = 'no metadataPrefix defined'
	        }
	        if(!set){
	            set = 'default'
	        }

	        if(resumptionToken){
	            delivered = deStructureToken()
	        }
        }

		def setObject = ProviderSet.findByProviderAndCode(providerObj, set);
        if (setObject) {
            setObject.records.each {
				if(it.live && !it.deleted && (it.convertedType.equals(metadataPrefix) || it.originalType.equals(metadataPrefix))) {
                    records.add(it)
                }
            }
        } else{
            errorCode = 'noSetHierarchy'
            error_msg = 'the set returned no matches'
        }

        if(from && until && records){
            def recTemp =[]
            records.each{
                if(dateCompare(it,from)){
                    recTemp.add(it)
                }
            }
            records.removeAll()
            recTemp.each{
                if(!dateCompare(it,until)){
                    records.add(it)
                }

            }
        }
        else if(from && records){
            def recTemp =[]
            records.each{
                if(dateCompare(it,from)){
                    recTemp.add(it)
                }
            }
            records = recTemp
        }
        else if(until && records){
            def recTemp =[]
            records.each{
                if(!dateCompare(it,until)){
                    recTemp.add(it)
                }
            }
            records = recTemp
        }

        if(!records){
            errorCode = 'noRecordsMatch'
            error_msg = 'no matching records found'
        }
        StringBuilder xml_string = new StringBuilder();

        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if (errorCode) {
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        } else{
            def recs = records.toArray()
            xml_string.append("<ListRecords>\n")

			def deliveredLimit = delivered + 100
            while(delivered < deliveredLimit && delivered != records.size()){

                if(recs.getAt(delivered)){
	                xml_string.append("<record>\n")
	                xml_string.append("<header>\n")
	
	                Record rec = recs.getAt(delivered)
	                xml_string.append("<identifier>oai:http://euinside.k-int.com/oai/:$provider:"+ rec.id +"</identifier>\n")
	                xml_string.append("<datestamp>"+getResponseDate()+"</datestamp>\n")
	                xml_string.append("<setSpec>$set</setSpec>")

	                xml_string.append("</header>\n")
	                xml_string.append("<metadata>\n")
	
	                if (rec.originalType.equals(metadataPrefix)) {
						if(rec.originalData){
							def temp = new String(rec.originalData)
							def temp2 = temp.replaceFirst("<\\?xml .*\\?>",'')
							xml_string.append(temp2)
	                    }
	                } else if (rec.convertedType.equals(metadataPrefix)) {
						if(rec.convertedType){
							def temp = new String(rec.convertedData)
							def temp2 = temp.replaceFirst("<\\?xml .*\\?>",'')
							xml_string.append(temp2)
						}
	                }

	                xml_string.append("</metadata>\n")
	                xml_string.append("</record>\n")
	                delivered++
                }
            }

            xml_string.append("</ListRecords>\n")
        }

        if(delivered > 0 && delivered < records.size()){
            xml_string.append("<resumptionToken>$from:$until:$metadataPrefix:$set:$delivered</resumptionToken>")
        }

        xml_string.append('</OAI-PMH>')


        return xml_string.toString()
    }

    def getRecord(){
        Record rec
        StringBuilder xml_string = new StringBuilder();

        if(from || until || set || resumptionToken){
            errorCode = 'badArgument'
            error_msg = 'please remove any invalid arguments'
        }
        if(identifier){
           def split = identifier.split(':')
            if(split.size() == 4){
            rec = Record.get(split[3])
                if(!rec){
                    errorCode = 'idDoesNotExsist'
                    error_msg = 'identifier does not exist'
                }
            }
            else{
                errorCode = 'badArgument'
                error_msg = 'identifier is invalid'
            }
        }
        else{
            errorCode = 'badArgument'
            error_msg = 'identifier is required'
        }



        xml_string.append(

                '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>' +
                        '<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" \n' +
                        '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                        '         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/\n' +
                        '         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">\n' +
                        '<responseDate>'+ getResponseDate() +'</responseDate>\n' +
                        " <request verb=\"$verb\" ")
        if(identifier){
            xml_string.append("identifier=\"$identifier\" ")
        }
        if(metadataPrefix){
            xml_string.append("metadataPrefix=\"$metadataPrefix\" ")
        }
        if(from){
            xml_string.append("from=\"$from\" ")
        }
        if(until){
            xml_string.append("until=\"$until\" ")
        }
        if(set){
            xml_string.append("set=\"$set\" ")
        }
        if(resumptionToken){
            xml_string.append("resumptionToken=\"$resumptionToken\" ")
        }

        xml_string.append(">http://euinside.k-int.com/ECKSetManager/Oai</request>")

        if(errorCode){
            xml_string.append("  <error code=\"$errorCode\">$error_msg</error>\n")
        }
        else{

                xml_string.append("<GetRecord>\n")

                    xml_string.append("<header>\n")
                    xml_string.append("<identifier>oai:http://euinside.k-int.com/oai/:$provider:"+ rec.persistentId +"</identifier>\n")
                    xml_string.append("<datestamp>"+getResponseDate()+"</datestamp>\n")
                    xml_string.append("<setSpec>$set</setSpec>")

                    xml_string.append("</header>\n")
                    xml_string.append("<metadata>\n")

                    if(metadataPrefix.contains('LIDO')){
                        if(rec.originalType.contains('LIDO')|| metadataPrefix.contains('lido_raw')){
                            def temp = new String(rec.originalData)
                            def temp2 = temp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>",'')
                            xml_string.append(temp2)
                        }
                        else if(rec.convertedType.contains('LIDO')|| metadataPrefix.contains('lido_raw')){
                            def temp = new String(rec.convertedType)
                            def temp2 = temp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>",'')
                            xml_string.append(temp2)
                        }
                    }
                    else  if(metadataPrefix.contains('EDM')|| metadataPrefix.contains('edm_raw')){
                        if(rec.originalType.contains('EDM')){
                            def temp = new String(rec.originalData)
                            def temp2 = temp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>",'')
                            xml_string.append(temp2)
                        }
                        else if(rec.convertedType.contains('EDM')|| metadataPrefix.contains('edm_raw')){
                            def temp = new String(rec.convertedType)
                            def temp2 = temp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>",'')
                            xml_string.append(temp2)
                        }
                    }

                    xml_string.append("</metadata>\n")




                xml_string.append("</GetRecord>\n")

        }

        xml_string.append('</OAI-PMH>')

        return xml_string.toString()


    }



    def getResponseDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Timestamp response_time  = new Timestamp(System.currentTimeMillis());
        return formatter.format(response_time);

    }

  //Token structure is: $from:$until:$metaDataPrefix:$set:$delivered
    def deStructureToken(){

        def split =  resumptionToken.split(':')

        if(split.length == 5){
              from = split[0]
              until = split[1]
              metadataPrefix = split[2]
              set = split[3]

            return(Integer.parseInt(split[4]));
        }
        else{
            errorCode = 'badResumptionToken'
            error_msg = 'this token is invalid or malformed'
        }
        return 0

    }

    def dateCompare(Record record, String str_date){

        Date date = new Date().parse("YYYY-MM-DD'T'hh:mm:ss'Z'", str_date)


        if(date > record.lastUpdated) {
           return true
        }
        else{
            return false
        }
    }


}
