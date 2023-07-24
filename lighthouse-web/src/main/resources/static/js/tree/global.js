Number.prototype.to3ps = function(d) {
    d = d || 0;
    var s = String(this.toFixed(d));
    var g = /(\d{1,3})(?=(\d{3})+$)/g;
    if (d < 1)
        return s.replace(g, "$1,");
    var i = s.length - d;
    var p = s.substr(0, i - 1);
    var x = s.substr(i);
    p = p.replace(g, "$1,");
    return p + '.' + x;
}
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        "S" : this.getMilliseconds()
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
var App = {
    editRegion : {
        url : '/javascript/dtree/edit.php'
    },
    defaultViewSet : {
        theme : 'default',
        editRegion : {
            type : "none",
            show : "true",
            resizable : false,
            position : ["center", "center"],
            width : 420,
            height : 180
        }
    }
};
App.viewSet = App.defaultViewSet;
App.options = function(self, cfg) {
    self.empty();
    $.each(cfg.data, function(i, n) {
        var k = cfg.keyField, v = cfg.displayField
            , a = $('<option></option>').appendTo(self);
        a.attr('value', (k? n[k]:i));
        a.append(cfg.render? cfg.render(n):(v? n[v]:n));
    });
    return self;
};
App.inputBox = function(label, callback, params, value){
    var b = $('<div style="text-align: center; margin: 8px"></div>');
    var t = $('<textarea></textarea>').width(320).height(80).append(value);
    var f = b.clone();
    var d = $('<div></div>').dialog( {
        title: mac.getMsg(label, params),
        modal : true,
        width : 400,
        resizable : false,
        autoOpen : true
    }).append(b.append(t)).append(f);
    $('<button name="ok">' + Msg.ok + '</button>').click(function(){
        d.dialog('close');
        callback(t.val());
    }).button().appendTo(f);
    $('<button name="cancel">' + Msg.cancel + '</button>').click(function(){
        d.dialog('close');
    }).button().appendTo(f);
}
App.setLanguage = function(lan) {
    $('.text').each(function(n, c) {
        var ec = $(c);
        var msg = ec.html();
        if (msg && msg.length > 4 && msg.substr(0, 4) == 'Msg.')
            ec.html(eval('(' + msg + ')'));
    });
};
App.wait = function(msg, params) {
    var dlg = $('<div><h3 align="center">' + mac.getMsg(msg, params) + '</h3></div>');
    dlg.dialog( {
        modal : true,
        resizable : false,
        width : 400,
        autoOpen : true,
        modal : true,
        beforeclose : function() {
            return false;
        }
    });
    return dlg;
};
App.alert = function(msg, params) {
    var dlg = $('<div><h3 align="center">' + mac.getMsg(msg, params) + '</h3></div>');
    var d = dlg.dialog( {
        modal : true,
        width : 400,
        resizable : false,
        autoOpen : true,
        buttons : {
            "纭畾" : function() {
                $(this).dialog("close");
            }
        }
    });
    $(document).keydown(function(e){
        switch(e.keyCode){
            case 13:
                d.dialog("close");
                break;
            case 27:
                d.dialog("close");
                break;
        }
    });
    return dlg;
};
App.confirm = function(msg, callback, params, btns) {
    var dlg = $('<div><h3 align="center">' + mac.getMsg(msg, params) + '</h3></div>');
    btns = btns || {
        'Cancel' : function() {
            $(this).dialog("close");
        },
        'OK' : function() {
            $(this).dialog("close");
            callback();
        }
    }
    var d = dlg.dialog( {
        modal : true,
        width : 400,
        resizable : false,
        autoOpen : true,
        buttons : btns
    });
    $(document).keydown(function(e){
        switch(e.keyCode){
            case 13:
                callback();
                d.dialog("close");
                break;
            case 27:
                d.dialog("close");
                break;
        }
    });
    return dlg;
};
App.open = function(l, i) {
    var g = l.action;
    if (!g) {
        alert("No action!");
        return
    }
    var k = App[g];
    var j = App.viewSet[g];
    var h = $("#" + g);
    if (h.length > 0) {
        h.dialog("moveToTop");
        mac.alert(Msg.i9.opened);
        return h
    }
    h = $('<div id="' + g + '" class="window"></div>');
    h.callback = i;
    h.type = j.type;
    h.params = l.params;
    h.dialog($.extend( {
        title : l.title,
        width : l.width || j.width,
        height : l.height || j.height,
        minWidth : l.width || j.minWidth,
        minHeight : l.height || j.minHeight,
        position : j.position,
        resizable : j.resizable,
        modal : l.modal,
        autoOpen : true,
        close : function() {
            if (l.onClose) {
                l.onClose()
            }
            $(this).remove()
        }
    }, l.dlgCfg));
    k.ui = h;
    if (l.rpath) {
        k.rpath = l.rpath;
        k.ui.load(l.rpath + k.url)
    } else {
        k.rpath = "";
        k.ui.load(k.url)
    }
    return h
};
App.popup = function(m, j) {
    var n = m.action;
    if (!n) {
        alert("No action!");
        return
    }
    var l = App[n];
    var k = App.viewSet[n] || {};
    $.extend(k, m);
    var h = $("<div></div>").mac("dialog", k);
    if (j) {
        h.callback = j
    }
    if (m.params) {
        h.params = m.params
    }
    l.ui = h;
    var i = mac.wait(Msg.info.loading);
    l.rpath = m.rpath ? m.rpath : "";
    h.load(l.rpath + l.url, function() {
        h.show();
        i.remove()
    });
    return h
};
//p, callback, container, onLoad
App.show = function(p, cb, cp, ol) {
    cp = cp || $("#main").css("height", "");
    var act = p.action;
    if (!act) {
        alert("No " + act + " action!");
        return
    }
    var mo = App[act];
    mo.ui = $('<div id="' + act + '" class="panel loading"></div>');
    if (App.timer)
        window.clearInterval(App.timer)
    cp.empty().append(mo.ui);
    mo.ui.params = p.params;
    var w = mac.wait(Msg.loading);
    j.ui.load(j.url, function(a) {
        w.remove();
        mo.ui.removeClass("loading")
        if(ol) ol();
    });
    mo.ui.callback = cb;
    return j.ui
};