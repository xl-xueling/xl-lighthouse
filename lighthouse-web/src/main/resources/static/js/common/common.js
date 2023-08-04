/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var $ = jQuery.noConflict();



(window.onload = function () {
    Calculate = {
        /**
         * @return {string}
         */
        Avg:function(arr){
              let sum = 0;
              for(var i = 0; i < arr.length; i++){
                  sum += Number(arr[i]);
              }
              let avg  = sum / arr.length;
              return avg.toFixed(2);
          }
    };
    Link = {
        /**
         * @return {string}
         */
        GetUrlRelativePath:function (url){
            if(url.indexOf("//") !== -1){
                var arrUrl = url.split("//");
                var start = arrUrl[1].indexOf("/");
                return arrUrl[1].substring(start);
            }
            return url;
        },
        CombineParams:function (baseUrl,params) {
            let arr = [];
            Object.keys(params).forEach(k => !Validate.isNull(params[k]) && params[k] !== -1 && arr.push(`${k}=${params[k]}`));
            if (baseUrl.search(/\?/) === -1) {
                baseUrl += `?${arr.join('&')}`
            } else {
                baseUrl += `&${arr.join('&')}`
            }
            return baseUrl;
        }
    };
    MsgBox = {
        Alert: function (msg) {
            if(msg && msg.length < 35){
                alertMsg(msg);
            }else{
                alertMoreMsg(msg);
            }
            btnNo();
        },
        AlertRequestError: function (data) {
            if(data && !Validate.isNull(data.msg)){
                let value = I18N.translate(data.msg);
                MsgBox.Alert(value);
            }else{
                MsgBox.Alert("System Error!");
            }
        },
        AlertWithCallBack: function (msg,callback) {
            if(msg && msg.length < 35){
                alertMsg(msg);
            }else{
                alertMoreMsg(msg);
            }
            btnAlertNo(callback);
        },
        Confirm: function (msg, callback) {
            if(msg && msg.length < 35){
                alertConfirm(msg);
            }else{
                alertMoreConfirm(msg);
            }
            btnOk(callback);
            btnNo();
        },
        Redirect : function(href){
            if(Validate.isNull(href)){
                return;
            }
            if(href.endWith("#")){
                href = href.substring(0,href.length - 1);
            }
            href = Link.GetUrlRelativePath(href);
            window.location.href = Encrypt.encryptUrl(href);
        },
        Open:function(href){
            if(href.endWith("#")){
                href = href.substring(0,href.length - 1);
            }
            href = Link.GetUrlRelativePath(href);
            let url = Encrypt.encryptUrl(href);
            window.open(url,"_blank");
        },
        subRedirect:function(div,href){
            COMMON.loadURL(div,href);
        }
    };
    Encrypt = {
        encrypt:function(word){
            const iv = "e4883f3c48f6e8867265cdbc75b948aa";
            const salt = "fb1837e8d99840b70d91295965851835";
            const aesUtil = new AesUtil(128, 1000);
            return aesUtil.encrypt(salt, iv, "1234567812345678", word);
        },
        encryptParams:function(paramsObj){
            paramsObj.t = new Date().getTime();
            const paramStr = JSON.stringify(paramsObj)
            const p = Encrypt.encrypt(paramStr);
            const vk = hex_md5(paramStr);
            let encryptObj = {};
            encryptObj.p = p;
            encryptObj.vk = vk;
            return encryptObj;
        },
        encryptUrl:function(url){
            let index = url.indexOf("?");
            let obj = {};
            obj.t = new Date().getTime();
            let base;
            let encryptFlag = false;
            if(index === -1){
                base = url;
            }else{
                base = url.substring(0,index);
                let params = url.substring(index + 1,url.length);
                let paramArr = params.split("&");
                for (var i = 0;i < paramArr.length;i++){
                    let param = paramArr[i];
                    let key = param.split("=")[0];
                    let value = param.split("=")[1];
                    if(key === 'vk'){
                        encryptFlag = true;
                    }
                    obj[key] = value;
                }
            }
            if(encryptFlag){
                return url;
            }else{
                let paramStr = JSON.stringify(obj)
                let p =  encodeURIComponent(Encrypt.encrypt(paramStr));
                let vk = hex_md5(paramStr);
                return base + "?p=" + p + "&vk=" + vk;
            }
        }
    };
    Validate = {
        isNum:function(str){
            let re = /^[0-9]*$/;
            return re.test(str);
        },
        isPhone:function(str){
            let re = /^1[3|4|5|6|7|8][0-9]{9}$/;
            return re.test(str);
        },
        isNumOrLetter:function(str){
            let re = /^[A-Za-z0-9]+$/;
            return re.test(str);
        },
        isNumLetterOrLine:function(str){
            let re = /^[A-Za-z0-9_]+$/;
            return re.test(str);
        },
        isChinese:function(str){
            let re=/[^\u4e00-\u9fa5]/;
            return !re.test(str);
        },
        isEmail:function(str){
            let re = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
            return re.test(str);
        },
        isNull:function(str){
            return str === null || str === 'undefined' || !str || str.length === 0 || String(str).length === 0;
        },
        isExistSpecialChar:function(str){
            let pat= new RegExp("[`~!@#$^&*=|{}':;',.<>《》/?~！@#￥……&*——|{}‘；：”“'。，、？]");
            return pat.test(str);
        },
        isJson:function (str) {
            try {
                if (typeof JSON.parse(str) == "object") {
                    return true;
                }
            } catch(e) {}
            return false;
        }
    };
    PAGE = {
        tdclickCallBack:function (tdobject,callback){
            var td=$(tdobject);
            var id = td.attr("id");
            td.attr("onclick", "");
            var text=td.text();
            td.html("");
            var input=$("<input>");
            input.attr("value",text);
            input.width(td.width());
            input.height("20px");
            input.bind("blur",function(){
                var inputnode=$(this);
                var inputtext=inputnode.val();
                var tdNode=inputnode.parent();
                if(inputtext === ''){
                    tdNode.html("--");
                }else{
                    tdNode.html(inputtext);
                }
                tdNode.click(PAGE.tdclick);
                td.attr("onclick","PAGE.tdclickCallBack(this,"+callback+");");
                callback();
            });
            td.append(input);
            var t =input.val();
            input.val("").focus().val(t);
            td.unbind("click");
        },
        tdclick:function (tdobject){
            var td=$(tdobject);
            td.attr("onclick", "");
            var text=td.text();
            td.html("");
            var input=$("<input>");
            input.attr("value",text);
            input.width(td.width());
            input.height("20px");
            input.bind("blur",function(){
                var inputnode=$(this);
                var inputtext=inputnode.val();
                var tdNode=inputnode.parent();
                if(inputtext === ''){
                    tdNode.html("--");
                }else{
                    tdNode.html(inputtext);
                }
                tdNode.click(PAGE.tdclick);
                td.attr("onclick", "PAGE.tdclick(this)");
            });
            td.append(input);
            var t =input.val();
            input.val("").focus().val(t);
            td.unbind("click");
        },
        tdclick2:function (tdobject){
            var td=$(tdobject);
            td.attr("onclick", "");
            var text=td.text();
            td.html("");
            var input=$("<input>");
            input.attr("value",text);
            input.width("150px");
            input.height("20px");
            input.bind("blur",function(){
                var inputnode=$(this);
                var inputtext=inputnode.val();
                var tdNode=inputnode.parent();
                if(inputtext === ''){
                    tdNode.html("--");
                }else{
                    tdNode.html(inputtext);
                }
                tdNode.click(PAGE.tdclick);
                td.attr("onclick", "PAGE.tdclick2(this)");
            });
            td.append(input);
            var t =input.val();
            input.val("").focus().val(t);
            td.unbind("click");
        },
    };
    COMMON = {
        clear:function(obj){
            $(obj).val("");
        },
        getRandomString:function(len) {
            len = len || 32;
            const $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
            var maxPos = $chars.length;
            var pwd = '';
            for (var index = 0; index < len; index++) {
                pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
        showLoading:function(){
            $("#loading").show();
            $("#popLayer").show();
        },
        hiddenLoading:function(){
            $("#loading").hide();
            $("#popLayer").hide();
        },
        hideAllPop:function (){
            $(".pop-dialog").modal('hide');
            COMMON.hiddenLoading();
        },
        loadURL:function(selector,url){
            document.documentElement.style.overflowY = 'scroll';
            $("body").css("padding-right","0px");
            if(url.indexOf("?") === -1){
                url += "?isSub=1";
            }else{
                url += "&isSub=1";
            }
            url = Encrypt.encryptUrl(url);
            $(selector).empty();
            COMMON.showLoading();
            jQuery.get(url,function(data,status,xhr){
                let sessionStatus = xhr.getResponseHeader('sessionStatus');
                if(sessionStatus && sessionStatus === 'timeout') {
                    MsgBox.Alert($.i18n.prop('ldp_i18n_user_login_1012'));
                    MsgBox.Redirect("/login/index.shtml");
                }else{
                    jQuery(selector).html(data);
                    COMMON.hiddenLoading();
                    $(".modal-backdrop").remove();
                }
            })
        }
    };
    I18N = {
        refresh:function (){
            let elements = $(".i18n");
            elements.each(function(){
                let html = $(this).html();
                if(!Validate.isNull(html) && html.indexOf('i18n(') !== -1){
                    let res = html.replace(/i18n\((.+?)\)/g, function(match, i18nConf) {
                        let array = i18nConf.split(",")
                        let params = array.slice(1);
                        return Strings.formatArgs($.i18n.prop(array[0]), params);
                    });
                    $(this).html(res);
                    return;
                }
                let title = $(this).attr("title");
                if(!Validate.isNull(title) && title.startWith("i18n")){
                    let a = title.match(/\((.+?)\)/g);
                    let i18nCode = a[0].substring(1,a[0].length - 1);
                    if(!Validate.isNull(i18nCode) && !Validate.isNull($.i18n.prop(i18nCode))){
                        $(this).attr('title',$.i18n.prop(i18nCode));
                    }
                    return;
                }
                let placeholder = $(this).attr("placeholder");
                if(!Validate.isNull(placeholder) && placeholder.startWith("i18n")){
                    let a = placeholder.match(/\((.+?)\)/g);
                    let i18nCode = a[0].substring(1,a[0].length - 1);
                    if(!Validate.isNull(i18nCode) && !Validate.isNull($.i18n.prop(i18nCode))){
                        $(this).attr('placeholder',$.i18n.prop(i18nCode));
                    }
                    return;
                }
                let i18nCode = $(this).attr("i18n_code");
                if(!Validate.isNull(i18nCode) && !Validate.isNull($.i18n.prop(i18nCode))){
                    $(this).html($.i18n.prop(i18nCode));
                }
            });
        },
        translate:function (html){
            if(!Validate.isNull(html) && html.indexOf('i18n(') !== -1){
                return html.replace(/i18n\((.+?)\)/g, function (match, i18nConf) {
                    let array = i18nConf.split(",")
                    let params = array.slice(1);
                    return Strings.formatArgs($.i18n.prop(array[0]), params);
                });
            }else if(!Validate.isNull(html) && html.indexOf('i18n<') !== -1){
                return html.replace(/i18n<<(.+?)>>/g, function (match, i18nConf) {
                    let array = i18nConf.split("#")
                    let params = array.slice(1);
                    return Strings.formatArgs($.i18n.prop(array[0]), params);
                });
            }else{
                return html;
            }
        }
    };
    Strings = {
        format:function(format) {
            var args = Array.prototype.slice.call(arguments, 1);
            return format.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined'
                    ? args[number]
                    : match
                    ;
            });
        },
        formatArgs:function(format,args) {
            return format.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined'
                    ? args[number]
                    : match
                    ;
            });
        }
    };
    DATE = {
        formatUnixtimestamp:function(unixTimeStamp){
            if(unixTimeStamp.toString().length === 10){
                unixTimeStamp = unixTimeStamp * 1000;
            }
            let unixtimestamp = new Date(unixTimeStamp);
            let year = 1900 + unixtimestamp.getYear();
            let month = "0" + (unixtimestamp.getMonth() + 1);
            let date = "0" + unixtimestamp.getDate();
            let hour = "0" + unixtimestamp.getHours();
            let minute = "0" + unixtimestamp.getMinutes();
            let second = "0" + unixtimestamp.getSeconds();
            return year + "-" + month.substring(month.length-2, month.length)  + "-" + date.substring(date.length-2, date.length)
                + " " + hour.substring(hour.length-2, hour.length) + ":"
                + minute.substring(minute.length-2, minute.length) + ":"
                + second.substring(second.length-2, second.length);
        },
        format:function(unixTimeStamp){
            if(unixTimeStamp.toString().length === 10){
                unixTimeStamp = unixTimeStamp * 1000;
            }
            let unixtimestamp = new Date(unixTimeStamp);
            let year = 1900 + unixtimestamp.getYear();
            let month = "0" + (unixtimestamp.getMonth() + 1);
            let date = "0" + unixtimestamp.getDate();
            let hour = "0" + unixtimestamp.getHours();
            let minute = "0" + unixtimestamp.getMinutes();
            let second = "0" + unixtimestamp.getSeconds();
            return year + "-" + month.substring(month.length-2, month.length)  + "-" + date.substring(date.length-2, date.length);
        }

};

    COOKIE = {
        setCookie:function(cname,value,exdays){
                let d = new Date();
                d.setTime(d.getTime()+(exdays*24*60*60*1000));
                let expires = "expires="+d.toGMTString();
                document.cookie = cname + "=" + value + "; " + expires + ";path=/";
        },
        getCookie:function (cname){
            let name = cname + "=";
            let ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++)
            {
                var c = ca[i].trim();
                if (c.indexOf(name)===0) return c.substring(name.length,c.length);
            }
            return "";
        },
    };

    let btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            closeConfirm(this);
            closeMsg(this);
            callback();
        });
    };

    let btnNo = function () {
        $("#mb_btn_no,#mb_btn_cancel").click(function () {
            closeConfirm(this);
            closeMsg(this);
        });
    };

    let btnAlertNo = function (callback) {
        $("#mb_btn_no,#mb_btn_cancel").click(function () {
            closeConfirm(this);
            closeMsg(this);
            callback && callback();
        });
    };

    let alertMoreMsg = function(message){
        let alertObj = $("#alertdiv");
        if(alertObj.length > 0){
            return;
        }
        var str = "";
        str += "<div id=\"alertdiv\" style=\"width:550px;position:fixed;z-index:10001;_position:absolute\">";
        str += "<div class=\"box box-success box-solid2\">";
        str += "<div class=\"box-header with-border\">";
        str += "<h3 class=\"box-title\">Notice</h3>";
        str += "<div class=\"box-tools pull-right\">";
        str += "<button id=\"mb_btn_cancel\" type=\"button\" class=\"btn btn-box-tool\" data-widget=\"remove\"><i class=\"fa fa-times\"></i></button>";
        str += "</div>";
        str += "</div>";
        str += "<div class=\"box-body\" style=\"text-align: center;font-size:15px;\">";
        str += message;
        str += "</div>";
        str += "</div>";
        str += "</div>";
        $("body").append(str);
        alertObj = $("#alertdiv");
        showPop();
        let width =  $(alertObj).width();
        let height =  $(alertObj).height();
        let top = (getInner().height - height) / 2 + getScroll().top;
        let left = (getInner().width - width) / 2 + getScroll().left;
        let param = {'left': left};
        if (arguments.length === 1) {
            param['top'] = '30%';
        }
        $(alertObj).css(param);
    };

    let alertMsg = function(message){
        let alertObj = $("#alertdiv");
        if($(alertObj).length > 0){
            return;
        }
        let str = "";
        str += "<div id=\"alertdiv\" style=\"width:350px;position:fixed;z-index:10001;_position:absolute\">";
        str += "<div class=\"box box-success box-solid2\">";
        str += "<div class=\"box-header with-border\">";
        str += "<h3 class=\"box-title\">Notice</h3>";
        str += "<div class=\"box-tools pull-right\">";
        str += "<button id=\"mb_btn_cancel\" type=\"button\" class=\"btn btn-box-tool\" data-widget=\"remove\"><i class=\"fa fa-times\"></i></button>";
        str += "</div>";
        str += "</div>";
        str += "<div class=\"box-body\" style=\"text-align: center;font-size:15px;\">";
        str += message;
        str += "</div>";
        str += "</div>";
        str += "</div>";
        $("body").append(str);
        alertObj = $("#alertdiv");
        showPop();
        let width =  $(alertObj).width();
        let height =  $(alertObj).height();
        let top = (getInner().height - height) / 2 + getScroll().top;
        let left = (getInner().width - width) / 2 + getScroll().left;
        let param = {'left': left};
        if (arguments.length === 1) {
            param['top'] = '30%';
        }
        $(alertObj).css(param);
    };

    let getInner=(function() {
        if (typeof window.innerWidth !== 'undefined') {
            return function(){
                return {
                    width : window.innerWidth,
                    height : window.innerHeight
                }
            }
        } else {
            return function(){
                return {
                    width : document.documentElement.clientWidth,
                    height : document.documentElement.clientHeight
                }
            }
        }
    })();
    let getScroll=function(){
        return {
            top:document.documentElement.scrollTop || document.body.scrollTop,
            left:document.documentElement.scrollLeft || document.body.scrollLeft,
            height:document.documentElement.scrollHeight ||document.body.scrollHeight
        };
    };

    let closeMsg = function(obj){
        $("#alertdiv").remove();
        hidePop();
    };

    let alertConfirm = function(message){
        let confirmObj = $("#confirmdiv");
        if(confirmObj.length > 0){
            return;
        }
        let str = "";
        str += "<div id=\"confirmdiv\" style=\"width:350px;position:fixed;z-index:10001;_position:absolute\">";
        str += "<div class=\"box box-success box-solid2\">";
        str += "<div class=\"box-header with-border\">";
        str += "<h3 class=\"box-title\">Notice</h3>";
        str += "<div class=\"box-tools pull-right\">";
        str += "<button  id=\"mb_btn_cancel\" type=\"button\" class=\"btn btn-box-tool\" data-widget=\"remove\"><i class=\"fa fa-times\"></i></button>";
        str += "</div>";
        str += "</div>";
        str += "<div class=\"box-body\" style=\"text-align:center;\">";
        str += message;
        str += "<br><br>"
        str += "<button id='mb_btn_ok' type=\"button\" class=\"btn btn-success i18n\">"+$.i18n.prop('ldp_i18n_common_1006')+"</button>";
        str += "&nbsp;&nbsp;&nbsp;&nbsp;"
        str += "<button id='mb_btn_no' type=\"button\" class=\"btn btn-danger i18n\">"+$.i18n.prop('ldp_i18n_common_1007')+"</button>";
        str += "</div>";
        str += "</div>";
        str += "</div>";
        $("body").append(str);
        showPop();
        confirmObj = $("#confirmdiv");
        let width = $(confirmObj).width();
        let height = $(confirmObj).height();
        let top = (getInner().height - height) / 2 + getScroll().top;
        let left = (getInner().width - width) / 2 + getScroll().left;
        let param = {'left': left};
        if (arguments.length === 1) {//Vertical direction
            param['top'] = '30%';
        }
        confirmObj.css(param);
    };

    let alertMoreConfirm = function(message){
        let confirmObj = $("#confirmdiv");
        if(confirmObj.length > 0){
            return;
        }
        let str = "";
        str += "<div id=\"confirmdiv\" style=\"width:550px;position:fixed;z-index:10001;_position:absolute\">";
        str += "<div class=\"box box-success box-solid2\">";
        str += "<div class=\"box-header with-border\">";
        str += "<h3 class=\"box-title\">Notice</h3>";
        str += "<div class=\"box-tools pull-right\">";
        str += "<button  id=\"mb_btn_cancel\" type=\"button\" class=\"btn btn-box-tool\" data-widget=\"remove\"><i class=\"fa fa-times\"></i></button>";
        str += "</div>";
        str += "</div>";
        str += "<div class=\"box-body\" style=\"text-align:center;\">";
        str += message;
        str += "<br><br>"
        str += "<button id='mb_btn_ok' type=\"button\" class=\"btn btn-success i18n\">"+$.i18n.prop('ldp_i18n_common_1006')+"</button>";
        str += "&nbsp;&nbsp;&nbsp;&nbsp;"
        str += "<button id='mb_btn_no' type=\"button\" class=\"btn btn-danger i18n\">"+$.i18n.prop('ldp_i18n_common_1007')+"</button>";
        str += "</div>";
        str += "</div>";
        str += "</div>";
        $("body").append(str);
        showPop();
        confirmObj = $("#confirmdiv");
        let width = $(confirmObj).width();
        let height = $(confirmObj).height();
        let top = (getInner().height - height) / 2 + getScroll().top;
        let left = (getInner().width - width) / 2 + getScroll().left;
        let param = {'left': left};
        if (arguments.length === 1) {//Vertical direction
            param['top'] = '30%';
        }
        confirmObj.css(param);
    };


    let closeConfirm = function(obj){
        $("#confirmdiv").remove();
        hidePop();
    };

    let showPop = function(){
        let popLayer = $("#popLayer");
        popLayer.css("height",$(document).height());
        popLayer.css("width",$(document).width());
        popLayer.show();
    };

    let hidePop = function(){
        $("#popLayer").hide();
    }


})();

