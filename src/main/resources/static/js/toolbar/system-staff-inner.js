/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$('#staffAdm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#staffDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#staffTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            this.$('#staffBldg').html(this.getOptions(ToolbarModel.getBldgNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #staffAdm': 'admissionNmChanged',
            'change #staffDate': 'attendDateChanged',
            'change #staffTime': 'attendTimeChanged'
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#staffDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#staffTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#staffBldg').html(this.getOptions(ToolbarModel.getBldgNm(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#staffAdm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#staffTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#staffBldg').html(this.getOptions(ToolbarModel.getBldgNm(param)));
        },
        attendTimeChanged: function (e) {
            var param = {
                admissionNm: this.$('#staffAdm').val(),
                attendDate: this.$('#staffDate').val(),
                attendTime: e.currentTarget.value
            };
            this.$('#staffBldg').html(this.getOptions(ToolbarModel.getBldgNm(param)));
        }
    });
});