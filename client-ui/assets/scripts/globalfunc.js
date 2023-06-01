function requestOption(method, body, userToken) {
    return method==='GET'?
    {
        method: method,
        mode: 'cors',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${userToken}`
        }
    }:
    {
        method: method,
        mode: 'cors',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${userToken}`
        },
        body: JSON.stringify(body)
    }
}

function loadingDialog() {
    let sectionDialog = document.querySelector("section[id='dialog']");
    let dialogTemplate = `<div id="loadingModal" class="modal" style="display:block;">
    <div class="modal-content">
        <div class="spinner-container">
            <div class="spinner">
            </div>
        </div>
    </div>
</div>`
    sectionDialog.innerHTML = dialogTemplate;

    const modal = sectionDialog.querySelector('#loadingModal');
    modal.style.top = "0";
}


function renderContainer(selector, items, renderCallback) {
    let container = document.querySelector(selector);
    container.innerHTML = renderCallback(items);
}

function dateFormat(strDate) {
    let date = new Date(strDate);
    let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    let month = date.getMonth() < 9 ? ('0' + (date.getMonth() + 1)) : (date.getMonth() + 1);

    let hour = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
    let minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
    let noon = hour < 12 ? 'AM' : 'PM'
    return `${day}/${month}/${date.getFullYear()} ${hour}:${minutes} ${noon}`;
}

function priceFormat(price) {
    return new Intl.NumberFormat('vn-VN').format(price);
}

function getMyCart() {
    const userToken = localStorage.getItem("userToken");
    if (userToken) {
        return fetch("http://localhost:5000/cart/my-cart", requestOption('GET', undefined, userToken))
            .then(res => {
                return res.json();
            })
            .then(res => {
                return res.cart;
            })
    } else {
        window.location.href = "./login.html";
    }
}

function dialog(mode, message) {
    let iconUrl;
    switch (mode) {
        case "success":
            iconUrl = "../img/modal/success.png"
            break;
        case "alert":
            iconUrl = "../img/modal/alert.png"
            break;
        case "failure":
            iconUrl = "../img/modal/failed.png"
            break;
        default:
            break;
    }
    let sectionDialog = document.querySelector("section[id='dialog']");
    let dialogTemplate = `<div id="myModal" class="modal" style="display:block;">
    <div class="modal-content">
        <img class="icon" src="${iconUrl}" alt="" srcset="">
        <p style="align-self: center;">${message}</p>
        <div class="button-group">
            <button id="button-yes" class="action-button">Xác nhận</button>
            ${mode === 'alert' ? `<button id="button-no" class="action-button">Hủy</button>` : ''}
        </div>
    </div>
</div>`
    sectionDialog.innerHTML = dialogTemplate;

    const pmDialog = new Promise((resolve, reject) => {
        let myModal = document.querySelector("#myModal")
        let bttYesOption = myModal.querySelector(".modal-content #button-yes");
        let bttNoOption = myModal.querySelector(".modal-content #button-no");

        bttNoOption ? (bttNoOption.onclick = (evt) => {
            sectionDialog.innerHTML = '';
            resolve(false);
        }) : 1;
        bttYesOption.onclick = (evt) => {
            sectionDialog.innerHTML = '';
            resolve(true);
        }
    })
    return pmDialog;
}

function notificationDialog(mode, message) {
    debugger
    let iconUrl;
    switch (mode) {
        case "success":
            iconUrl = "../img/modal/success.png"
            break;
        case "alert":
            iconUrl = "../img/modal/alert.png"
            break;
        case "failure":
            iconUrl = "../img/modal/failed.png"
            break;
        default:
            break;
    }
    let sectionDialog = document.querySelector("section[id='dialog']");
    let dialogTemplate = `<div id="notificationModal" class="modal" style="display:block;">
    <div class="modal-content">
      <img class="icon" src="${iconUrl}" alt="" srcset="">
      <p style="align-self: center; margin: unset;">${message}</p>
    </div>
  </div>`
    sectionDialog.innerHTML = dialogTemplate;

    const modal = sectionDialog.querySelector('#notificationModal');
    modal.style.top = "0";

    setTimeout(function () {
        modal.style.top = "-1000px";
    }, 1500)
}

function turnOnToolTip() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    })
}