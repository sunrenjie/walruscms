var XT = {};


XT.version = 20071009;


XT.defaultLoadingElementId = null;


XT.defaultLoadingImage = null;


XT.defaultErrorHandler = null;


XT.doAjaxAction = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    if (! clientParams.errorHandler) {
        clientParams.errorHandler = this.defaultErrorHandler;
    }
    
    var ajaxClient = new XT.ajax.Client();
    
    if (clientParams.formName) {
        return ajaxClient.doAjaxAction(eventId, document.forms[clientParams.formName], sourceElement, serverParams, clientParams);
    } else if (clientParams.formId) {
        return ajaxClient.doAjaxAction(eventId, document.getElementById(clientParams.formId), sourceElement, serverParams, clientParams);
    } else {
        return ajaxClient.doAjaxAction(eventId, document.forms[0], sourceElement, serverParams, clientParams);
    }
};


XT.doAjaxSubmit = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    if (! clientParams.errorHandler) {
        clientParams.errorHandler = this.defaultErrorHandler;
    }
    
    var ajaxClient = new XT.ajax.Client();
    
    if (clientParams.formName) {
        return ajaxClient.doAjaxSubmit(eventId, document.forms[clientParams.formName], sourceElement, serverParams, clientParams);
    } else if (clientParams.formId) {
        return ajaxClient.doAjaxSubmit(eventId, document.getElementById(clientParams.formId), sourceElement, serverParams, clientParams);
    } else {
        return ajaxClient.doAjaxSubmit(eventId, document.forms[0], sourceElement, serverParams, clientParams);
    }
};


XT.ajax = {};


XT.ajax.Client = function() {
    
    var ajaxParameter = "ajax-request";
    var eventParameter = "event-id";
    var elementParameter = "source-element";
    var elementIdParameter = "source-element-id";
    var jsonParameters = "json-params";
    
    this.doAjaxAction = function(eventId, sourceForm, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-action";
        var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
        
        var ajaxRequest = new XT.taconite.AjaxRequest(document.URL);
        
        configureRequest(ajaxRequest, clientParams);
        
        ajaxRequest.addFormElements(sourceForm);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
        
        ajaxRequest.sendRequest();
    };
    
    this.doAjaxSubmit = function(eventId, sourceForm, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-submit";
        
        if (clientParams && clientParams.enableUpload && clientParams.enableUpload == true) {
            var queryParameters = prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var iframeRequest = new XT.taconite.IFrameRequest(sourceForm, document.URL, queryParameters);
            
            configureRequest(iframeRequest, clientParams);
            
            iframeRequest.sendRequest();
        } else {
            var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var ajaxRequest = new XT.taconite.AjaxRequest(document.URL);
            
            configureRequest(ajaxRequest, clientParams);
            
            ajaxRequest.addFormElements(sourceForm);
            ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
            ajaxRequest.setUsePOST();
            
            ajaxRequest.sendRequest();
        }
    };
    
    function prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams) {
        var qs = "";
        if (ajaxRequestType) {
            qs = ajaxParameter + "=" + ajaxRequestType;
        }
        if (eventId) {
            qs = qs + "&" + eventParameter + "=" + eventId;
        }
        if (sourceElement) {
            if (sourceElement.name != null) {
                qs = qs + "&" + elementParameter + "=" + sourceElement.name;
            }
            if (sourceElement.id != null) {
                qs = qs + "&" + elementIdParameter + "=" + sourceElement.id;
            }
        }
        if (serverParams) {
            qs = qs + "&" + jsonParameters + "=" + encodeURIComponent(JSON.stringify(serverParams));
        }
        return qs;
    };
    
    function prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams) {
        var params = {};
        params[ajaxParameter] = ajaxRequestType;
        params[eventParameter] = eventId;
        if (sourceElement) {
            if (sourceElement.name != null) {
                params[elementParameter] = sourceElement.name;
            }
            if (sourceElement.id != null) {
                params[elementIdParameter] = sourceElement.id;
            }
        }
        if (serverParams) {
            params[jsonParameters] = JSON.stringify(serverParams);
        }
        return params;
    };
    
    function configureRequest(request, clientParams) {
        if (! clientParams) {
            return;
        }
        
        if (clientParams.loadingElementId != null && clientParams.loadingImage != null) {
            request.loadingElementId = clientParams.loadingElementId; 
            request.loadingImage = clientParams.loadingImage;
            request.setPreRequest(showLoadingSign);
            request.setPostRequest(hideLoadingSign);
        }
        
        if (clientParams.errorHandler != null) {
            request.setErrorHandler(clientParams.errorHandler);
        }
    };
    
    function showLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null) {
            var img = document.createElement("img");
            img.setAttribute("src", request.loadingImage);
            targetEl.appendChild(img);
        }
    };
    
    function hideLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null && targetEl.childNodes.length > 0) {
            targetEl.removeChild(targetEl.childNodes[0]);
        }
    };
};
XT.taconite = {};


XT.taconite.isIE = document.uniqueID;


XT.taconite.BaseRequest = function() {
    
    var preRequest = null;
    
    var postRequest = null;
    
    var errorHandler = null;
    
    this.setPreRequest = function(func) {
        preRequest = func;
    };
    
    this.setPostRequest = function(func) {
        postRequest = func;
    };
    
    this.getPreRequest = function() {
        return preRequest;
    };
    
    this.getPostRequest = function() {
        return postRequest;
    };
    
    this.setErrorHandler = function(func){
        errorHandler = func;
    };
    
    this.getErrorHandler = function() {
        return errorHandler;
    };
};


