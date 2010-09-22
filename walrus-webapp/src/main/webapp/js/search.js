google.load("search", "1");
	    
var domainRoot = window.location.hostname;
var searchFormElementId = "searchForm";
var searchResultsElementId = "searchResults";
var contentElementId = "mainContent";
var rightElementId = "right";

var searcher;
var searchForm;
var cursorDiv;

function registerLoadHandler(handler) {
	var node = window;
	if (node.addEventListener) {
		node.addEventListener("load", handler, false);
	} else if (node.attachEvent) {
		node.attachEvent("onload", handler);
	} else {
		node['onload'] = handler;
	}
	return true;
}
  
function searchLoad() {
	searcher = new GwebSearch();
	searcher.setSiteRestriction(domainRoot);
	searcher.setResultSetSize(GSearch.LARGE_RESULTSET);
	searcher.setSearchCompleteCallback(this, showCoreSearch);

	var searchFormElement = document.getElementById("mock_gs");
	searchForm = new GSearchForm(true, searchFormElement);
	searchForm.setOnSubmitCallback(this, startGSearch);
	searchForm.setOnClearCallback(this, clearCoreSearch);
}
 	
function startGSearch(form) {
	pageTracker._trackEvent('Demo - content', 'search');
	if (form.input.value) {
		searcher.execute(form.input.value);
	}
} 	

function startSearch(form) {
	if (form.q.value) {
		searcher.execute(form.q.value);
	}
} 	
 	
function showCoreSearch() {
	if (!searcher.results) return;
	
	var container = document.getElementById(searchResultsElementId);
	removeChildren(container);
	
	if (!searcher.cursor) {
		container.appendChild(showNoResults());
	} else {
		container.appendChild(showResults(searcher));
	}
	
	container.appendChild(GSearch.getBranding());
	
	document.getElementById(contentElementId).style.display = 'none';
	if(document.getElementById(rightElementId))
		document.getElementById(rightElementId).style.display = 'none';
	container.style.display = 'block';
	container.parentNode.style.display = 'block';
}	    

function showResults(searcher) {

		var resDiv = document.createElement("DIV");
		
		var pageCount = searcher.cursor.pages.length;
		var resultCount = searcher.results.length;
		var cursor = searcher.cursor;
		var results = searcher.results;

		var startResult = parseInt(cursor.pages[cursor.currentPageIndex].start);
		resDiv.appendChild(createDiv("Showing results " + (startResult + 1) + " - " + (startResult + resultCount) + " of " + cursor.estimatedResultCount));
		
		for (var i = 0; i < searcher.results.length; i++) {
			resDiv.appendChild(searcher.results[i].html);
		}

		resDiv.appendChild(showPages(searcher));		
		
		return resDiv;
}

function gotoPage(searcher, page) {
	searcher.gotoPage(page);
}

function showPages(searcher) {
	var cursorDiv;
	if (searcher.cursor) {
		cursorDiv = createDiv(null, "gsc-cursor-box");
		
		var cursorNode = createDiv(null, "gsc-cursor");
		for (var i = 0; i < searcher.cursor.pages.length; i++) {
			var className = "gsc-cursor-page";
			if (i == searcher.cursor.currentPageIndex) {
				className = className + " gsc-cursor-current-page";
			}
			var pageNode = createDiv(searcher.cursor.pages[i].label, className);
			pageNode.onclick = methodClosure(this, this.gotoPage,  [searcher, i]); 
			cursorNode.appendChild(pageNode);
		}
		cursorDiv.appendChild(cursorNode);
		var more = createLink(searcher.cursor.moreResultsUrl,
		                      GSearch.strings["more-results"] + "&nbsp;&raquo;",
		                      GSearch.LINK_TARGET_SELF,
		                      "gsc-trailing-more-results");
		cursorDiv.appendChild(more);
	} else {
		cursorDiv = createDiv("BLAGAI");
	}
	
	return cursorDiv;
}

function showNoResults() {
	return createDiv("No search results found.");
}

function clearCoreSearch() {
	document.getElementById(contentElementId).style.display = 'block';
	if(document.getElementById(rightElementId))
		document.getElementById(rightElementId).style.display = 'block';
	document.getElementById(searchResultsElementId).style.display = 'none';
}

    /**
     * Static DOM Helper Functions
     */
    function removeChildren(parent) {
      while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
      }
    }
    
    function createDiv(opt_text, opt_className) {
      var el = document.createElement("div");
      if (opt_text) {
        el.innerHTML = opt_text;
      }
      if (opt_className) { el.className = opt_className; }
      return el;
    }

    function methodClosure(object, method, opt_argArray) {
      return function() {
        return method.apply(object, opt_argArray);
      }
    }

    function createLink(href, opt_text, opt_target, opt_className, opt_divwrap) {
      var el = document.createElement("a");
      el.href = href;
      if (opt_text) {
        el.innerHTML = opt_text;
      }
      if (opt_className) {
        el.className = opt_className;
      }
      if (opt_target) {
        el.target = opt_target;
      }
      if (opt_divwrap) {
        var div = this.createDiv(null, opt_className);
        div.appendChild(el);
        el = div;
      }
      return el;
    }

google.setOnLoadCallback(searchLoad);