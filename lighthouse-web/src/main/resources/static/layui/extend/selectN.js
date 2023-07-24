/*
* @version: 1.2
* @Author:  tomato
* @Date:    2018-4-24 22:56:00
* @Last Modified by:   tomato
* @Last Modified time: 2018-5-26 18:08:43
*/
layui.define(['jquery', 'form'], function(exports){
		var MOD_NAME = 'selectN';
		var $ = layui.jquery;
		var form = layui.form;
    var obj = function(config){
		this.selected =[];
		this.values = [];
		this.names = [];
		this.lastValue = '';
		this.lastName = '';
		this.isSelected = false;
		this.config = {
			elem: '',
			data: [],
			selected: [],
			tips: '请选择',
			search:false,
			width:null,
			last: false,
			verify: '',
			filter: '',
			name: '',
			delimiter: ',',
			field:{idName:'id',titleName:'name',statusName:'status',childName:'children'},
			formFilter: null,
			level: '',
		};
		
		//实例化配置
		this.config = $.extend(this.config,config);

		//“请选择”文字
		this.setTips = function(){
			var o = this,c = o.config;
			if(Object.prototype.toString.call(c.tips)!='[object Array]'){
				return c.tips;
			}
			else{
				var i=$(c.elem).find('select').length;
				return c.tips.hasOwnProperty(i) ? c.tips[i] : '请选择'; 
			}
		};

		//设置是否允许搜索
		this.setSearch = function(){
			var o = this,c = o.config;
			if(Object.prototype.toString.call(c.search)!='[object Array]'){
				return c.search===true ? 'lay-search ' : ' ';
			}
			else{
				var i=$(c.elem).find('select').length;
				if(c.search.hasOwnProperty(i)){
					return c.search[i]===true ? 'lay-search ' : ' ';
				}
			}
			return ' ';
		}

		//设置是否允许搜索
		this.setWidth = function(){
			var o = this,c = o.config;
			if(Object.prototype.toString.call(c.width)!='[object Array]'){
				return /^\d+$/.test(c.width) ? 'style="width:'+c.width+'px;" ' : ' ';
			}
			else{
				var i=$(c.elem).find('select').length;
				if(c.width.hasOwnProperty(i)){
					return /^\d+$/.test(c.width[i]) ? 'style="width:'+c.width[i]+'px;" ' : ' ';
				}
			}
		}

		this.setChangeWidth = function(isLast){
			if(!Validate.isNull(isLast) && isLast === true){
				var o = this,c = o.config;
				if(Object.prototype.toString.call(c.width)!='[object Array]'){
					return /^\d+$/.test(c.width) ? 'style="width:'+c.width+'px;margin-right:0px;" ' : ' ';
				}
				else{
					var i=$(c.elem).find('select').length;
					if(c.width.hasOwnProperty(i)){
						return /^\d+$/.test(c.width[i]) ? 'style="width:'+c.width[i]+'px;margin-right:0px;" ' : ' ';
					}
				}
			}else{
				var o = this,c = o.config;
				if(Object.prototype.toString.call(c.width)!='[object Array]'){
					return /^\d+$/.test(c.width) ? 'style="width:'+c.width+'px;" ' : ' ';
				}
				else{
					var i=$(c.elem).find('select').length;
					if(c.width.hasOwnProperty(i)){
						return /^\d+$/.test(c.width[i]) ? 'style="width:'+c.width[i]+'px;" ' : ' ';
					}
				}
			}
		}
			
		//创建一个Select
		this.createSelect = function(optionData){
			var o = this,c = o.config,f=c.field;
			var html = '';
			html+= '<div class="layui-input-inline" '+o.setWidth()+'>';
			html+= ' <select '+o.setSearch()+'lay-filter="'+c.filter+'">';
			html+= '  <option value="">'+o.setTips()+'</option>';
			for(var i=0;i<optionData.length;i++){
				var disabled = optionData[i][f.statusName]==0 ? 'disabled="" ' : '';
				html+= '  <option '+disabled+'value="'+optionData[i][f.idName]+'">'+optionData[i][f.titleName]+'</option>';
			}
			html+= ' </select>';
			html+= '</div>';
			return html;
		};

		this.createChangeSelect = function(optionData,isLast){
			var o = this,c = o.config,f=c.field;
			var html = '';
			html+= '<div class="layui-input-inline" '+o.setChangeWidth(isLast)+'>';
			html+= ' <select '+o.setSearch()+'lay-filter="'+c.filter+'">';
			html+= '  <option value="">'+o.setTips()+'</option>';
			for(var i=0;i<optionData.length;i++){
				var disabled = optionData[i][f.statusName]==0 ? 'disabled="" ' : '';
				html+= '  <option '+disabled+'value="'+optionData[i][f.idName]+'">'+optionData[i][f.titleName]+'</option>';
			}
			html+= ' </select>';
			html+= '</div>';
			return html;
		};

		//获取当前option的数据
		this.getOptionData=function(catData,optionIndex){
			var f = this.config.field;
			var item = catData;
			for(var i=0;i<optionIndex.length;i++){
				if('undefined' == typeof item[optionIndex[i]]){
					item = null;
					break;      
				}
				else if('undefined' == typeof item[optionIndex[i]][f.childName]){
					item = null;
					break;
				}
				else{
					item = item[optionIndex[i]][f.childName];
				}
			}
			return item;
		};

		//初始化
		this.set = function(selected){
			var o = this,c = o.config;
			$E = $(c.elem);
			//创建顶级select
			var verify = c.verify=='' ? '' : 'lay-verify="'+c.verify+'" ';
			var html = '<div style="height:0px;width:0px;overflow:hidden"><input '+verify+'name="'+c.name+'"></div>';
			var level = c.level;
			html += o.createSelect(c.data);
			for (let n=1;n<level;n++){
				html += o.createSelect([]);
			}
			$E.html(html);
			selected = typeof selected=='undefined' ? c.selected : selected;
			var index=[];
			for(var i=0;i<selected.length;i++){
				//设置最后一个select的选中值
				$E.find('select:last').val(selected[i]);
				//获取该选中值的索引
				var lastIndex = $E.find('select:last').get(0).selectedIndex-1; 
				index.push(lastIndex);
				//取出下级的选项值
				var childItem = o.getOptionData(c.data,index);
				//下级选项值存在则创建select
				if(childItem){
					var html = o.createSelect(childItem);
					$E.append(html);
				}
			}
			form.render('select',c.formFilter);
			o.getSelected();					
		};
		
		//下拉事件
		this.change = function(elem){
			var o = this,c = o.config;
			$E = $(c.elem);
			var verify = c.verify=='' ? '' : 'lay-verify="'+c.verify+'" ';
			var $thisItem = elem.parent();
			//移除后面的select
			$thisItem.nextAll('div.layui-input-inline').remove();
			var index=[];
			//获取所有select，取出选中项的值和索引
			$thisItem.parent().find('select').each(function(){
				index.push($(this).get(0).selectedIndex-1);
			});
			var childItem = o.getOptionData(c.data,index);
			if(childItem){
				let length = childItem.filter(x => x.hasOwnProperty("children")).length;
				var html = o.createChangeSelect(childItem,length === 0);
				html += '<div style="height:0px;width:0px;overflow:hidden"><input name=\"ss\"></div>';
				var level = c.level;
				for (let n=1;n<level - index.length;n++){
					html += o.createChangeSelect([],true);
				}
				$thisItem.after(html);
				form.render('select',c.formFilter);
			}else{
				var html = '<div style="height:0px;width:0px;overflow:hidden"><input name=\"ss\"></div>';
				var level = c.level;
				for (let n=0;n<level - index.length;n++){
					let ss = "";
					if(n === level - index.length - 1){
						ss = o.createChangeSelect([],true);
					}else{
						ss = o.createChangeSelect([],false);
					}
					html += ss;
				}
				$thisItem.after(html);
				form.render('select',c.formFilter);
			}
			o.getSelected();			
		};

		//获取所有值-数组 每次选择后执行
		this.getSelected=function(){
			var o = this,c = o.config;
			var values =[];
			var names =[];
			var selected =[];
			$E = $(c.elem);
			$E.find('select').each(function(){
				var item = {};
				var v = $(this).val()
				var n = $(this).find('option:selected').text();
				item.value = v;
				item.name = n;
				values.push(v);
				names.push(n);
				selected.push(item);
			});
			o.selected =selected;			
			o.values = values;
			o.names = names;
			o.lastValue = $E.find('select:last').val();
			o.lastName = $E.find('option:selected:last').text();
			
			o.isSelected = o.lastValue=='' ? false : true;
			var inputVal = c.last===true ? o.lastValue : o.values.join(c.delimiter);
			$E.find('input[name='+c.name+']').val(inputVal);
		};
		//ajax方式获取候选数据
		this.getData = function(url){
			var d;
			$.ajax({
				url:url,
				dataType:'json',
				async:false,
				success:function(json){
					d=json;
				},
				error: function(){
					console.error(MOD_NAME+' hint：候选数据ajax请求错误 ');
					d = false;
				}
			});
			return d;
		}		
		
		
	};

	//渲染一个实例
  obj.prototype.render = function(level){
		var o=this,c=o.config;
		$E = $(c.elem);
		if($E.length==0){
			console.error(MOD_NAME+' hint：找不到容器 '+c.elem);
			return false;
		}
		if(Object.prototype.toString.call(c.data)!='[object Array]'){
			var data = o.getData(c.data);
			if(data===false){
				console.error(MOD_NAME+' hint：缺少分类数据');
				return false;
			}
			o.config.data =  data;
		}
		o.config.level = level;
		c.filter = c.filter=='' ? c.elem.replace('#','').replace('.','') : c.filter;
		c.name = c.name=='' ? c.elem.replace('#','').replace('.','') : c.name;
		o.config = c;
		
		//初始化
		o.set();
		
		//监听下拉事件
		form.on('select('+c.filter+')',function(data){
			o.change($(data.elem));	
		});
		//验证失败样式
		$E.find('input[name='+c.name+']').focus(function(){
			var t = $(c.elem).offset().top;
			$('html,body').scrollTop(t-200);			
			$(c.elem).find('select:last').addClass('layui-form-danger');
			setTimeout(function(){
				$(c.elem).find('select:last').removeClass('layui-form-danger');
			},3000);
		});
	}
	
	//输出模块
	exports(MOD_NAME, function (config,level) {
		var _this = new obj(config);
		_this.render(level);
		return _this;
  });
});