XT.taconite.AjaxRequest = function(url) {
    
    var ajaxRequest = this;
    
    var xmlHttp = createXMLHttpRequest();
    
    var queryString = "";
    
    var requestURL = url;
    
    var method = "GET";
    
    var async = true;
    
    this.getXMLHttpRequestObject = function() {
        return xmlHttp;
    };
    
    this.setUsePOST = function() {
        method = "POST";
    };
    
    this.setUseGET = function() {
        method = "GET";
    };
    
    this.setQueryString = function(qs) {
        queryString = qs;
    };
    
    this.getQueryString = function() {
        return queryString;
    };
    
    this.setAsync = function(asyncBoolean){
        async = asyncBoolean;
    };
    
    this.addFormElements = function(form) {
        var formElements = new Array();
        if (form != null) {
            if (typeof form == "string") {
                var el = document.getElementById(form);
                if (el != null) {
                    formElements = el.elements;
                }
            } else {
                formElements = form.elements;
            }
        }
        var values = toQueryString(formElements);
        accumulateQueryString(values);
    };
    
    this.addNameValuePair = function(name, value) {
        var nameValuePair = name + "=" + encodeURIComponent(value);
        accumulateQueryString(nameValuePair);
    };
    
    this.addNamedFormElementsByFormID = function() {
        var elementName = "";
        var namedElements = null;
        
        for(var i = 1; i < arguments.length; i++) {
            elementName = arguments[i];
            namedElements = document.getElementsByName(elementName);
            var arNamedElements = new Array();
            for(j = 0; j < namedElements.length; j++) {
                if(namedElements[j].form  && namedElements[j].form.getAttribute("id") == arguments[0]){
                    arNamedElements.push(namedElements[j]);				
                }
            }
            if(arNamedElements.length > 0){
                elementValues = toQueryString(arNamedElements);
                accumulateQueryString(elementValues);
            }
        }
    };
    
    this.addNamedFormElements = function() {
        var elementName = "";
        var namedElements = null;
        
        for(var i = 0; i < arguments.length; i++) {
            elementName = arguments[i];
            namedElements = document.getElementsByName(elementName);
            
            elementValues = toQueryString(namedElements);
            
            accumulateQueryString(elementValues);
        }
    };
    
    this.addFormElementsById = function() {
        var id = "";
        var element = null;
        var elements = new Array();
        
        for(var h = 0; h < arguments.length; h++) {
            element = document.getElementById(arguments[h]);
            if(element != null) {
                elements[h] = element;
            }
        }
        
        elementValues = toQueryString(elements);
        accumulateQueryString(elementValues);
    };
    
    this.sendRequest = function() {
        if(this.getPreRequest()) {
            var preRequest = this.getPreRequest();
            preRequest(this);
        }
        
        if(async) {
            xmlHttp.onreadystatechange = handleStateChange;
        }
        
        if(requestURL.indexOf("?") > 0) {
            requestURL = requestURL + "&ts=" + new Date().getTime();
        }
        else {
            requestURL = requestURL + "?ts=" + new Date().getTime();
        }
        
        try {
            if(method == "GET") {
                if(queryString.length > 0) {
                    requestURL = requestURL + "&" + queryString;
                }
                xmlHttp.open(method, requestURL, async);
                xmlHttp.send(null);
            }
            else {
                xmlHttp.open(method, requestURL, async);
                //Fix a bug in Firefox when posting
                try {
                    if (xmlHttp.overrideMimeType) {
                        xmlHttp.setRequestHeader("Connection", "close");//set header after open
                    }			
                }
                catch(e) {
                    // Do nothing
                }
                xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
                xmlHttp.send(queryString);
            }
        }
        catch(exception) {
            if(this.getErrorHandler()) {
                var errorHandler = this.getErrorHandler();
                errorHandler(this, exception);
            }
            else {
                throw exception;
            }
        }
        
        if(!async) {  //synchronous request, handle the state change
            handleStateChange();
        }
    };
    
    function handleStateChange() {
        if(ajaxRequest.getXMLHttpRequestObject().readyState != 4) {
            return;
        }
        if(ajaxRequest.getXMLHttpRequestObject().status != 200) {
            var errorHandler = ajaxRequest.getErrorHandler();
            errorHandler(ajaxRequest);
            return;
        }
        try {
            //handle null responseXML
            var nodes = null;
            if (ajaxRequest.getXMLHttpRequestObject().responseXML != null) {
                nodes = ajaxRequest.getXMLHttpRequestObject().responseXML.documentElement.childNodes;
            }
            else {
                nodes = new Array();
            }
            
            var parser = new XT.taconite.XhtmlToDOMParser();
            for(var i = 0; i < nodes.length; i++) {
                if(nodes[i].nodeType != 1) {
                    continue;
                }
                parser.parseXhtml(nodes[i]);
            }
        }
        catch(exception) {
            if(ajaxRequest.getErrorHandler()) {
                var errorHandler = ajaxRequest.getErrorHandler();
                errorHandler(ajaxRequest, exception);
            }
            else {
                throw exception;
            }
        }
        finally {
            try {
                if(ajaxRequest.getPostRequest()) {
                    var postRequest = ajaxRequest.getPostRequest();
                    postRequest(ajaxRequest);
                }
            }
            catch(exception) {
                if(ajaxRequest.getErrorHandler()) {
                    var errorHandler = ajaxRequest.getErrorHandler();
                    errorHandler(ajaxRequest, exception);
                }
            }
        }
    };
    
    function createXMLHttpRequest() {
        var req = false;
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
        }
        else if (window.ActiveXObject) {
            try {
                req = new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch(e) {
                try {
                    req = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch(e) {
                    req = false;
                }
            }
        }
        return req;
    };
    
    function accumulateQueryString(newValues) {
        if(queryString == "") {
            queryString = newValues; 
        }
        else {
            queryString = queryString + "&" +  newValues;
        }
    };
    
    function toQueryString(elements) {
        var node = null;
        var qs = "";
        var name = "";
        
        var tempString = "";
        for(var i = 0; i < elements.length; i++) {
            tempString = "";
            node = elements[i];
            
            name = node.getAttribute("name");
            //use id if name is null
            if (!name) {
                name = node.getAttribute("id");
            }
            name = encodeURIComponent(name);
            
            if(node.tagName.toLowerCase() == "input") {
                if(node.type.toLowerCase() == "radio" || node.type.toLowerCase() == "checkbox") {
                    if(node.checked) {
                        tempString = name + "=" + encodeURIComponent(node.value);
                    }
                }
                
                if(node.type.toLowerCase() == "text" || node.type.toLowerCase() == "hidden" || node.type.toLowerCase() == "password") {
                    tempString = name + "=" + encodeURIComponent(node.value);
                }
            }
            else if(node.tagName.toLowerCase() == "select") {
                tempString = getSelectedOptions(node);
            }
            
            else if(node.tagName.toLowerCase() == "textarea") {
                tempString = name + "=" + encodeURIComponent(node.value);
            }
            
            if(tempString != "") {
                if(qs == "") {
                    qs = tempString;
                }
                else {
                    qs = qs + "&" + tempString;
                }
            }
            
        }
        
        return qs;
    };
    
    function getSelectedOptions(select) {
        var options = select.options;
        var option = null;
        var qs = "";
        var tempString = "";
        
        for(var x = 0; x < options.length; x++) {
            tempString = "";
            option = options[x];
            
            if(option.selected) {
                tempString = encodeURIComponent(select.name) + "=" + encodeURIComponent(option.value);
            }
            
            if(tempString != "") {
                if(qs == "") {
                    qs = tempString;
                }
                else {
                    qs = qs + "&" + tempString;
                }
            }
        }
        
        return qs;
    };
};


XT.taconite.AjaxRequest.prototype = new XT.taconite.BaseRequest();


XT.taconite.IFrameRequest = function(form, url, parameters) {
    
    var iFrameRequest = this;
    
    var requestForm = form;
    
    var requestURL = url;
    
    var requestParams = parameters;
    
    var containerId = "CONTAINER-" + Math.floor(Math.random() * 99999);
    
    var frameId = "FRAME-" + Math.floor(Math.random() * 99999);
    
    var container = null;
    
    var frame = null;
    
    init();
    
    this.sendRequest = function() {
        if (this.getPreRequest()) {
            var preRequest = this.getPreRequest();
            preRequest(this);
        }
        
        try {
            requestForm.setAttribute("target", frame.getAttribute("id"));
            requestForm.submit();
            var interval = window.setInterval(
            function() {
                if (frames[frameId].document 
                && frames[frameId].document.documentElement 
                && frames[frameId].document.getElementsByTagName("ajax-response").length == 1) {
                    /* We use frames[frameId].document.getElementsByTagName("ajax-response")
                     * instead of a more compact form because IE puts the ajax-response tag
                     * inside a body tag.
                     */
                    onComplete();
                    window.clearInterval(interval);
                }
            },
            250
            );
        } catch(ex) {
            if (this.getErrorHandler()) {
                var errorHandler = this.getErrorHandler();
                errorHandler(this, exception);
            }
            else {
                throw ex;
            }
        }
        
        return true;
    };
    
    function init() { 
        container = document.createElement("div");
        container.setAttribute("id", containerId);
        for (var name in requestParams) {
            var input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", name);
            input.setAttribute("value", requestParams[name]);
            container.appendChild(input);
        }
        
        frame = document.createElement("iframe");
        frame.setAttribute("id", frameId);
        frame.setAttribute("name", frameId);
        frame.setAttribute("src", "");
        frame.setAttribute("style", "width:0;height:0;visibility:hidden;");
        frame.style.cssText = "width:0;height:0;visibility:hidden;";
        
        container.appendChild(frame);
        
        requestForm.appendChild(container);
        
        // IE hack: we need to set id and name if undefined. 
        if (! frames[frameId].id) {
            frames[frameId].id = frameId;
        }
        if (! frames[frameId].name) {
            frames[frameId].name = frameId;
        }
    };
    
    function onComplete() {
        try {
            var nodes = frames[frameId].document.getElementsByTagName("ajax-response")[0].childNodes;
            var parser = new XT.taconite.XhtmlToDOMParser();
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].nodeType != 1) {
                    continue;
                }
                parser.parseXhtml(nodes[i]);
            }
        } catch(ex) {
            if (iFrameRequest.getErrorHandler()) {
                var errorHandler = iFrameRequest.getErrorHandler();
                errorHandler(iFrameRequest, exception);
            }
            else {
                throw ex;
            }
        } finally {
            try {
                // Remove the whole container, with the iframe and all hidden input fields:
                requestForm.removeChild(container);
                //
                if (iFrameRequest.getPostRequest()) {
                    var postRequest = iFrameRequest.getPostRequest();
                    postRequest(iFrameRequest);
                }
            }
            catch(exception) {
                if (iFrameRequest.getErrorHandler()) {
                    var errorHandler = iFrameRequest.getErrorHandler();
                    errorHandler(iFrameRequest, exception);
                }
            }
        }
    };
};


