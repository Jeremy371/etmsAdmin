define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    var BootstrapDialog = require('bootstrap-dialog');
    var DuplicatePaper = require('../grid/check-paper-duplicate.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'paperCd', label: '답안지번호'},
                {name: 'duplicateCnt', label: '중복횟수'},
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'check/paper.json',
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {

                        var rowData = $(this).jqGrid('getRowData', rowid);

                        var param = {
                            attendCd: rowData.attendCd,
                            paperCd: rowData.paperCd
                        };

                        var dialog = new BootstrapDialog({
                            title: '<h4>중복된 답안지를 확인하세요. 푸른 영역이 마지막으로 처리된 상태입니다</h4>',
                            onshown: function(dialog){
                                dialog.list = new DuplicatePaper({el: dialog.$modalBody, param: param}).render();
                            },
                            buttons: [
                                {
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }
                            ]
                        });

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalDialog().css('width', '60%');
                        dialog.open();
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        }
    });
});