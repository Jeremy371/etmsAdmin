define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/data-paper.js');
    var Toolbar = require('../toolbar/data-paper.js');
    var Template = require('text!/tpl/data-paper.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid'}).render();

        }, search: function (o) {
            this.list.search(o);
            this.list.$grid.jqGrid('setGridParam', {url: 'data/paper.json', datatype: 'json'}).trigger('reloadGrid');

            $('.hm-ui-grid').fadeIn(50);
            $('.notice').hide();
        }
    });
});