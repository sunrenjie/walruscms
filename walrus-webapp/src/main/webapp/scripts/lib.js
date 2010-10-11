(function(view, undefined) {
    var ua = navigator.userAgent.toLowerCase(),
        
        log = function log() {
            if (view.console && view.console.log && view.console.log.apply) {
                view.console.log.apply(view.console, arguments);
            } else {
                log.output.push(lib.array.toArray(arguments).join(", "));
                view.clearTimeout(log.time);
                log.time = view.setTimeout(function() {
                    var t = log.output.join("\r\n");
                    log.output = [];
                    alert(t);
                }, 1000);
            }
            return arguments[0];
        },
        
        inspect = function inspect(object) {
            var o = [];
            for (var i in object) {
                o.push(i + ": " + object[i]);
            }
            return o.join("\r\n");
        },
        
        lib = {
            log: log,
            inspect: inspect,
            
            view: view,
            window: view, // for legacy compatibility
            document: view && view.document || document,
            
            isReady: false,
            
            ready: function(f) {
                if (typeof f !== "function") return;
                if (!lib.isReady) {
                    lib.event.add(document, "libReady", lib.bind(f, window));
                } else {
                    f();
                }
            },
            
            env: function env() {
                var nanArray = [0, NaN],
                    opera = (/opera[\s\/]([\w\.]+)/.exec(ua) || nanArray)[1],
                    ie = opera ? NaN : (/msie ([\w\.]+)/.exec(ua) || nanArray)[1],
                    gecko = (/rv:([\w\.]+).*gecko\//.exec(ua) || nanArray)[1],
                    webkit = (/applewebkit\/([\w\.]+)/.exec(ua) || nanArray)[1],
                    khtml = (/khtml\/([\w\.]+)/.exec(ua) || nanArray)[1];
                
                return {
                    gecko   : parseFloat(gecko),
                    ie      : parseFloat(ie),
                    opera   : parseFloat(opera),
                    webkit  : parseFloat(webkit),
                    khtml   : parseFloat(khtml),
                    version : ie || gecko || webkit || opera || khtml,
                    standardsMode : this.d.compatMode != "BackCompat" && (!ie || ie >= 6)
                }
            },
            
            extend: function extend(target) {
                for (var i, k = 0, len = arguments.length; ++k < len;)
                    for (i in arguments[k]) target[i] = arguments[k][i];
                return target;
            },
            
            bind: function bind(method, context) {
                return function() {
                    return method.apply(context, arguments);
                };
            },
            
            guid: function guid(object) {
                if (!object) return ++lib.guid.id;
                if (!object._guid) object._guid = lib.guid.id++;
                return object._guid;
            }
        };
    
    lib.log.output = [];
    lib.guid.id = 1;
    
    if (!view.lib) view.lib = lib;
    if (!view.log) view.log = log;
    
    (function() {
        if (lib.isReady) return;
        
        function onReady() {
            if (!lib.isReady) {
                lib.isReady = true;
                lib.event.dispatch(document, "libReady", {safe: true});
            }
        };
        
        if (document.readyState == "complete") { // already here!
            onReady();
        } else if (lib.env.ie && document.attachEvent) {
            // like IE
            
            // not an iframe...
            if (document.documentElement.doScroll && window == top) {
                (function() {
                    try {
                        document.documentElement.doScroll("left");
                    } catch(error) {
                        setTimeout(arguments.callee, 0);
                        return;
                    }
                    
                    // and execute any waiting functions
                    onReady();
                })();
            } else {
                // an iframe...
                document.attachEvent(
                    "onreadystatechange",
                    function() {
                        if (document.readyState == "complete") {
                            document.detachEvent("onreadystatechange", arguments.callee);
                            onReady();
                        }
                    }
                );
            }
        } else if (document.readyState) {
            // like pre Safari
            (function() {
                if (/loaded|complete/.test(document.readyState)) {
                    onReady();
                } else {
                    setTimeout(arguments.callee, 0);
                }
            })();
        } else if (document.addEventListener) {
            // like Mozilla, Opera and recent webkit
            document.addEventListener( 
                "DOMContentLoaded",
                function() {
                    document.removeEventListener("DOMContentLoaded", arguments.callee, false);
                    onReady();
                },
                false
            );
        } else {
            throw new Error("Unable to bind lib ready listener to document.");
        }
    })();
})(window);
(function(lib, undefined) {
    if (lib.utils) throw new Error("lib.utild already defined");
    
    lib.utils = {
        getType: function getType(object) {
            var typeOfObject = typeof object,
                constructorStr,
                type;
            
            if (object === null) { return "null"; }
            else if (Object.prototype.toString.call(object) === "[object Function]") { return "Function"; }
            else if (Object.prototype.toString.call(object) === "[object Array]") { return "Array"; }
            else if (typeOfObject === 'object') {
                constructorStr = object.constructor.toString();
                if (/^function (\S+?)\(/.test(constructorStr)) {
                    type = RegExp.$1;
                    if (type === 'Object') { return 'Object'; }
                    else { return type; }
                }
            }
            
            return typeOfObject;
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.object) throw new Error("lib.object already defined");
    
    lib.object = {
        subtract: function subtract(minuend, subtrahend) {
            var difference = {};
            for (var i in minuend) {
                if (typeof subtrahend[i] == "undefined") {
                    difference[i] = minuend[i];
                }
            }
            return difference;
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.array) throw new Error("lib.array already defined");
    
    lib.array = {
        toArray: function toArray(object) {
            var array = [];
            try {
                array = Array.prototype.slice.call(object, 0);
            } catch (e) {
                for (var i = 0, len = object.length; i < len; i++) array[i] = object[i];
            }
            return array;
        },
        
        inArray: function inArray(array, object) {
            for (var i = 0, len = array.length; i < len; i++) {
                if (array[i] === object) return true;
            }
            return false;
        },
        
        forEach: function forEach(array, callback, thisObject) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("forEach" in array) {
                array.forEach(callback, thisObject || lib.view);
            } else {
                for (var i = 0, len = array.length; i < len; i++) {
                    callback.call(thisObject || lib.view, array[i], i, array);
                }
            }
        },
        
        every: function every(array, callback, thisObject) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("every" in array) {
                return array.every(callback, thisObject || lib.view);
            } else {
                for (var i = 0, len = array.length; i < len; i++) {
                    if (i in array && !callback.call(thisObject || lib.view, array[i], i, array)) return false;
                }
                return true;
            }
        },
        
        some: function some(array, callback, thisObject) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("some" in array) {
                return array.some(callback, thisObject || lib.view);
            } else {
                for (var i = 0, len = array.length; i < len; i++) {
                    if (i in array && callback.call(thisObject || lib.view, array[i], i, array)) return true;
                }
                return false;
            }
        },
        
        filter: function filter(array, callback, thisObject) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("filter" in array) {
                return array.filter(callback, thisObject || lib.view);
            } else {
                var out = [];
                for (var i = 0, len = array.length; i < len; i++) {
                    if (i in array) {
                        if (callback.call(thisObject || lib.view, array[i], i, array)) out.push(array[i]);
                    }
                }
                return out;
            }
        },
        
        map: function map(array, callback, thisObject) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("map" in array) {
                return array.map(callback, thisObject || lib.view);
            } else {
                var len = array.length,
                    out = new Array(len);
                for (var i = 0; i < len; i++) {
                    if (i in array) out[i] = callback.call(thisObject || lib.view, array[i], i, array);
                }
                return out;
            }
        },
        
        reduce: function reduce(array, callback, initialValue) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("reduce" in array) {
                return array.reduce(callback, initialValue);
            } else {
                var len = array.length,
                    isUndefined = typeof initialValue == "undefined";
                
                if (len == 0 && isUndefined) {
                    throw new TypeError("Reduce of empty array with no initial value");
                }
                
                var i = 0,
                    out = (isUndefined) ? initialValue : array[i++];
                
                for (; i < len; i++) {
                    if (i in array) out = callback.call(lib.view, out, array[i], i, array);
                }
                return out;
            }
        },
        
        reduceRight: function reduceRight(array, callback, initialValue) {
            if (typeof callback != "function") throw new TypeError(callback + " is not a function");
            
            if ("reduceRight" in array) {
                return array.reduceRight(callback, initialValue);
            } else {
                var len = array.length,
                    isUndefined = typeof initialValue == "undefined";
                
                if (len == 0 && isUndefined) {
                    throw new TypeError("Reduce of empty array with no initial value");
                }
                
                var i = len - 1,
                    out = (isUndefined) ? initialValue : array[i--];
                
                for (; i >= 0; i--) {
                    if (i in array) out = callback.call(lib.view, out, array[i], i, array);
                }
                return out;
            }
        },
        
        equal: function equal(array) {
            var out = true,
                len = array.length;
            for (var i = 1, argsLen = arguments.length; i < argsLen; i++) {
                if (len != arguments[i].length) return false;
                for (var j = 0; j < len; j++) {
                    if (array[j] instanceof Array || arguments[i][j] instanceof Array) {
                        if (array[j] instanceof Array && arguments[i][j] instanceof Array) {
                            out = this.equal(array[j], arguments[i][j]);
                            if (!out) break;
                        } else {
                            return false;
                        }
                    } else {
                        out = (array[j] == arguments[i][j]);
                        if (!out) break;
                    }
                }
                if (!out) break;
            }
            return out;
        },
        
        subtract: function subtract(array) {
            var outPrev = array,
                outCurr = [],
                subtrahend = [];
            
            for (var i = 1, argsLen = arguments.length; i < argsLen; i++) {
                if (arguments[i] instanceof Array) {
                    for (var j = 0, len = arguments[i].length; j < len; j++) {
                        subtrahend.push(arguments[i][j]);
                    }
                } else {
                    subtrahend.push(arguments[i]);
                }
            }
            
            for (var i = 0, len = subtrahend.length; i < len; i++) {
                for (var j = 0, outLen = outPrev.length; j < outLen; j++) {
                    if (subtrahend[i] instanceof Array) {
                        if (!this.equal(outPrev[j], subtrahend[i])) {
                            outCurr.push(outPrev[j]);
                        }
                    } else {
                        if (outPrev[j] != subtrahend[i]) {
                            outCurr.push(outPrev[j]);
                        }
                    }
                }
                outPrev = outCurr;
                outCurr = [];
            }
            return outPrev;
        },
        
        intersect: function intersect(array) {
            var out = array;
            for (var i = 1, len = arguments.length; i < len; i++) {
                var tmp = this.subtract(array, arguments[i]);
                out = this.subtract(out, tmp);
            }
            return out;
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.string) throw new Error("lib.string already defined");
    
    lib.string = {
        trim: function trim(str) {
            if ("trim" in String) {
                return String.trim(str);
            } else {
                str = str.replace(/^\s\s*/, "");
                var i = str.length;
                while (/\s/.test(str.charAt(--i)));
                return str.slice(0, i + 1);
            }
        },
        
        trimLeft: function trimLeft(str) {
            if ("trimLeft" in String) {
                return String.trimLeft(str);
            } else {
                return str.replace(/^\s\s*/, "");
            }
        },
        
        trimRight: function trimRight(str) {
            if ("trimRight" in String) {
                return String.trimRight(str);
            } else {
                var i = str.length;
                while (/\s/.test(str.charAt(--i)));
                return str.slice(0, i + 1);
            }
        },
        
        padding: function padding(str, pad, length) {
            if (typeof str != "string") str = str.toString();
            
            var absLength = Math.abs(length);
            if (str.length >= absLength) return str;
            
            var prepend = (absLength == length) ? true : false,
                out = new Array(absLength - str.length);
            
            for (var i = 0, len = out.length; i < len; i++) out[i] = pad;
            
            if (prepend) {
                out.push(str);
            } else {
                out.unshift(str);
            }
            return out.join("");
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.date) throw new Error("lib.date already defined");
    
    lib.date = {
        parseISOString: function parseISOString(str) {
            var parsed = Date.parse(str);
            if (!isNaN(parsed)) return new Date(parsed);
            
            var match = str.match(/\d+/g),
                date = new Date(match[0], parseInt(match[1], 10) - 1, match[2], match[3], match[4], match[5], match[6]),
                offset = (new Date()).getTimezoneOffset(),
                offsetAbs = Math.abs(offset),
                offsetSign = (offsetAbs == offset) ? -1 : 1,
                offsetHours = (offsetAbs - (offsetAbs % 60)) / 60,
                offsetMinutes = offsetAbs - offsetHours * 60;
            date.setHours(date.getHours() + offsetHours * offsetSign);
            date.setMinutes(date.getMinutes() + offsetMinutes * offsetSign);
            return date;
        },
        
        toISOString: function toISOString(date) {
            if ("toISOString" in date) {
                return date.toISOString();
            } else {
                return date.getUTCFullYear() + "-"
                     + lib.utils.padding(date.getUTCMonth() + 1, 0, 2) + "-"
                     + lib.utils.padding(date.getUTCDate(), 0, 2) + "T"
                     + lib.utils.padding(date.getUTCHours(), 0, 2) + ":"
                     + lib.utils.padding(date.getUTCMinutes(), 0, 2) + ":"
                     + lib.utils.padding(date.getUTCSeconds(), 0, 2) + "."
                     + lib.utils.padding(date.getUTCMilliseconds(), 0, 3) + "Z";
            }
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.JSON) throw new Error("lib.JSON already defined");
    
    lib.JSON = {
        parse: function parse(str) {
            if (JSON && "parse" in JSON) {
                return JSON.parse(str);
            } else {
                if (typeof str != "string" && typeof str != "undefined") return str;
                
                var canEval = (/^[\],:{}\s]*$/.test(
                    str.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@")
                    .replace(/\"[^\"\\\n\r]*\"|true|false|null|-?\d+(?:\.\d*)?(:?[eE][+\-]?\d+)?/g, "]")
                    .replace(/(?:^|:|,)(?:\s*\[)+/g, "")
                ));
                if (canEval) {
                    return (new Function("return (" + str + ")"))();
                } else {
                    throw new SyntaxError("Invalid JSON");
                }
            }
        },
        
        stringify: function stringify(object) {
            function stringify(object) {
                if (lib.utils.getType(object) == "array") {
                    var type = {
                        start : "[",
                        end : "]",
                        showKeys : false
                    };
                } else {
                    var type = {
                        start : "{",
                        end : "}",
                        showKeys : true
                    };
                }
                
                var serial = [type.start],
                    len = 1,
                    dataType,
                    notFirst = false;
                
                for (var key in object) {
                    dataType = lib.utils.getType(object[key]);
                    
                    if (dataType != "undefined") {
                        if (notFirst) {
                            serial[len++] = ",";
                        }
                        notFirst = true;
                        
                        if (type.showKeys) {
                            serial[len++] = "\"";
                            serial[len++] = key;
                            serial[len++] = "\"";
                            serial[len++] = ":";
                        }
                        
                        switch (dataType) {
                            case "Function":
                                throw new TypeError("Cannot stringify function to JSON");
                                break;
                            case "String":
                            default:
                                serial[len++] = "\"";
                                serial[len++] = object[key];
                                serial[len++] = "\"";
                                break;
                            case "Number":
                            case "Boolean":
                                serial[len++] = object[key];
                                break;
                            case "Object":
                            case "Array":
                                serial[len++] = stringify(object[key]);
                                break;
                            case "null":
                                serial[len++] = null;
                                break;
                        }
                    }
                }
                serial[len++] = type.end;
                return serial.join("");
            }
            
            var type = lib.utils.getType(object);
            
            if (type == "Object" || type == "Array") {
                return stringify(object);
            } else {
                throw new TypeError("Cannot stringify function to JSON");
            }
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.dom) throw new Error("lib.dom already defined");
    lib.dom = {
        byId: function byId(id) {
            return lib.document.getElementById(id);
        },
        
        byTag: function byTag(name, element) {
            return (element || lib.document).getElementsByTagName(name);
        },
        
        byClass: function(klass, tag, element) {
            if (typeof tag == "object" && typeof element == "undefined") {
                element = tag;
                tag = undefined;
            }
            
            if (lib.document.getElementsByClassName) {
                var elements = (element || lib.document).getElementsByClassName(klass),
                    nodeName = tag ? new RegExp("\\b" + tag + "\\b", "i") : null,
                    returnElements = [];
                for (var i = 0; i < elements.length; i++)
                    if (!nodeName || nodeName.test(elements[i].nodeName))
                        returnElements.push(elements[i]);
                
                return returnElements;
            } else {
                tag = tag || "*";
                element = element || lib.document;
                
                if (lib.document.evaluate) {
                    var classes = klass.split(" "),
                        classesToCheck = "",
                        xhtmlNamespace = "http://www.w3.org/1999/xhtml",
                        namespaceResolver = (lib.document.documentElement.namespaceURI === xhtmlNamespace)
                                            ? xhtmlNamespace : null,
                        returnElements = [],
                        elements,
                        node;
                    
                    for (var i = 0; i < classes.length; i++)
                        classesToCheck += "[contains(concat(' ', @class, ' '), ' " + classes[i] + " ')]";
                    
                    try {
                        elements = lib.document.evaluate(".//" + tag + classesToCheck, element, namespaceResolver, 0, null);
                    } catch (e) {
                        elements = lib.document.evaluate(".//" + tag + classesToCheck, element, null, 0, null);
                    }
                    
                    while (node = elements.iterateNext())
                        returnElements.push(node);
                    
                    return returnElements;
                } else {
                    var classes = klass.split(" "),
                        classesToCheck = [],
                        elements = (tag === "*" && element.all) ? element.all : element.getElementsByTagName(tag),
                        current,
                        returnElements = [],
                        match;
                    for (var i = 0; i < classes.length; i++)
                        classesToCheck.push(new RegExp("(^|\\s)" + classes[i] + "(\\s|$)"));
                    
                    for (var i = 0; i < elements.length; i++) {
                        match = false;
                        for (var j = 0; j < classesToCheck.length; j++){
                            match = classesToCheck[j].test(elements[i].className);
                            if (!match)
                                break;
                        }
                        if (match)
                            returnElements.push(elements[i]);
                    }
                    
                    return returnElements;
                }
            }
        },
        
        parent: function(element, klass, name) {
            klass = klass && new RegExp("(^|\\s)" + klass + "(\\s|$)");
            name = name && name.toUpperCase();
            
            while ((element = element.parentNode)
                && (klass && !klass.test(element.className)
                    || name && name != element.nodeName));
            
            return element;
        },
        
        isChild: function(element, parent) {
            while ((element = element.parentNode)) {
                if (element === parent) return true;
            }
            return false;
        },
        
        prev: function(element, klass, name) {
            klass = klass && new RegExp("(^|\\s)" + klass + "(\\s|$)");
            name = name && name.toUpperCase();
            
            while ((element = element.previousSibling)
                && (element.nodeType != 1
                    || klass && !klass.test(element.className)
                    || name && name != element.nodeName));
            
            return element;
        },
        
        next: function(element, klass, name) {
            klass = klass && new RegExp("(^|\\s)" + klass + "(\\s|$)");
            name = name && name.toUpperCase();
            
            while ((element = element.nextSibling)
                && (element.nodeType != 1
                    || klass && !klass.test(element.className)
                    || name && name != element.nodeName));
            
            return element;
        },
        
        create: function(html) {
            var d = lib.document.createElement("div"), element;
            d.innerHTML = html;
            element = d.firstChild;
            d = null;
            return this.remove(element);
        },
        
        remove: function(element) {
            var p = this.parent(element);
            return p && p.removeChild(element);
        },
        
        before: function(element, before) {
            var p = this.parent(before);
            return p && p.insertBefore(element, before);
        },
        
        after: function(element, after) {
            var p = this.parent(after);
            return p && p.insertBefore(element, after.nextSibling);
        },
        
        hasClass: function(element, klass) {
            return new RegExp("(^|\\s)" + klass + "(\\s|$)").test(element.className);
        },
        
        addClass: function(element, klass) {
            if (!this.hasClass(element, klass))
                element.className += (element.className ? " " : "") + klass;
            return element;
        },
        
        removeClass: function(element, klass) {
            element.className = element.className.replace(new RegExp("(^|\\s)" + klass + "(\\s|$)"), "$2");
            return element;
        },
        
        toggleClass: function(element, klass) {
            if (this.hasClass(element, klass)) {
                return this.removeClass(element, klass);
            } else {
                return this.addClass(element, klass);
            }
        },
        
        getStyle: function(element, property, pseudoElement) {
            var value = null, inline = false;
            if (lib.window && lib.window.getComputedStyle) {
                value = lib.window.getComputedStyle(element, pseudoElement || null)[property];
            } else if (element.currentStyle) {
                value = element.currentStyle[property];
            } else {
                value = element.style[property];
                inline = true;
            }
            
            if (!value && !inline) {
                return element.style[property];
            } else {
                return value;
            }
        }
    };
})(lib);
(function(lib, undefined) {
    if (lib.dom.NodeList) throw new Error("lib.dom.NodeList already defined");
    
    function NodeList(elements) {
        if (this == lib.dom) return new NodeList(elements);
        
        this.items = [];
        this.length = 0;
        
        if (!elements) return;
        
        elements = lib.array.toArray(elements);
        
        for (var i = 0; i < elements.length; i++) {
            if (elements[i].nodeType && lib.array.inArray([1, 9], elements[i].nodeType)) {
                this.length = this.push(elements[i]);
            }
        }
    };
    
    lib.extend(NodeList.prototype, {
        toString: function toString() {
            return this.items.join(", ");
        },
        
        valueOf: function valueOf() {
            return this;
        },
        
        length: 0,
        
        item: function item(index) {
            return this.items[index];
        },
        
        push: function push(node) {
            return this.length = this.items.push(node);
        },
        
        pop: function pop() {
            var node = this.items.pop();
            this.length = this.items.length;
            return node;
        },
        
        unshift: function unshift(node) {
            return this.length = this.items.unshift(node);
        },
        
        shift: function shift() {
            var node = this.items.shift();
            this.length = this.items.length;
            return node;
        },
        
        reverse: function reverse() {
            this.items.reverse();
            return this;
        },
        
        sort: function sort(callback) {
            this.items.sort(callback);
            return this;
        },
        
        splice: function splice() {
            var out = Array.prototype.splice.apply(this.items, arguments);
            clean.call(this);
            this.length = this.items.length;
            return new NodeList(out);
        },
        
        concat: function concat() {
            var out = this.items;
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof NodeList) {
                    for (var j = 0; j < arguments[i].length; j++) {
                        out.push(arguments[i].item(j));
                    }
                }
            }
            return new NodeList(out);
        },
        
        subtract: function subtract() {
            var outPrev = this.items,
                outCurr = [];
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof NodeList) {
                    for (var j = 0; j < outPrev.length; j++) {
                        var found = false;
                        for (var k = 0; k < arguments[i].length; k++) {
                            if (outPrev[j] == arguments[i].item(k)) {
                                found = true;
                            }
                        }
                        if (!found) outCurr.push(outPrev[j]);
                    }
                    outPrev = outCurr;
                    outCurr = [];
                }
            }
            return new NodeList(outPrev);
        },
        
        slice: function slice(begin, end) {
            var out = this.items.slice(begin, end);
            return new NodeList(out);
        },
        
        toArray: function toArray() {
            var out = new Array(this.items.length);
            for (var i = 0; i < this.items.length; i++) {
                out[i] = this.items[i];
            }
            return out;
        },
        
        forEach: function forEach(callback, thisObject) {
            lib.array.forEach(this.items, callback, thisObject)
        },
        
        every: function every(callback, thisObject) {
            return lib.array.every(this.items, callback, thisObject)
        },
        
        some: function some(callback, thisObject) {
            return lib.array.some(this.items, callback, thisObject);
        },
        
        filter: function filter(callback, thisObject) {
            return lib.array.filter(this.items, callback, thisObject);
        },
        
        map: function map(callback, thisObject) {
            return lib.array.map(this.items, callback, thisObject)
        },
        
        byTag: function byTag(tag) {
            var out = [];
            for (var i = 0; i < this.items.length; i++) {
                var elems = lib.dom.byTag(tag, this.items[i]);
                for (var j = 0; j < elems.length; j++) {
                    out.push(elems[j]);
                }
            }
            
            return new NodeList(out);
        },
        
        byClass: function byClass(klass, tag) {
            var out = [];
            for (var i = 0; i < this.items.length; i++) {
                var elems = lib.dom.byClass(klass, tag, this.items[i]);
                for (var j = 0; j < elems.length; j++) {
                    out.push(elems[j]);
                }
            }
            
            return new NodeList(out);
        }
    });
    
    function clean() {
        var out = [];
        for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].nodeType && lib.array.inArray([1, 9], this.items[i].nodeType)) {
                out.push(this.items[i]);
            }
        }
        this.items = out;
    }
    
    lib.dom.NodeList = NodeList;
})(lib);
(function() {
    var event = {
        add: function(target, type, callback) {
            var _type = type.toLowerCase();
            this.initEventProperty(target, type);
            target._events[type].callbacks[lib.guid(callback)] = callback;
            
            if (this.w3c) {
                target.addEventListener(type, callback, false);
            } else if (this.ie) {
                target._events[type].handle = null;
                target._events[type].IECallbacks = {};
                target._events[type].hasAttribute = false;
                target._events[type].supported = typeof target["on" + _type] === "object"
                                                 || typeof target["on" + _type] === "function";
                
                if (target._events[type].supported) {
                    if (target["on" + _type] !== null) this.addIEAttributeEvent(target, type);
                    
                    var _callback = lib.bind(function() {
                        if (typeof callback.attributeEvent == "undefined") {
                            var event = fixIEEvent(window.event, target);
                        } else {
                            var event = fixIEEvent(window.event, null);
                        }
                        
                        return callback.apply(target, [event]);
                    }, this);
                    target._events[type].IECallbacks[callback._guid] = _callback;
                    target.attachEvent("on" + type, _callback);
                } else {
                    target._events[type].handle = function(event) {
                        this.event = extendIEEventSafe(this.event, event);
                        this.event = fixIEEvent(this.event);
                        return this.callback.apply(this.event.currentTarget, [this.event]);
                    };
                    
                    var _target = (target == document) ? document.documentElement : target;
                    if (typeof target.libEvent == "undefined") {
                        target.libEvent = 0;
                        if (target != _target) _target.libEvent = 0;
                    }
                    
                    _target.attachEvent("onpropertychange", function(event) {
                        if (event.propertyName === "libEvent") {
                            if (target._events[type].handle.event
                                && target == target._events[type].handle.event.currentTarget
                                && target._events[type].handle.dispatched === false) {
                                target._events[type].handle.dispatched = true;
                                return lib.bind(target._events[type].handle, target._events[type].handle)(event);
                            }
                        }
                    });
                }
            }
        },
        
        initEventProperty: function(target, type) {
            if (typeof target._events === "undefined") target._events = {};
            if (typeof target._events[type] === "undefined") target._events[type] = {};
            if (typeof target._events[type].callbacks === "undefined") target._events[type].callbacks = {};
        },
        
        addIEAttributeEvent: function(target, type) {
            this.initEventProperty(target, type);
            target._events[type].hasAttribute = true;
            
            var _type = type.toLowerCase(),
                e = target["on" + _type];
            target["on" + _type] = null;
            e.attributeEvent = true;
            this.add(target, type, e);
        },
        
        remove: function(target, type, callback) {
            if (typeof type === "undefined") {
                for (type in target._events) {
                    this.remove(target, type);
                }
            }
            
            if (typeof target._events === "object" && typeof target._events[type] === "object") {
                if (typeof callback === "function") {
                    if (this.w3c) {
                        target.removeEventListener(type, callback, false);
                    } else if (this.ie && target._events[type].supported) {
                        var _callback = target._events[type].IECallbacks[callback._guid];
                        target.detachEvent("on" + type, _callback);
                        delete target._events[type].IECallbacks[callback._guid];
                    }
                    delete target._events[type].callbacks[callback._guid];
                } else {
                    for (i in target._events[type].callbacks) {
                        this.remove(target, type, target._events[type].callbacks[i]);
                    }
                }
            }
        },
        
        dispatch: function(target, type, properties) {
            if (this.w3c) {
                switch (this.events[type]) {
                    case this.types.mouse:
                        return this.dispatchMouseEvent(target, type, properties);
                        break;
                    case this.types.keyboard:
                        return this.dispatchKeyboardEvent(target, type, properties);
                        break;
                    case this.types.html:
                        return this.dispatchHTMLEvent(target, type, properties);
                        break;
                    case this.types.dom: // not implemented
                    case this.types.wheel: // not implemented
                    default:
                        return this.dispatchUIEvent(target, type, properties);
                        break;
                }
            } else if (this.ie) {
                return this.dispatchIEEvent(target, type, properties);
            }
        },
        
        dispatchMouseEvent: function(target, type, properties) {
            if (typeof properties === "undefined") var properties = {};
            var eventProperties = {
                    bubbles: true,
                    cancelable: true,
                    detail: 1,
                    screenX: 0,
                    screenY: 0,
                    clientX: 0,
                    clientY: 0,
                    ctrlKey: false,
                    altKey: false,
                    shiftKey: false,
                    metaKey: false,
                    button: 0,
                    relatedTarget: null
                },
                customProperties = lib.object.subtract(properties, eventProperties);
            lib.extend(eventProperties, properties || {});

            var event = document.createEvent("MouseEvents");
            event.initMouseEvent(type, eventProperties.bubbles, eventProperties.cancelable, window,
                eventProperties.detail, eventProperties.screenX, eventProperties.screenY, eventProperties.clientX,
                eventProperties.clientY, eventProperties.ctrlKey, eventProperties.altKey, eventProperties.shiftKey,
                eventProperties.metaKey, eventProperties.button, eventProperties.relatedTarget);
            lib.extend(event, customProperties);
            return target.dispatchEvent(event);
        },
        
        dispatchKeyboardEvent: function(target, type, properties) {
            if (typeof properties === "undefined") var properties = {};
            var eventProperties = {
                    bubbles: true,
                    cancelable: true,
                    ctrlKey: false,
                    altKey: false,
                    shiftKey: false,
                    metaKey: false,
                    keyCode: 9,
                    charCode: 0
                },
                customProperties = lib.object.subtract(properties, eventProperties);
            lib.extend(eventProperties, properties || {});
            
            var event = document.createEvent("KeyboardEvent");
            if (typeof event.initKeyboardEvent === "undefined" && event.initKeyEvent) {
                event.initKeyboardEvent = event.initKeyEvent;
            }
            
            event.initKeyboardEvent(type, eventProperties.bubbles, eventProperties.cancelable, window,
                eventProperties.ctrlKey, eventProperties.altKey, eventProperties.shiftKey, eventProperties.metaKey,
                eventProperties.keyCode, eventProperties.charCode);
            lib.extend(event, customProperties);
            return target.dispatchEvent(event);
        },
        
        dispatchHTMLEvent: function(target, type, properties) {
            if (typeof properties === "undefined") var properties = {};
            var eventProperties = {
                    bubbles: true,
                    cancelable: true
                },
                customProperties = lib.object.subtract(properties, eventProperties);
            lib.extend(eventProperties, properties || {});
            
            var event = document.createEvent("HTMLEvents");
            event.initEvent(type, eventProperties.bubbles, eventProperties.cancelable);
            lib.extend(event, customProperties);
            return target.dispatchEvent(event);
        },
        
        dispatchUIEvent: function(target, type, properties) {
            if (typeof properties === "undefined") var properties = {};
            var eventProperties = {
                    bubbles: true,
                    cancelable: true,
                    detail: null
                },
                customProperties = lib.object.subtract(properties, eventProperties);
            lib.extend(eventProperties, properties || {});
            
            var event = document.createEvent("UIEvents");
            event.initUIEvent(type, eventProperties.bubbles,
                              eventProperties.cancelable,
                              window,
                              eventProperties.detail);
            lib.extend(event, customProperties);
            return target.dispatchEvent(event);
        },
        
        dispatchIEEvent: function(target, type, properties) {
            var _type = type.toLowerCase();
            if (typeof target._events === "undefined") {
                if (typeof target["on" + _type] === "object" || typeof target["on" + _type] === "function") {
                    this.addIEAttributeEvent(target, type);
                } else {
                    return;
                }
            }
            
            if (typeof target._events[type] === "undefined") return;
            
            properties = properties || {};
            if (target._events[type].supported) {
                var event = document.createEventObject();
                lib.extend(event, properties);
                return target.fireEvent("on" + type, event);
            } else {
                var event = lib.extend(properties, {
                    target: target,
                    type: type,
                    currentTarget: target
                });
                
                while (target) {
                    if (target._events && target._events[type]) {
                        for (var i in target._events[type].callbacks) {
                            if (typeof properties.safe == "boolean" && properties.safe === true) {
                                target._events[type].handle.callback = target._events[type].callbacks[i];
                                target._events[type].handle.event = event;
                                target._events[type].handle.dispatched = false;
                                
                                if (event.currentTarget == document) {
                                    document.documentElement.libEvent++;
                                } else {
                                    event.currentTarget.libEvent++;
                                }
                            } else {
                                
                                if (target._events[type].callbacks[i].call(
                                        target, fixIEEvent(event, target)) === false) {
                                    event.preventDefault();
                                    event.stopPropagation();
                                }
                            }
                        }
                    }
                    target = event.currentTarget = !event.cancelBubble && target.parentNode;
                }
                return !(event.returnValue === false);
            }
        },
                
        w3c: (document.addEventListener) ? true : false,
        ie: (document.attachEvent && !document.addEventListener) ? true : false,
        
        types: {
            mouse: 1,
            keyboard: 2,
            html: 3,
            dom: 4,
            wheel: 5
        }
    };
    
    event.events = {
        click: event.types.mouse,
        dblclick: event.types.mouse,
        mousedown: event.types.mouse,
        mouseup: event.types.mouse,
        mouseover: event.types.mouse,
        mousemove: event.types.mouse,
        mouseout: event.types.mouse,
        keypress: event.types.keyboard,
        keydown: event.types.keyboard,
        keyup: event.types.keyboard,
        load: event.types.html,
        unload: event.types.html,
        abort: event.types.html,
        error: event.types.html,
        resize: event.types.html,
        scroll: event.types.html,
        select: event.types.html,
        change: event.types.html,
        submit: event.types.html,
        reset: event.types.html,
        focus: event.types.html,
        blur: event.types.html,
        DOMFocusIn: event.types.dom,
        DOMFocusOut: event.types.dom,
        DOMActivate: event.types.dom,
        DOMSubtreeModified: event.types.dom,
        DOMNodeInserted: event.types.dom,
        DOMNodeRemoved: event.types.dom,
        DOMNodeRemovedFromDocument: event.types.dom,
        DOMNodeInsertedIntoDocument: event.types.dom,
        DOMAttrModified: event.types.dom,
        DOMCharacterDataModified: event.types.dom,
        mousewheel: event.types.wheel,
        DOMMouseScroll: event.types.wheel
    };
    
    function fixIEEvent(event) {
        event.target = event.target || event.srcElement;
        event.currentTarget = (arguments[1] && arguments[1].nodeName)
                              ? arguments[1]
                              : (arguments[1] === null)
                                ? null : event.currentTarget;
        
        if (arguments[1] && arguments[1].nodeName && event.toElement && event.toElement.nodeName) {
            event.relatedTarget = (event.toElement == event.target) ? event.fromElement : event.toElement;
        }
        
        event.preventDefault = function() {
            event.returnValue = false;
        };
            
        event.stopPropagation = function() {
            event.cancelBubble = true;
        };
        
        return event;
    };
    
    function extendIEEventSafe(target) {
        var props = ["altKey", "attrChange", "attrName", "bubbles", "button", "cancelable", "charCode",
                    "clientX", "clientY", "ctrlKey", "currentTarget", "data", "detail", "eventPhase",
                    "fromElement", "handler", "keyCode", "layerX", "layerY", "metaKey", "newValue",
                    "offsetX", "offsetY", "originalTarget", "pageX", "pageY", "prevValue", "relatedNode",
                    "relatedTarget", "screenX", "screenY", "shiftKey", "srcElement", "target", "toElement",
                    "view", "wheelDelta", "which"];
        for (var i, k = 0; ++k < arguments.length;) {
            for (i in props) {
                if (!(typeof arguments[k][props[i]] === "undefined"))
                target[props[i]] = arguments[k][props[i]];
            }
        }
        
        target.originalEvent = arguments[1];
        return target;
    };
    
    lib.event = {
        add: lib.bind(event.add, event),
        remove: lib.bind(event.remove, event),
        dispatch: lib.bind(event.dispatch, event),
        support: testSupport,
        compat: {
            DOMAttrModified: DOMAttrModified
        }
    }
    
    function testSupport(type) {
        if (typeof type !== "string") return null;
        switch (type) {
            case "DOMAttrModified":
                return testDOMAttrModified();
                break;
            default:
                return null;
                break;
        }
    };
    
    function testDOMAttrModified() {
        if (event.w3c) {
            var capable = false,
                div = document.createElement("div");
            div.addEventListener("DOMAttrModified", function(event) {
                if (event && event.target && event.target === div)
                    capable = true;
            }, false);
            div.style.display = "none";
            return capable;
        } else {
            return true;
        }
    };
    
    // Webkit DOMAttrModified workaround
    function DOMAttrModified(target, path) {
        if (!event.w3c) return;
        
        event.initEventProperty(target, "DOMAttrModified");
        if (typeof target._events["DOMAttrModified"].oldProperties === "undefined")
            target._events["DOMAttrModified"].oldProperties = {};
        
        var array = path.split("."),
            last = array[array.length -1];
        
        function find() {
            var newValue = target[array[0]];
            for (var i = 1; i < array.length; i++) {
                newValue = newValue[array[i]];
            }
            return newValue;
        }
        
        var guid = lib.guid();
        var prevValue = target._events["DOMAttrModified"].oldProperties[guid] = find();
        var interval = window.setInterval(function() {
            var newValue = find();
            if (prevValue !== newValue) {
                event.dispatch(target, "DOMAttrModified", {
                    MODIFICATION: 1,
                    ADDITION: 2,
                    REMOVAL: 3,
                    attrChange: 1,
                    attrName: path,
                    newValue: newValue,
                    prevValue: prevValue,
                    compat: {
                        stop: function() {
                            window.clearInterval(interval);
                            delete target._events["DOMAttrModified"].oldProperties[guid];
                        }
                    }
                });
                prevValue = newValue;
            }
        }, 15);
    };
})();
(function() {
    var dimensions = {
        innerWidth: window.innerWidth || document.documentElement.offsetWidth,
        innerHeight: window.innerHeight || document.documentElement.offsetHeight,
        scrollWidth: null,
        scrollHeight: null,
        scrollX: window.scrollX || document.documentElement.scrollLeft,
        scrollY: window.scrollY || document.documentElement.scrollTop
    };
    
    lib.ready(function() {
        lib.extend(dimensions, {
            scrollWidth: document.documentElement.scrollWidth,
            scrollHeight: document.documentElement.scrollHeight
        });
    });
    
    dimensions.get = function get(elem) {
        
    };
    
    lib.dimensions = dimensions;
})();
if (!window.opera) try { document.execCommand("BackgroundImageCache", false, true); } catch(e) {};
lib.widget = function(prototype) {
    var klass = function(prototype) {
        if (!(this.name = prototype.name)) throw new Error("widget class must have name defined.");
        this.construct = lib.extend(function(element, settings) {
            lib.event.add(document, "libDestroy", lib.bind(this.destroy, this));
            this._bound = [];
            lib.extend(this, settings, {element: element}).construct();
        }, {
            prototype: lib.extend(this.prototype, prototype)
        });
    };
    
    klass.prototype = {
        run: function(elements, settings) {
            elements = (elements)
                       ? (elements instanceof Array)
                           ? elements
                           : (elements instanceof NodeList || elements instanceof HTMLCollection)
                               ? lib.array(elements)
                               : [elements]
                       : [];
            
            for (var i = 0; i < elements.length; i++) {
                var widgets = elements[i].widgets;
                if (!widgets)
                    widgets = elements[i].widgets = {};
                
                (widgets[this.name] = widgets[this.name]
                                      ? lib.extend(widgets[this.name], settings)
                                      : new this.construct(elements[i], settings));
            }
        },
        
        destroy: function() {
            this.destroy();
            this.removeEvent();
            this.unbind.apply(this._bound);
            delete this.element.widgets[this.name];
            this.element = null;
        },
        
        prototype: {
            construct: function() {},
            destroy: function() {},
            bind: function() {
                for (var i = 0, a = arguments; i < a.length; i++) {
                    this.element[a[i]] = lib.bind(this[a[i]], this);
                    this._bound.push(a[i]);
                }
            },
            unbind: function() {
                for (var i = 0, a = arguments; i < a.length; i++)
                    delete this.element[a[i]]
            }
        }
    };
    
    return lib.widget[prototype.name] = new klass(prototype);
};
