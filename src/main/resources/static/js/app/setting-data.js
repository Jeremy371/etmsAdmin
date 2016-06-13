define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    //var DlgDownload = require('../dist/dlg-download.js');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            //this.dlgDownload = new DlgDownload();
        }, render: function () {
            this.$el.html(Template);
            this.initForm('#frmUploadHall');
            this.initForm('#frmUploadExaminee');
        }, initForm: function (id) {
            this.$(id).ajaxForm({
                beforeSubmit : function(arr){
                    for(var i in arr){
                        if(arr[i].name =='file' && arr[i].value == ''){
                            return false;
                        }
                    }
                },
                success : function(data){
                    console.log(data);
                    console.log('데이터 전송 완료!');
                }
            });
        }
    });
});