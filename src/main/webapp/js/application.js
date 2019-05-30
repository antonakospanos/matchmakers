$(document).ready(function () {
    data = {};
  
    $('#BatchJobsModal').on('hidden.bs.modal', function (e) {
        $("#BatchJobsModal .modal-body").empty();
    })

    $.ajax({
        type: 'GET',
        dataType: 'json', 
        url: ApiAddresses.BatchJob,
        async: false,
        data: data,
        beforeSend: function () {
            waitingDialog.show();
        },
        success: function (data) {
            drawTable(data);
        },
        failure: function (response) {
            waitingDialog.hide()
        },
        complete: function () {
            waitingDialog.hide()
        }
    });

    function drawTable(data) {
        for (var i = 0; i < data.length; i++) {
            drawRow(data[i]);
        }
    }

    function drawRow(rowData) {
        var row = $("<tr />")
        $("#BatchJobs").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it 
        row.append($("<td>" + rowData.startDate[3] + ":" + (rowData.startDate[4] < 10 ? '0' : '') + rowData.startDate[4] + "</td>"));
        row.append($("<td>" + rowData.endDate[3] + ":" + (rowData.endDate[4] < 10 ? '0' : '') + rowData.endDate[4] + "</td>"));
        row.append($("<td>" + rowData.name + "</td>"));
        row.append($("<td>" + +"</td>"));
        row.append($("<td>" + MapReceivedData(rowData.type) + "</td>"));
        row.append($("<td>" + rowData.state + "</td>"));
        var Actions = $("<td class='clickable'></td>");
        var DetailsButton=$("<button type='button' class='btn btn-default btn-xs'><span class='glyphicon glyphicon-search'></span></button>");
        var StatisticsButton=$("<button type='button' class='btn btn-default btn-xs'><span class='glyphicon glyphicon glyphicon-signal'></span></button>");
        DetailsButton.click(function () {
            $.ajax({
                type: 'GET',
                dataType: 'json',
                async: false,
                url: ApiAddresses.Process + '/' + rowData.id,
                data: data,
                success: function (data) {
                    fillProcessDetails(data)
                }
            });

        });
        StatisticsButton.click(function () {
            fillStatistics();
        });
        DetailsButton.appendTo(Actions);
        StatisticsButton.appendTo(Actions);
        row.append(Actions);
        
    }


    function FillStepsMasterDetails(container, data) {
        var table = $("<table/>").addClass('table table-hover table-striped ');

        var row = $("<tr/>");
        row.append($("<td/>").text("Name").addClass("font-bold"));
        row.append($("<td/>").text(data.name));
        table.append(row);
        var row = $("<tr/>");
        row.append($("<td/>").text("Start Time").addClass("font-bold"));
        row.append($("<td/>").text(data.startDate[3] + ":" + (data.startDate[4] < 10 ? '0' : '') + data.startDate[4]));
        table.append(row);
        var row = $("<tr/>");
        row.append($("<td/>").text("End Time").addClass("font-bold"));
        row.append($("<td/>").text(data.endDate[3] + ":" + (data.endDate[4] < 10 ? '0' : '') + data.endDate[4]));
        table.append(row);
        var row = $("<tr/>");
        row.append($("<td/>").text("Execution End Time").addClass("font-bold"));
        row.append($("<td/>").text(data.executionsEndTime));
        table.append(row);
        var row = $("<tr/>");
        row.append($("<td/>").text("Repetition Scheme").addClass("font-bold"));
        row.append($("<td/>").text(data.repetitionScheme));
        table.append(row);
        var row = $("<tr/>");
        row.append($("<td/>").text("Process Type").addClass("font-bold"));
        row.append($("<td/>").text(ApiReceivedTypeToTableTypeMapper[data.processType]));
        table.append(row);
        return container.append(table);
    }


    function fillStepsSection(container, data) {
        var content = $("<div/>").addClass('table-responsive')

        $.each(data.steps, function (rowIndex, r) {
            var table = $("<table/>").addClass('table table-hover table-striped ');
            $.each(r, function (colIndex, c) {
                var row = $("<tr/>");
                row.append($("<td/>").text(MapReceivedData(colIndex)).addClass("font-bold"));
                if(colIndex!=='selectionFilterStr'){
                    row.append($("<td/>").text(c));
                }else{
                    var lines = c.substring(2).split('$$');
                    var output = '<ul> ';
                    $.each(lines, function (key, line) {
                        output += '<li>' + line.replace("=", " : "); + '</li>';
                    });
                    output+=' </ul>';
                    row.append($("<td/>").html(output));
                }
                table.append(row);
            });
            var stepNum=rowIndex+1;
            content.append($("<div/>").append($("<div/>").text("Step "+stepNum).addClass('panel-heading font-bold')).append($("<div/>").append(table).addClass('panel-body')).addClass('panel panel-default'))
        });

        return container.append(content);
    }

    function fillProcessDetails(data) {
        $('#BatchJobsModal .modal-body').append('<div class="panel panel-default"><div class="panel-body placeholder"></div></div>');
        FillStepsMasterDetails($(".placeholder"), data)
        var content = fillStepsSection($(".placeholder"), data);
        $('#BatchJobsModal').modal('show');
    }
    
    function MapReceivedData(data){
        return ApiReceivedTypeToTableTypeMapper[data] != undefined ? ApiReceivedTypeToTableTypeMapper[data] : data;
    }
    
    
    
    
    function fillStatistics(data){
          $('#BatchJobsModal .modal-body').append('<div class="panel panel-default"> \
            <div class="panel-body"> \
                <div class="table-responsive">   \
                    <table id="BatchJobsStatics" class="table table-hover table-striped"> \
                        <thead> \
                            <tr> \
                                <th>Started at</th> \
                                <th>Current Status</th> \
                                <th>Pending</th> \
                                <th>Processing</th> \
                                <th>Sent</th> \
                                <th>Billed</th> \
                                <th>Unbilled</th> \
								<th>Failed</th> \
								<th>Deferred</th> \
								<th>Expired</th> \
								<th>Delivered</th> \
                            </tr> \
                        </thead> \
                        <tbody> \
                            <tr> \
                                <td>10/03/14 14:00</td> \
                                <td>Completed</td> \
                                <td>0</td> \
                                <td>0</td> \
                                <td>0</td> \
                                <td>0</td> \
                                <td>0</td> \
								<td>0</td> \
								<td>0</td> \
								<td>0</td> \
								<td>0</td> \
                            </tr> \
                        </tbody> \
                    </table> \
                </div> \
            </div> \
        </div> \ ');
        $('#BatchJobsModal').modal('show');
    }

    $('#BatchJobs').DataTable({ 
            "aoColumnDefs": [
          { 'bSortable': false, 'aTargets': [ 6 ] }
       ]
   });
});

