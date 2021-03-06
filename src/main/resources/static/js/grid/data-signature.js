define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'attendCd', hidden: true},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'hallCd', hidden: true},
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'attendCnt', label: '배정인원'},
                {
                    name: 'isSignature',
                    label: '서명여부',
                    formatter: 'select',
                    editoptions: {value: {true: '서명', false: '미서명'}}
                },
                {name: 'signDttm', label: '서명시간'},
                {name: 'isEtc', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/signature.json',
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.isEtc == 'true') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        }
                    },
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);
                        //var url1 = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_1_all_sign.jpg';
                        //var url2 = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_2_all_sign.jpg';
                        var url = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_all_sign.jpg';

                        if (rowdata.isSignature && rowdata.isSignature == 'false') return false;

                        var dialog = new BootstrapDialog({
                            title: '<h3>' + rowdata.bldgNm + ' ' + rowdata.hallNm + '</h3>',
                            //message: '<image src="' + url1 + '"><image src="' + url2 + '">',
                            message: '<div style="text-align:center"><image src="' + url + '" style="width:60%"></div>',
                            size: 'size-wide',
                            closable: true,
                            buttons: [{
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }]
                        });

                        dialog.realize();
                        dialog.open();
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/signature.xlsx');
            return this;
        }
    });
});