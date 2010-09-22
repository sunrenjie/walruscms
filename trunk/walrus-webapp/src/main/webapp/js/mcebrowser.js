function fileBrowserCallback(field_name, url, type, win) {
        var listUrl = "../files/list?type="+type;
        
        tinyMCE.activeEditor.windowManager.open({
        file : listUrl,
        title : 'Pasirinkite failą',
        width : 420,  // Your dimensions may differ - toy around with them!
        height : 400,
        resizable : "yes",
        inline : "yes",  // This parameter only has an effect if you use the inlinepopups plugin!
        close_previous : "no"
    }, {
        'window' : win,
        'input' : field_name
    }); 
    return false;
}

function selectURL(url) {
        //field = window.top.opener.browserWin.document.forms[0].elements[window.top.opener.browserField];
        browserWin=tinyMCEPopup.getWindowArg('window');
        browserField=tinyMCEPopup.getWindowArg('input');
        field=browserWin.document.forms[0].elements[browserField];
        field.value = url;
        if (field.onchange != null)
                field.onchange();
        browserWin.focus();  
        tinyMCEPopup.close();
}

function fileUploaded(param) {
        selectURL(param.url);
}