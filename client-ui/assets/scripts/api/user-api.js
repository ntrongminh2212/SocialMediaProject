const productRouteURL = 'http://localhost:8080/user'

async function login(account) {
    return fetch(productRouteURL + `/login`, requestOption('POST', account))
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