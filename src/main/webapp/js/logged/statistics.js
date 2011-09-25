jQuery(function($) {
	var chart;
	var pointInterval = 24 * 3600 * 1000;
	var maxZoom = 14 * 24 * 3600000; // fourteen days.

	$(document).ready(function() {
	    chart = new Highcharts.Chart({
			chart: {
			    renderTo: 'chart-div',
			    zoomType: 'x',
			},
			title: {
			    text: i18n['chart.title']
			},
			subtitle: {
			    text: document.ontouchstart === undefined ? i18n['chart.sub1'] : i18n['chart.sub2']
			},
			xAxis: {
			    type: 'datetime',
				maxZoom: maxZoom,
			    title: {
					text: null
			    }
			},
			yAxis: {
				allowDecimals: false,
				title: {
					text: null
			    },
			    min: 0,
			},
			tooltip: {
			    crosshairs: true,
			    shared: true
			},
			credits: {
			    enabled: true // great product.
			},
			series: [{
			    name: i18n['chart.creat'],
			    pointInterval: pointInterval,
			    pointStart: pointStart,
			    data: created
			}, {
			    name: i18n['chart.done'],
			    pointInterval: pointInterval,
			    pointStart: pointStart,
			    data: done
			}]
	    });
	});

	/* Theme */
	Highcharts.theme = {
		colors: ["#7798BF", "#DF5353"]
	};
	Highcharts.setOptions(Highcharts.theme);
});
