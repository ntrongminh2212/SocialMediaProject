let lstImages =[];
let mainImg = document.querySelector('#main-img');
let inputStatusContent = document.querySelector('input#status-content');

function previewFile(inputSelectImg) {
    let lstSelectImages = inputSelectImg.files;
    if (lstSelectImages.length > 0) {
        if (lstImages.length + lstSelectImages.length < 6) {
            Array.from(lstSelectImages).forEach(image => {
                var fileReader = new FileReader();

                fileReader.addEventListener('load', () => {
                    if (!lstImages.includes(fileReader.result)) {
                        // renderImageGroup(fileReader.result);
                        lstImages.push(fileReader.result);
                        mainImg.src = fileReader.result;
                    } else {
                        alert('Ảnh trùng lặp')
                    }
                })
                fileReader.readAsDataURL(image);
            })
        } else {
            alert('Chỉ được nhập nhiều nhất 5 ảnh cho một sản phẩm');
        }
    }
}

function createPost() {
    let newPost = {
        statusContent: inputStatusContent.value,
        attachmentUrl: lstImages[0]
    }

    console.log(newPost);
    // createNewPostAPI(newPost);
}