XT.taconite.IFrameRequest.prototype = new XT.taconite.BaseRequest();


XT.taconite.XhtmlToDOMParser = function() {
    
    this.parseXhtml = function(xml) {
        var xmlTagName=xml.tagName.toLowerCase();
        switch (xmlTagName) {
            case "append-as-children":
                executeAction(xml, appendAsChildrenAction);
                break;
            case "delete":
                executeAction(xml, deleteAction);
                break;
            case "append-as-first-child":
                executeAction(xml, appendAsFirstChildAction);
                break;                         
            case "insert-after":
                executeAction(xml, insertAfterAction);
                break;
            case "insert-before":
                executeAction(xml, insertBeforeAction);
                break;                         
            case "replace-children":
                executeAction(xml, replaceChildrenAction);
                break;
            case "replace":
                executeAction(xml, replaceAction);
                break;                         
            case "set-attributes":
                executeAction(xml, setAttributesAction);
                break;
            case "redirect":
                executeAction(xml, executeRedirectAction);
                break;
            case "execute-javascript":
                executeAction(xml, executeJavascriptAction);
                break;
            default :
                throw {'message' : 'Wrong tag name!'};
        }
    };
    
    function executeAction(xml, action) {
        var context = xml.getElementsByTagName("context")[0];
        var content = xml.getElementsByTagName("content")[0];
        if (context) {
            var contextNodes = getContextNodes(context);
            for (var i = 0; i < contextNodes.length; i++) {
                var contextNode = contextNodes[i];
                action(contextNode, content);
            }
        } else {
            action(content);
        }
    };
    
    function getContextNodes(context) {
        var matchMode = context.getElementsByTagName("matcher")[0].getAttribute("matchMode");
        var contextNodes = new Array();
        if (matchMode != null) {
            switch(matchMode) {
                case "plain" : 
                    contextNodes = getContextNodesByPlainMatch(context);
                    break;
                case "wildcard" : 
                    contextNodes = getContextNodesByWildcardMatch(context);
                    break;
                case "selector" : 
                    contextNodes = getContextNodesBySelectorMatch(context);
                    break;
            }
        } else {
            contextNodes = getContextNodesByPlainMatch(context);
        }
        return contextNodes;
    };
    
    function getContextNodesByPlainMatch(context) {
        var contextNodeID = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0, k = 0; i < contextIDs.length; i++) {
            var tmp = document.getElementById(contextIDs[i].trim());
            if (tmp != null) {
                contextNodes[k] = tmp;
                k++;
            }
        }
        return contextNodes;
    };
    
    function getContextNodesByWildcardMatch(context) {
        var contextNodeID = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeID");
        var contextIDs = contextNodeID.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextIDs.length; i++) {
            contextNodes = contextNodes.concat(document.getElementsByMatchingId(contextIDs[i].trim()));
        }
        return contextNodes;
    };
    
    function getContextNodesBySelectorMatch(context) {
        var selector = new XT.util.DOMSelector();
        var contextNodeSelector = context.getElementsByTagName("matcher")[0].getAttribute("contextNodeSelector");
        var contextSelectors = contextNodeSelector.split(',');
        var contextNodes = new Array();
        for (var i = 0; i < contextSelectors.length; i++) {
            contextNodes = contextNodes.concat(selector.select(contextSelectors[i].trim()));
        }
        return contextNodes;
    };
    
    function setAttributesAction(domNode,xml){
        var sourceNode = xml.getElementsByTagName("attributes")[0];
        handleAttributes(domNode, sourceNode);
    };
    
    function appendAsFirstChildAction(domNode,xml){
        var firstNode=null;
        if(domNode.childNodes.length > 0) {
            firstNode=domNode.childNodes[0];
        }
        
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null){
                if(firstNode==null){
                    domNode.appendChild(domChildNode);
                    firstNode=domChildNode;
                }
                else {
                    domNode.insertBefore(domChildNode,firstNode);
                }
            }
        }              
    };
    
    function insertAfterAction(domNode,xml){
        var domChildNode=null;
        var nextSibling=domNode.nextSibling;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null){
                if(nextSibling!=null) {
                    domNode.parentNode.insertBefore(domChildNode,nextSibling);
                }
                else {
                    domNode.parentNode.appendChild(domChildNode);
                }
            }
        }              
    };
    
    function insertBeforeAction(domNode,xml){
        var domChildNode=null;
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                domNode.parentNode.insertBefore(domChildNode,domNode);
            }
        }              
    };  
    
    function replaceAction(domNode,xml){
        insertAfterAction(domNode,xml);
        domNode.parentNode.removeChild(domNode);
    };
    
    function deleteAction(domNode) {
        domNode.parentNode.removeChild(domNode);
    };
    
    function appendAsChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, false);
    };
    
    function replaceChildrenAction(domNode,xml) {
        internalAppendOrReplaceChildren(domNode, xml, true);
    };
    
    function executeRedirectAction(xmlNode) {
        var targetUrl = xmlNode.getElementsByTagName("target")[0].getAttribute("url");
        window.location.href = targetUrl;
    };
    
    function executeJavascriptAction(xmlNode) {
        var scripts = xmlNode.getElementsByTagName("script");
        for (var i = 0; i < scripts.length; i++) {
            var script = scripts[i];
            if (script.getAttribute("type") == "text/javascript") {
                var js = "";
                for (var i = 0; i < script.childNodes.length; i++) {
                    js = js + script.childNodes[i].nodeValue;
                }
                eval(js);
            }
        }
    };
    
    function internalAppendOrReplaceChildren(domNode,xml,doRemoveChildren) {
        var domChildNode=null;
        if(doRemoveChildren){
            while(domNode.childNodes.length >0){
                domNode.removeChild(domNode.childNodes[0]);
            }      
        }
        for(var i=0;i<xml.childNodes.length;i++){
            domChildNode=handleNode(xml.childNodes[i]);
            if(domChildNode!=null) {
                // Here we have to check xml.childNodes[i].nodeType==1 because of an IE7 bug 
                if (XT.taconite.isIE && xml.childNodes[i].nodeType==1) {
                    checkForIEMultipleSelectHack(domNode, domChildNode);
                }
                domNode.appendChild(domChildNode);
            }
        }              
    };
    
    function isInlineMode(node) {
        var lowerCaseTag = node.tagName.toLowerCase();
        
        if(lowerCaseTag == "button") {
            return true;
        }
        
        var attrType = node.getAttribute("type");
        
        if(lowerCaseTag == "input" && (attrType == "radio" || attrType == "checkbox")) {
            return true;
        }
        return false;
    };  
    
    function handleAttributes(domNode, xmlNode, returnAsText) {
        var attr = null;
        var attrString = "";
        var name = "";
        var value = "";
        for(var x = 0; x < xmlNode.attributes.length; x++) {
            attr = xmlNode.attributes[x];
            name = cleanAttributeName(attr.name.trim());
            value = attr.value.trim();
            if(!returnAsText){
                if(name == "style") {
                    /* IE workaround */
                    domNode.style.cssText = value;
                    /* Standards compliant */
                    domNode.setAttribute(name, value);
                }
                else if(name.trim().toLowerCase().substring(0, 2) == "on") {
                    /* IE workaround for event handlers */
                    if(XT.taconite.isIE) { 
                        eval("domNode." + name.trim().toLowerCase() + "=function(){" + value + "}"); 
                    }
                    else { 
                        domNode.setAttribute(name,value); 
                    }                    
                }
                else if(name == "value") {
                    /* IE workaround for the value attribute -- makes form elements selectable/editable */
                    domNode.value = value;
                }
                else if(useIEFormElementCreationStrategy(xmlNode.tagName) && name == "name") {
                    //Do nothing, as the "name" attribute was handled in the createFormElementsForIEStrategy function
                    continue;
                }
                else {
                    /* Standards compliant */
                    domNode.setAttribute(name,value);
                }
                /* class attribute workaround for IE */
                if(name == "class") {
                    domNode.setAttribute("className",value);
                }
                
                /* This is a workaround for a bug in IE where select elemnts with multiple don't have 
                    all the appropriate options selected.  Only one is selected.  Appears fixed in IE7.
                 */
                if(XT.taconite.isIE) {
                    if(name == "multiple" && domNode.id != "") {
                        setTimeout(
                        function() {
                            var selectNode = document.getElementById(domNode.id);
                            var options = selectNode.getElementsByTagName("option");
                            var option;
                            for(var i = 0; i < options.length; i++) {
                                option = options[i];
                                if (typeof(option.taconiteOptionSelected) != "undefined") {
                                    option.selected = true;
                                } 
                                else {
                                    option.selected = false;
                                }
                            }
                        },
                        100);
                    }
                    if(name == "selected") {
                        domNode.taconiteOptionSelected = true;
                    }
                }
            } else{
                attrString = attrString + name + "=\"" + value + "\" " ;
            }
        }
        return attrString;
    };
    
    function handleNode(xmlNode){
        var nodeType = xmlNode.nodeType;               
        switch(nodeType) {
            case 1:  //ELEMENT_NODE
                return handleElement(xmlNode);
            case 3:  //TEXT_NODE
            case 4:  //CDATA_SECTION_NODE
                var textNode = document.createTextNode(xmlNode.nodeValue);
                if(XT.taconite.isIE) {
                    textNode.nodeValue = textNode.nodeValue.replace(/\n/g, '\r');
                }
                return textNode;
        }      
        return null;
    };
    
    function handleElement(xmlNode){
        var domElemNode = null;
        var xmlNodeTagName = xmlNode.tagName.toLowerCase();
        if(XT.taconite.isIE){
            if(isInlineMode(xmlNode)) {
                return document.createElement("<" + xmlNodeTagName + " " + handleAttributes(domElemNode, xmlNode, true) + ">");
            }
            if(xmlNodeTagName == "style"){
                //In internet explorer, we have to use styleSheets array.		
                var text,rulesArray,styleSheetPtr;
                var regExp = /\s+/g;
                text=xmlNode.text.replace(regExp, " ");
                rulesArray=text.split("}");
                
                domElemNode=document.createElement("style");
                styleSheetPtr=document.styleSheets[document.styleSheets.length-1];
                for(var i=0;i<rulesArray.length;i++){
                    rulesArray[i]=rulesArray[i].trim();
                    var rulePart=rulesArray[i].split("{");
                    if(rulePart.length==2) {//Add only if the rule is valid
                        styleSheetPtr.addRule(rulePart[0],rulePart[1],-1);//Append at the end of stylesheet.
                    }
                }							
                return domElemNode;			
            }
            
        }
        if(domElemNode == null){
            if(useIEFormElementCreationStrategy(xmlNodeTagName)) {
                domElemNode = createFormElementsForIEStrategy(xmlNode);
            }
            else {
                domElemNode = document.createElement(xmlNodeTagName);
            }
            
            handleAttributes(domElemNode,xmlNode);
            //Fix for IE Script tag: Unexpected call to method or property access error
            //IE don't allow script tag to have child
            if(XT.taconite.isIE && !domElemNode.canHaveChildren){
                if(xmlNode.childNodes.length > 0){
                    domElemNode.text=xmlNode.text;
                }
                
            }                              
            else{
                for(var z = 0; z < xmlNode.childNodes.length; z++) {
                    var domChildNode=handleNode(xmlNode.childNodes[z]);
                    if(domChildNode!=null) {
                        domElemNode.appendChild(domChildNode);
                    }
                }
            }
        }      
        
        return domElemNode;
    };
    
    function useIEFormElementCreationStrategy(xmlNodeTagName) {
        var useIEStrategy = false;
        
        var nodeName = xmlNodeTagName.toLowerCase();
        if (XT.taconite.isIE && (nodeName == "form" ||
        nodeName == "input" ||
        nodeName == "textarea" ||
        nodeName == "select" ||
        nodeName == "a" ||
        nodeName == "applet" ||
        nodeName == "button" ||
        nodeName == "img" ||
        nodeName == "link" ||
        nodeName == "map" ||
        nodeName == "object")) {
            useIEStrategy = true;
        }
        
        return useIEStrategy;
    };
    
    function createFormElementsForIEStrategy(xmlNode) {
        var attr = null;
        var name = "";
        var value = "";
        for (var x = 0; x < xmlNode.attributes.length; x++) {
            attr = xmlNode.attributes[x];
            name = attr.name.trim();
            if (name == "name") {
                value = attr.value.trim();
            }
        }
        
        domElemNode = document.createElement("<" + xmlNode.tagName + " name='" + value + "' />"); // e.g. document.createElement("<input name='slot2'>");
        
        return domElemNode;
    };
    
    function checkForIEMultipleSelectHack(domNode, domChildNode) {
        if(XT.taconite.isIE && domChildNode.nodeType == 1 && domChildNode.tagName.toLowerCase() == "select" && domChildNode.getAttribute("multiple") != null) {
            createIEMultipleSelectHack(domNode);
        }
    };
    
    function createIEMultipleSelectHack(contextNode) {
        //this is a total and complete hack for IE 6's totally broken select
        //box implementation.  A "multiple" select box only appears as a drop-
        //down box... but for some reason, creating another select around it
        //makes it render correctly (??).  So, create a bogus select box and hide
        //it.
        var selectBox = document.createElement("select");
        selectBox.size = 3;
        
        for (x=0; x < 1; x++) {
            selectBox.options[x] = new Option(x, x);
        }
        
        //hide it
        selectBox.style.display = "none";
        
        contextNode.appendChild(selectBox);
    };
    
    function cleanAttributeName(name) {
        if(XT.taconite.isIE == false) {
            return;
        }
        
        // IE workaround to change cellspacing to cellSpacing, etc
        var cleanName = name.toLowerCase();
        if(cleanName == "cellspacing") {
            cleanName = "cellSpacing";
        }
        else if(cleanName == "cellpadding") {
            cleanName = "cellPadding";
        }
        else if(cleanName == "colspan") {
            cleanName = "colSpan";
        }
        else if(cleanName == "tabindex") {
            cleanName = "tabIndex";
        }
        else if(cleanName == "readonly") {
            cleanName = "readOnly";
        }
        else if(cleanName == "maxlength") {
            cleanName = "maxLength";
        }
        else if(cleanName == "rowspan") {
            cleanName = "rowSpan";
        }
        else if(cleanName == "valign") {
            cleanName = "vAlign";
        }
        
        return cleanName;
    };
};
XT.util = {};


