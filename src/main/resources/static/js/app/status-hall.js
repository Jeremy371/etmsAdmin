define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-hall.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/status-hall.js');
    var Template = require('text!/tpl/status-hall.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/hallStat'});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();
        }, search: function (o) {
            this.list.search(o);
            this.summary.render(o);
        }
    });
});