function html2Escape(sHtml) {
    return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
}

function switchLang(lang){
    jQuery.i18n.properties({
        name:"ldp-i18n",
        path:"/static/dist/i18n/",
        mode:"map",
        cache: true,
        language:lang,
        callback:function (){}
    })
}

String.prototype.removeLineEnd = function() {
    return this.replace(/(<.+?\s+?)(?:\n\s*?(.+?=".*?"))/g, '$1 $2')
}
String.prototype.endWith=function(endStr){
    var d=this.length-endStr.length;
    return (d>=0&&this.lastIndexOf(endStr)===d);
};

Array.prototype.sum = function (){
    let result = 0;
    for(let i = 0; i < this.length; i++) {
        result += this[i];
    }
    return result;
};

function getPrefix(prefixIndex) {
    let span = '    ';
    let output = [];
    for (let i = 0; i < prefixIndex; ++i) {
        output.push(span);
    }
    return output.join('');
}

String.prototype.startWith=function(str){
    let reg=new RegExp("^"+str);
    return reg.test(this);
};

String.prototype.blen = function() {
    let len = 0;
    for (let i=0; i<this.length; i++) {
        if (this.charCodeAt(i)>127 || this.charCodeAt(i) === 94) {
            len += 2;
        } else {
            len ++;
        }
    }
    return len;
}

