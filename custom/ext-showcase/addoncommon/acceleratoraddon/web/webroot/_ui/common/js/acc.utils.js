if(typeof ACC == "undefined") {
    ACC = function() {};
}

/**
 * Utils methods like: overloading, calling function by name, get variables from global ACC.addons in secure way (no undefined)
 * @author: dariusz.malachowski
 */
ACC.Utils = {

    /**
     * util method for overloading js class methods
     * sample usage:
     * function Jedi() {
     *   ACC.Utils.overload(this, "useForce", function() {
     *      method impl
     *   });
     *   ACC.Utils.overload(this, "useForce", function(name) {
     *      method 2 impl
     *   });
     * }
     * @param _class
     * @param _fname
     * @param _fn
     */
    overload: function(_class, _fname, _fn) {
        var _fnclass = _class[_fname];
        _class[_fname] = function() {
            if(_fn.length == arguments.length) {
                return _fn.apply(this, arguments);
            } else if(typeof _fnclass == "function") {
                return _fnclass.apply(this, arguments);
            }
        };
    },

    /**
     * Allow calling function/method by name
     * @param functionName
     * @param context
     * @returns {*}
     */
    callByName: function(functionName, context /*, args */) {
        var args = Array.prototype.slice.call(arguments).splice(2,2);
        var namespaces = functionName.split(".");
        var func = namespaces.pop();

        for(var i = 0; i < namespaces.length; i++) {
            context = context[namespaces[i]];
        }

        if(typeof context[func] == 'function') {
            return context[func].apply(this, args);
        } else {
            console.debug('Function does not exist: ' + func);
        }
    },

    /**
     * Get js variables defined by BeforeViewJsPropsHandlerAdaptee
     * @param _globalAddons
     */
    jsProperty: function(_globalAddons, _default) {
        var DEFAULT = _default === undefined ? "" : _default;
        var ADDONS = _globalAddons;

        function getVariableByAddon(_addon, _variable, _default) {
            if(ADDONS === undefined || ADDONS[_addon] === undefined || ADDONS[_addon][_variable] === undefined) {
                console.debug("Variable " + _variable + " from addon: " + _addon + " is undefined");
                return _default === undefined ? DEFAULT : _default;
            }
            return ADDONS[_addon][_variable];
        }

        ACC.Utils.overload(this, "get", function(_addon, _variable) {
            return getVariableByAddon(_addon, _variable);
        });

        ACC.Utils.overload(this, "get", function(_addon, _variable, _default) {
            return getVariableByAddon(_addon, _variable, _default);
        });
    }

};

/**
 * Helper function for accesing addons js variables. Sample usage: JSProps('vouchers','variable');
 * @returns {*}
 * @constructor
 */
var JSProps = function() {
    var _helper = new ACC.Utils.jsProperty(ACC.addons);
    var _method = _helper['get'];
    return _method.apply(_helper, arguments);
};

