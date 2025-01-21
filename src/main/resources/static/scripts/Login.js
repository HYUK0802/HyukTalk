const loginForm = document.getElementById('login-form');
const id = document.getElementById('userid')

loginForm.onsubmit = e => {
    e.preventDefault()
    if (loginForm['email'].value === '') {
        loginForm['email'].focus();
        alert('아이디를 입력해주세요')
    }
    if (loginForm['pwd'].value === '') {
        loginForm['pwd'].focus();
        alert('비밀번호를 입력해주세요')
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', loginForm['email'].value);
    formData.append('pwd', loginForm['pwd'].value);
    xhr.open('POST','/login' );
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            return;
        }
            if (xhr.status >= 200 && xhr.status < 300 ) {
            const responseObject = JSON.parse(xhr.responseText);
            switch (responseObject.result){
                case 'failure':
                    alert('아이디, 비밀번호가 올바르지 않아요')
                case 'success':
                    location.href ="/friends"
                    break;
                default:
                    alert('서버오류입니다 재시도 해주세요')
            }
            } else {
                alert('네트워크 오류입니다. 새로고침 후 다시 시도해주세요')
            }
    };
    xhr.send(formData);
}
