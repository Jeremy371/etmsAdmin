/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.data-answerSheet.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'click #admissionNm': 'admissionNmChanged',
            'click #deptNm': 'deptNmChanged',
            'click #majorNm': 'majorNmChanged',
            'click #headNm': 'headNmChanged',
            'click #bldgNm': 'bldgNmChanged',
            'click #hallNm': 'hallNmChanged',
            'click #attendDate': 'attendDateChanged',
            'click #attendTime': 'attendTimeChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.list) {
                this.list.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    deptNm : _this.$('#deptNm').val(),
                    majorNm: _this.$('#majorNm').val(),
                    headNm : _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val(),
                    lastSheet: _this.$('#lastSheet').val(),
                    isChange : _this.$('#isChange').val(),
                    examineeCd : _this.$('#examineeCd').val(),
                    examineeNm : _this.$('#examineeNm').val()
                });
            }
        },
        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            }
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        deptNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: e.currentTarget.value
            }
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        majorNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: e.currentTarget.value
            }
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        headNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                headNm: e.currentTarget.value
            }
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        bldgNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: e.currentTarget.value
            }
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        hallNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: e.currentTarget.value
            }
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        attendDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: e.currentTarget.value
            }
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }
    });
});