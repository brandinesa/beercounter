google.charts.load("current", {packages:["corechart"]});

function init() {
    loadData();
}

function loadData() {
    setTimeout(function() {
        $.ajax({
            url: "/api/asdf"
        }).done(function(result) {
            var total = 0;
            var dataArray = [["Name", "Menge"]];
            result.forEach(function(r) {
                if (r.name !== "James") {
                    total += r.amount;
                }
                dataArray.push([r.name, r.amount]);
            });

            var data = google.visualization.arrayToDataTable(dataArray);

            var view = new google.visualization.DataView(data);

            var options = {
                title: "Bier-Ranking (Gesamt ohne James: " + (Math.round(total * 100) / 100) + " l)",
                bar: {groupWidth: "95%"},
                'width':1300,
                'height':700,
                'chartArea': {'width': '80%', 'height': '80%'},
                legend: { position: "none" },
            };

            var chart = new google.visualization.BarChart(document.getElementById("barchart_values"));
            chart.draw(view, options);
        });

        loadData();
    }, 5000);
}

function auswertung() {
    $.ajax({url: "/api/asdf/all"}).done(function(result) {
        var persons = [];
        result.forEach(function(r) {
            if (persons.indexOf(r.name) < 0) {
                persons.push(r.name);
                $("#personSelect").append("<option>" + r.name + "</option>");
            }
        });

        $("#personSelect").on("change", function() { refresh(result); });
        $("#daySelect").on("change", function() { refresh(result); });

        refresh(result);
    });
}

function refresh(result) {
    var day = $("#daySelect").val();
    var person = $("#personSelect").val();
    $("#barchart_values").html("");

    var seidl = 0;
    var vier = 0;
    var kruegl = 0;
    var total = 0;
    var perHour = {};

    for (var i = 0; i < result.length; i++) {
        var r = result[i];
        if ((person === "Alle" || r.name === person) && (day === "Alle" || day === r.timestamp.dayOfWeek)) {
            total += r.amount;
            if (r.amount === 0.3) {
                seidl++;
            } else if (r.amount === 0.4) {
                vier++;
            } else if (r.amount === 0.5) {
                kruegl++;
            }
            if (!(r.timestamp.hour in perHour)) {
                perHour[r.timestamp.hour] = 0;
            }

            var valPerHour = perHour[r.timestamp.hour] + r.amount;
            perHour[r.timestamp.hour] = Math.round(valPerHour * 100) / 100;
        }
    }

    var googleArray = [["Stunde", "Menge"]];

    for (var key in perHour) {
        googleArray.push([key, perHour[key]]);
    }

    var data = google.visualization.arrayToDataTable(googleArray);
    var options = {
        title: 'Getrunkene Bier pro Stunde',
        legend: { position: 'bottom' }
    };

    total = Math.round(total * 100) / 100;

    $("#barchart_values").append("<b>Getrunkene Menge:</b> " + total + "l<br />");
    $("#barchart_values").append("<b>" + (seidl + vier + kruegl) + "</b> gezapfte Biere<br />");
    $("#barchart_values").append("<b>davon Seidl:</b> " + seidl + "<br />");
    $("#barchart_values").append("<b>davon 0,4:</b> " + vier + "<br />");
    $("#barchart_values").append("<b>davon Kriagl:</b> " + kruegl + "<br />");
    $("#barchart_values").append("<b>davon Maß:</b> 0 :(<br />");
    $("#barchart_values").append("<b>davon Stiefel:</b> 0 :(<br /><br />");
    $("#barchart_values").append("<div id='hourChart'></div>");


    var chart = new google.visualization.LineChart(document.getElementById('hourChart'));

    chart.draw(data, options);
}