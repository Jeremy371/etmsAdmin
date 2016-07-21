define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'deviceNo', label: '단말기번호'},
                {name: 'phoneNo', label: '전화번호'},
                {
                    name: 'isSignature',
                    label: '서명여부',
                    formatter: 'select',
                    editoptions: {value: {true: '서명', false: '미서명'}}
                }
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/signature.json',
                    colModel: colModel,
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);
                        var url = 'image/signature/' + rowdata.deviceNo + '.jpg';

                        if(!rowdata.deviceNo){
                            BootstrapDialog.show({
                                title: '감독관 서명',
                                message: '단말기가 배정되지 않은 고사실입니다.',
                                closable: true,
                                buttons: [{
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });
                            return false;
                        }

                        var img = new Image();
                        img.src = url;
                        img.onerror = function () {
                            BootstrapDialog.show({
                                title: '감독관 서명',
                                message: '서명이 완료되지 않았습니다. 감독관에게 문의하세요.',
                                closable: true,
                                buttons: [{
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });
                        }
                        img.onload = function () {
                            BootstrapDialog.show({
                                title : rowdata.bldgNm + ' ' + rowdata.hallNm,
                                message: '<image src="' + url + '">',
                                size: 'size-wide',
                                closable: true,
                                buttons: [{
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });
                        }
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