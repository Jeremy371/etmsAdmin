define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Donut({
                element: this.el.id,
                data: [{label: '응시율', value: 0.00}, {label: '결시율', value: 100.00}],
                formatter: function (y, data) {
                    return y + '%'
                }
            });
            this.search();
            this.resize();
            return this;
        }, resize: function () {
            var _this = this;
            $(window).bind('resizeEnd.Morris' + this.cid, function () {
                _this.chart.redraw();
            });
        }, close: function () {
            $(window).unbind('resizeEnd.Morris' + this.cid);
        }, search: function (o) {
            var _this = this;
            $.ajax({
                url: 'status/all',
                data: o
            }).done(function (response) {
                _this.chart.setData([
                    {label: '응시율', color: '#0B62A4', value: response[0].attendPer},
                    {label: '결시율', color: '#7A92A3', value: response[0].absentPer}
                ]);
            });
        }
    });
});