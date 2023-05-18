const productRouteURL = 'http://localhost:8080/post'

function createNewPostAPI(newPost) {
    const userToken = localStorage.getItem('userToken')
    if (userToken) {
        fetch(productRouteURL + `/create`, requestOption('POST', newPost, userToken))
            .then(res => {
                return res.json();
            })
            .then(res => {
                if (res.success) {
                    notificationDialog("success", "Đã đăng bài");
                    // window.location.href = `./product-detail.html?id=${res.product_id}`;
                    return res;
                } else {
                    console.log(res);
                    return false
                }
            })
            .catch(err => {
                alert('Thất bại');
                return false;
            });
    } else {
        alert('Không tìm thấy token')
        // window.location.href === 'http://127.0.0.1:5500/Frontend/assets/admin/views/login.html'
        //     ? 1 : window.location.href = './login.html';
        // return false
    }
}