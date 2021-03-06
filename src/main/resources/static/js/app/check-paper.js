define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/check-paper.js');
    var Toolbar = require('../toolbar/check-paper.js');
    var Template = require('text!/tpl/check-paper.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid'}).render();

        }, search: function (o) {
            this.list.search(o);
            this.list.$grid.jqGrid('setGridParam', {url: 'check/detect1', datatype: 'json'}).trigger('reloadGrid');

            $('.notice').hide();
            $('.hm-ui-grid').fadeIn(500);
        }
    });
});