String.prototype.endWith=function(str){
    let reg=new RegExp(str+"$");
    return reg.test(this);
};

Date.prototype.format = function(fmt) {
    let o = {
        "M+" : this.getMonth()+1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth()+3)/3),
        "S"  : this.getMilliseconds()
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(let k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length===1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
};

Array.prototype.remove = function(val) {
    let index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

jQuery(function($) {
    $(document).off('click').on('click', 'a',function(e){
        let href = $(this).attr("href");
        if(!href || href === '#' || href.indexOf("javascript") !== -1  || href.indexOf("dtstep.com") !== -1){
            return;
        }
        let target = $(this).attr("target");
        if(target !== "_blank" && $(this).parents('#sub_nav').length > 0 && !href.startWith("/output/download.shtml")){
            COMMON.loadURL("#sub_nav",href);
            e.preventDefault();
        }else{
            let url = Encrypt.encryptUrl(href);
            $(this).attr("href",url);
        }
    });

    let lang = getLanguage();
    switchLang(lang);
});

function getLanguage(){
    let lang = COOKIE.getCookie("ldp_language");
    if(!lang){
        lang = navigator.language? navigator.language : navigator.userLanguage;
    }
    if(lang.toLocaleLowerCase() === 'us' || lang.toLocaleLowerCase() === 'en' || lang.toLocaleLowerCase() === 'en_us'){
        lang = 'en';
    }else{
        lang = 'zh';
    }
    return lang;
}
function setLanguage(lang){
    COOKIE.setCookie("ldp_language",lang,90);
    switchLang(lang);
}

jQuery.ajaxSetup({
    cache:false,
    complete: function(xhr,status) {
        var sessionStatus = xhr.getResponseHeader('sessionStatus');
        if(sessionStatus === 'timeout') {
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_login_1012'));
            MsgBox.Redirect("/login/index.shtml");
        }
    }
});










