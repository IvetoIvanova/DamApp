$(document).ready(function () {
    $(".conversation-link").click(function () {
        const conversationId = $(this).data("conversation-id");
        $.get("/messages/conversation/" + conversationId, function (data) {
            $("#conversation").html(data);
        });
    });

    function loadUnreadCount() {
        $.get("/messages/unread-count", function (data) {
            if (data > 0) {
                $("#unreadCount").text(data);
            } else {
                $("#unreadCount").text('');
            }
        });
    }

    loadUnreadCount();
    setInterval(loadUnreadCount, 30000);
});