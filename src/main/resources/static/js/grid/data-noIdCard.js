define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'birth', label: '생년월일'},
                {name: 'attendHeadNm', label: '응시고사본부'},
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {name: 'isOtherHall', label: '타고사실여부', formatter: 'select', editoptions: {value: {true: 'Y', false: 'N'}}},
                {name: 'attendDttm', label: '등록시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/noIdCard.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/noIdCard.xlsx');
            return this;
        }
    });
});