XT.util.DOMSelector = function() {
    
    this.select = function(selector, rootContext) {
        if (! rootContext) {
            rootContext = document;
        }
        // Remove unwanted spaces between combinators:
        // > combinator
        selector = selector.replace(/\s*(?=>)/g, '');
        selector = selector.replace(/>\s*/g, '>');
        // + combinator
        selector = selector.replace(/\s*(?=\+)/g, '');
        selector = selector.replace(/\+\s*/g, '+');
        // ~ combinator
        selector = selector.replace(/\s*(?=~)/g, '');
        selector = selector.replace(/~\s*/g, '~');
        // Split selector into tokens
        var splitter = /\s|>|\+|~/g;
        var combinators = selector.match(splitter);
        var tokens = selector.split(splitter);
        var currentContext = new Array(rootContext);
        // Prepare regular expressions that will be used later:
        var attributesRegexp = /^(\w*)\[(\w+)([=~\|\^\$\*]?)=?"?([^\]"]*)"?\]$/;
        var pseudoClassesRegexp = /^(\w*)\:(\w+-?\w+)/;
        var regexpResult = null;
        // Go!
        for (var i = 0; i < tokens.length; i++) {
            var combinator = i == 0 ? " " : combinators[i - 1];
            var token = tokens[i].trim();
            if (token.indexOf('#') > -1) {
                // Token is an ID selector
                var tagName = token.substring(0, token.indexOf('#'));
                var id = token.substring(token.indexOf('#') + 1, token.length);
                var filterFunction = function(e) { 
                    return (e.id == id); 
                };
                var found = new Array();
                for (var h = 0; h < currentContext.length; h++) {
                    found = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    if (found && found.length == 1) {
                        break;
                    }
                }
                // Set currentContext to contain just this element
                currentContext = found;
                // Skip to next token
                continue; 
            }
            else if ((regexpResult = attributesRegexp.exec(token))) {
                // Token contains attribute selectors
                var tagName = regexpResult[1];
                var attrName = regexpResult[2];
                var attrOperator = regexpResult[3];
                var attrValue = regexpResult[4];
                // Attribute filtering functions:
                var filterFunction = null; // This function will be used to filter the elements
                switch (attrOperator) {
                    case '=': // Equality
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName) == attrValue); 
                        };
                        break;
                    case '~': // Match one of space seperated words 
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).match(new RegExp('(\\s|^)' + attrValue + '(\\s|$)'))); 
                        };
                        break;
                    case '|': // Match start with value followed by optional hyphen
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).match(new RegExp('^' + attrValue + '-?'))); 
                        };
                        break;
                    case '^': // Match starts with value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).indexOf(attrValue) == 0); 
                        };
                        break;
                    case '$': // Match ends with value - fails with "Warning" in Opera 7
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && (e.getAttribute(attrName).lastIndexOf(attrValue) == e.getAttribute(attrName).length - attrValue.length)); 
                        };
                        break;
                    case '*': // Match contains value
                        filterFunction = function(e) { 
                            return (e.getAttribute(attrName) && e.getAttribute(attrName).indexOf(attrValue) > -1); 
                        };
                        break;
                    default :
                        // Just test for existence of attribute
                        filterFunction = function(e) { 
                            return e.getAttribute(attrName); 
                        };
                }
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue;
            } 
            else if ((regexpResult = pseudoClassesRegexp.exec(token))) {
                // Token contains pseudo-class selectors
                var tagName = regexpResult[1];
                var pseudoClass = regexpResult[2];
                // Pseudo class filtering functions:
                var filterFunction = null; // This function will be used to filter the elements
                switch (pseudoClass) {
                    case 'first-child': 
                        filterFunction = function(e) { 
                            var parent = e.parentNode;
                            var i = 0;
                            while (parent.childNodes[i] && parent.childNodes[i].nodeType != 1) i++;
                            return (parent.childNodes[i] == e); 
                        };
                        break;
                    case 'last-child': 
                        filterFunction = function(e) { 
                            var parent = e.parentNode;
                            var i = parent.childNodes.length - 1;
                            while (parent.childNodes[i] && parent.childNodes[i].nodeType != 1) i--;
                            return (parent.childNodes[i] == e); 
                        };
                        break;    
                    case 'empty': 
                        filterFunction = function(e) { 
                            return (e.childNodes.length == 0); 
                        };
                        break;
                    default :
                        filterFunction = function(e) { 
                            return false; 
                        };
                }
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue;
            } 
            else if (token.indexOf('.') > -1) {
                // Token contains a class selector
                var tagName = token.substring(0, token.indexOf('.'));
                var className = token.substring(token.indexOf('.') + 1, token.length);
                var regexp = new RegExp('(\\s|^)' + className + '(\\s|$)');
                var filterFunction = function(e) { 
                    return (e.className && e.className.match(regexp)); 
                };
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
                // Skip to next token
                continue; 
            }
            else {
                // If we get here, token is just an element (not a class or ID selector)
                tagName = token;
                var filterFunction = function(e) { 
                    return true; 
                };
                var found = new Array();
                var counter = 0;
                for (var h = 0; h < currentContext.length; h++) {
                    var elements = selectByFilter(combinator, currentContext[h], tagName, filterFunction);
                    for (var j = 0; j < elements.length; j++) {
                        found[counter++] = elements[j];
                    }
                }
                currentContext = found;
            }
        }
        return currentContext;
    };
    
    function selectByFilter(combinator, context, tagName, filterFunction) {
        var result = new Array();
        var elements = new Array();
        // Get elements to filter depending on the combinator:
        if (combinator == " ") {
            elements = context.all ? context.all : context.getElementsByTagName('*');
        } else if (combinator == ">") {
            elements = context.childNodes;
        } else if (combinator == "+") {
            var sibling = context.nextSibling;
            while (sibling && sibling.nodeType != 1) {
                sibling = sibling.nextSibling;
            }
            if (sibling) elements = new Array(sibling);
            else elements = new Array();
        } else if (combinator == "~") {
            var sibling = context.nextSibling;
            var counter = 0;
            while (sibling) {
                if (sibling.nodeType == 1) {
                    elements[counter] = sibling;
                    counter++;
                }
                sibling = sibling.nextSibling;
            }
        }
        // Actually filter elements by tag name and filter function:
        var counter = 0;
        if (!tagName || tagName == '*') {
            for (var k = 0; k < elements.length; k++) {
                if (elements[k].nodeType == 1 && filterFunction(elements[k])) {
                    result[counter] = elements[k];
                    counter++;
                }
            }
        } else {
            for (var k = 0; k < elements.length; k++) {
                if (elements[k].nodeType == 1 && elements[k].nodeName.toLowerCase() == tagName.toLowerCase() && filterFunction(elements[k])) {
                    result[counter] = elements[k];
                    counter++;
                }
            }
        }
        return result;
    };
};


