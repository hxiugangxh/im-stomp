$(function () {
    // $("#menuModal").modal('show');
    var height = $(window).height();
    $('#content').css("height", height - $('#top').height() - $('#opt').height() - 90);
    $('#onLineContent').css("height", height - $('#top').height() - $('#opt').height() - 45);

    $('#loginBtn').click(function(){
        userLogin();
    });

    $('#faceBtn').qqFace({
        id: 'facebox',
        assign: 'msg',
        path: '/arclist/'	//表情存放的路径
    });

    $(window).resize(function() {
      var height = $(window).height();
      $('#content').css("height", height - $('#top').height() - $('#opt').height() - 90);
      $('#onLineContent').css("height", height - $('#top').height() - $('#opt').height() - 45);
    });
});

/**
 * 处理广播消息
 * @param data
 */
function broadcastInvake(data) {
    var mess = data.body;
    var nick = data.extend.nick;
    var uid = data.extend.uid;
    var time = data.extend.time;
    mess = replace_em(mess);
    var html = '<div class="title">'+nick+'&nbsp;('+uid+') &nbsp;'+time+'</div><div class="item">'+mess+'</div>';
    $("#content").append(html);
    $('#content').scrollTop($('#content')[0].scrollHeight);

}

//查看结果
function replace_em(str) {
    str = str.replace(/\</g, '&lt;');
    str = str.replace(/\>/g, '&gt;');
    str = str.replace(/\n/g, '<br/>');
    str = str.replace(/\[em_([0-9]*)\]/g, '<img src="arclist/$1.gif" border="0" />');
    return str;
}
