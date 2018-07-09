$(function () {
    // $("#menuModal").modal('show');
    var height = $(window).height();
    $('#content').css("height", height - $('#top').height() - $('#opt').height() - 90);
    $('#onLineContent').css("height", height - $('#top').height() - $('#opt').height() - 45);

    $('#iframe').css("min-height", height - $('#top').height() - $('#opt').height() - 90);

    $('#faceBtn').qqFace({
        id: 'facebox',
        assign: 'msg',
        path: '/arclist/'	//表情存放的路径
    });

    $(window).resize(function() {
      var height = $(window).height();
      $('#content').css("height", height - $('#top').height() - $('#opt').height() - 90);
      $('#iframe').css("min-height", height - $('#top').height() - $('#opt').height() - 90);
      $('#onLineContent').css("height", height - $('#top').height() - $('#opt').height() - 45);
    });
});