/*
    json2.js
    2007-11-06

    Public Domain

    No warranty expressed or implied. Use at your own risk.

    See http://www.JSON.org/js.html

    This file creates a global JSON object containing two methods:

        JSON.stringify(value, whitelist)
            value       any JavaScript value, usually an object or array.

            whitelist   an optional that determines how object values are
                        stringified.

            This method produces a JSON text from a JavaScript value.
            There are three possible ways to stringify an object, depending
            on the optional whitelist parameter.

            If an object has a toJSON method, then the toJSON() method will be
            called. The value returned from the toJSON method will be
            stringified.

            Otherwise, if the optional whitelist parameter is an array, then
            the elements of the array will be used to select members of the
            object for stringification.

            Otherwise, if there is no whitelist parameter, then all of the
            members of the object will be stringified.

            Values that do not have JSON representaions, such as undefined or
            functions, will not be serialized. Such values in objects will be
            dropped, in arrays will be replaced with null. JSON.stringify()
            returns undefined. Dates will be stringified as quoted ISO dates.

            Example:

            var text = JSON.stringify(['e', {pluribus: 'unum'}]);
            // text is '["e",{"pluribus":"unum"}]'

        JSON.parse(text, filter)
            This method parses a JSON text to produce an object or
            array. It can throw a SyntaxError exception.

            The optional filter parameter is a function that can filter and
            transform the results. It receives each of the keys and values, and
            its return value is used instead of the original value. If it
            returns what it received, then structure is not modified. If it
            returns undefined then the member is deleted.

            Example:

            // Parse the text. If a key contains the string 'date' then
            // convert the value to a date.

            myData = JSON.parse(text, function (key, value) {
                return key.indexOf('date') >= 0 ? new Date(value) : value;
            });

    This is a reference implementation. You are free to copy, modify, or
    redistribute.

    Use your own copy. It is extremely unwise to load third party
    code into your pages.
*/

