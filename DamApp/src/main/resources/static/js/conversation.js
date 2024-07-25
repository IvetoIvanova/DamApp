document.addEventListener('DOMContentLoaded', function () {
    let conversationId = document.getElementById('conversationId').value;
    console.log("Conversation ID:", conversationId);

    if (conversationId) {
        markMessagesAsRead(conversationId);
    }
});

function markMessagesAsRead(conversationId) {
    console.log("Sending request to mark messages as read for conversation ID: " + conversationId);
    fetch(`/messages/conversation/mark-as-read?conversationId=${encodeURIComponent(conversationId)}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log("Messages marked as read successfully");
        })
        .catch(error => console.error("Error marking messages as read:", error));
}