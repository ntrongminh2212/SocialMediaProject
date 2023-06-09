const userRouteURL = 'http://localhost:8080/user'

async function login(account) {
    return fetch(userRouteURL + `/login`, requestOption('POST', account))
        .then(res => {
            if (res.status === 200) {
                return res.json();
            }
        })
        .then(res => {
            console.log(res);
                // notificationDialog("success", "Đăng nhập thành công");
                // window.location.href = `./product-detail.html?id=${res.product_id}`;
                return res;
            })
        .catch(err => {
            return false;
        });
}
//http://localhost:8080/user/info


async function getPersonalInfoAPI() {
    const userToken = sessionStorage.getItem('userToken')
    if (userToken) {
        return fetch(
            userRouteURL+"/info",
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