/*jslint evil: true */
/*extern JSON */

if (!this.JSON) {

    JSON = function () {

        function f(n) {    // Format integers to have at least two digits.
            return n < 10 ? '0' + n : n;
        }

        Date.prototype.toJSON = function () {

// Eventually, this method will be based on the date.toISOString method.

            return this.getUTCFullYear()   + '-' +
                 f(this.getUTCMonth() + 1) + '-' +
                 f(this.getUTCDate())      + 'T' +
                 f(this.getUTCHours())     + ':' +
                 f(this.getUTCMinutes())   + ':' +
                 f(this.getUTCSeconds())   + 'Z';
        };


        var m = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };

        function stringify(value, whitelist) {
            var a,          // The array holding the partial texts.
                i,          // The loop counter.
                k,          // The member key.
                l,          // Length.
                r = /["\\\x00-\x1f\x7f-\x9f]/g,
                v;          // The member value.

            switch (typeof value) {
            case 'string':

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe sequences.

                return r.test(value) ?
                    '"' + value.replace(r, function (a) {
                        var c = m[a];
                        if (c) {
                            return c;
                        }
                        c = a.charCodeAt();
                        return '\\u00' + Math.floor(c / 16).toString(16) +
                                                   (c % 16).toString(16);
                    }) + '"' :
                    '"' + value + '"';

            case 'number':

// JSON numbers must be finite. Encode non-finite numbers as null.

                return isFinite(value) ? String(value) : 'null';

            case 'boolean':
            case 'null':
                return String(value);

            case 'object':

// Due to a specification blunder in ECMAScript,
// typeof null is 'object', so watch out for that case.

                if (!value) {
                    return 'null';
                }

// If the object has a toJSON method, call it, and stringify the result.

                if (typeof value.toJSON === 'function') {
                    return stringify(value.toJSON());
                }
                a = [];
                if (typeof value.length === 'number' &&
                        !(value.propertyIsEnumerable('length'))) {

// The object is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                    l = value.length;
                    for (i = 0; i < l; i += 1) {
                        a.push(stringify(value[i], whitelist) || 'null');
                    }

// Join all of the elements together and wrap them in brackets.

                    return '[' + a.join(',') + ']';
                }
                if (whitelist) {

// If a whitelist (array of keys) is provided, use it to select the components
// of the object.

                    l = whitelist.length;
                    for (i = 0; i < l; i += 1) {
                        k = whitelist[i];
                        if (typeof k === 'string') {
                            v = stringify(value[k], whitelist);
                            if (v) {
                                a.push(stringify(k) + ':' + v);
                            }
                        }
                    }
                } else {

// Otherwise, iterate through all of the keys in the object.

                    for (k in value) {
                        if (typeof k === 'string') {
                            v = stringify(value[k], whitelist);
                            if (v) {
                                a.push(stringify(k) + ':' + v);
                            }
                        }
                    }
                }

// Join all of the member texts together and wrap them in braces.

                return '{' + a.join(',') + '}';
            }
        }

        return {
            stringify: stringify,
            parse: function (text, filter) {
                var j;

                function walk(k, v) {
                    var i, n;
                    if (v && typeof v === 'object') {
                        for (i in v) {
                            if (Object.prototype.hasOwnProperty.apply(v, [i])) {
                                n = walk(i, v[i]);
                                if (n !== undefined) {
                                    v[i] = n;
                                }
                            }
                        }
                    }
                    return filter(k, v);
                }


// Parsing happens in three stages. In the first stage, we run the text against
// regular expressions that look for non-JSON patterns. We are especially
// concerned with '()' and 'new' because they can cause invocation, and '='
// because it can cause mutation. But just to be safe, we want to reject all
// unexpected forms.

// We split the first stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace all backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

                if (/^[\],:{}\s]*$/.test(text.replace(/\\./g, '@').
replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(:?[eE][+\-]?\d+)?/g, ']').
replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

// In the second stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                    j = eval('(' + text + ')');

// In the optional third stage, we recursively walk the new structure, passing
// each name/value pair to a filter function for possible transformation.

                    return typeof filter === 'function' ? walk('', j) : j;
                }

// If the text is not JSON parseable, then a SyntaxError is thrown.

                throw new SyntaxError('parseJSON');
            }
        };
    }();
}

String.prototype.trim = function() {
    //skip leading and trailing whitespace
    //and return everything in between
    var x=this;
    x=x.replace(/^\s*(.*)/, "$1");
    x=x.replace(/(.*?)\s*$/, "$1");
    return x;
};


document.getElementsByMatchingId = function(matchingId) {
    var allElements = document.all ? document.all : document.getElementsByTagName('*');
    var matchingElements = new Array();
    for (var i = 0; i < allElements.length; i++) {
        var currentElement = allElements[i];
        if (currentElement.nodeType == 1) {
            var id = currentElement.getAttribute("id");
            if (id != null && id != "") {
                if (id.indexOf("_") == (id.length - 1)) {
                    var pattern = "^" + id.replace(/_$/, ".*");
                    var rexp = new RegExp(pattern);
                    if (rexp.test(matchingId)) {
                        matchingElements.push(currentElement);
                    }
                } else if (id == matchingId) {
                    matchingElements.push(currentElement);
                }
            }
        }
    }
    return matchingElements;
};
