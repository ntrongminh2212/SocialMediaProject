const messageRouteURL = 'http://localhost:8080/message'

function getConversationMsgsAPI(conversationId) {
    const userToken = sessionStorage.getItem('userToken')
    if (userToken) {
        return fetch(
            messageRouteURL + `?conversation_id=${conversationId}`,
            requestOption('GET', "", userToken)
        )
            .then(res => {
                if (res.status == 200) {
                    return res.json();
                } else {
                    console.log(res);
                    return false
                }
            })
            .then(res => {
                console.log(res);
                return res;
            })
            .catch(err => {
                console.log(err);
                return false;
            });
    } else {
        alert('Không tìm thấy token')
        // window.location.href === 'http://127.0.0.1:5500/Frontend/assets/admin/views/login.html'
        //     ? 1 : window.location.href = './login.html';
        // return false
    }
}