/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var TableOptions = {
        getTableOption: function (data) {
            let seriesArray = [];
            let dataMap = data.dataMap;
            let dimensMapperInfo = data.dimensMapperInfo;
            let batchList = data.batchList;
            let dimensArray = [];
            let xAxisFormat = data.xAxisFormat;
            var html = "";
            html += "<div class=\"box-body table-responsive no-padding\">\n";
            html += "   <table class=\"table display table-hover\">\n";
            html += "       <tr>\n";
            html += "        <th>Dimens</th>\n";
            for (let i = 1, len = batchList.length + 1; i < len; i++) {
                html += "        <th>" + new Date(Number(batchList[i - 1])).format(xAxisFormat) + "</th>\n";
            }
            html += "       </tr>\n";
            for (var key in dataMap) {
                html += "<tr>\n";
                let valueList = dataMap[key];
                let mapperKey = key;
                if(dimensMapperInfo != null && !Validate.isNull(dimensMapperInfo[key])){
                    mapperKey = dimensMapperInfo[key];
                }
                html += "        <td>" + mapperKey + "</td>\n";
                for (var i = 1, len = valueList.length + 1; i < len; i++) {
                    html += "        <td>" + valueList[i - 1] + "</td>\n";
                }
                html += "</tr>\n";
            }
            html += "  </table>";
            html += "</div>";
            return html;
        },
        renderTableByBatchOption: function (data) {
            var seriesArray = [];
            var batchTime = data.batchTime;
            var xAxisFormat = data.xAxisFormat;
            var valueList = data.valueList;
            var html = "";
            html += "<div class=\"box-body table-responsive no-padding\" style='overflow-y:visible;height:320px;'>\n";
            html += "   <table class=\"table table-hover\">\n";
            html += "       <tr>\n";
            html += "        <th>NO</th>\n";
            html += "        <th>Dimens</th>\n";
            html += "        <th>" + new Date(Number(batchTime)).format(xAxisFormat) + "</th>\n";
            html += "       </tr>\n";
            for (var i = 0, len = valueList.length; i < len; i++) {
                var obj = valueList[i];
                html += "<tr>\n";
                html += "        <td>" + (i + 1) + "</td>\n";
                html += "        <td>" + obj.dimens + "</td>\n";
                html += "        <td>" + obj.value + "</td>\n";
                html += "</tr>\n";
            }
            html += "  </table>";
            html += "</div>";
            return html;
        }
    };