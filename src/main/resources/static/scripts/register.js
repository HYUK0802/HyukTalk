const registerForm = document.getElementById('register-form')
registerForm.onsubmit = e => {
    e.preventDefault();
    if(registerForm['email'].value == ''){
              alert('이메일을 입력해주세요');
              registerForm['email'].focus();
    };
    if(registerForm['pwd'].value == ''){
                  alert('비밀번호를 입력해주세요');
                  registerForm['pwd'].focus();
    };
     if(registerForm['pwdCheck'].value == ''){
                      alert('비밀번호를 입력해주세요');
                      registerForm['pwdCheck'].focus();
        };
    if(registerForm['name'].value == ''){
        alert('닉네임을 입력해주세요');
        registerForm['name'].focus();
    };
    if(registerForm['phone'].value == ''){
                  alert('전화번호을 입력해주세요');
                  registerForm['phone'].focus();
    };
    if(registerForm['birth'].value == ''){
                  alert('생년월일을 입력해주세요');
                  registerForm['birth'].focus();
    };
    if(registerForm['pwd'].value !== registerForm['pwdCheck'].value){
        alert('비밀번호가 일치하지 않습니다.')
        registerForm['pwdCheck'].focus();
    }
    const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('email', registerForm['email'].value);
            formData.append('pwd', registerForm['pwd'].value);
            formData.append('name', registerForm['name'].value);
            formData.append('phone', registerForm['phone'].value);
            formData.append('birthStr', registerForm['birth'].value);

            xhr.open('POST', './register');
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject.result) {
                            case `failure_email_duplicate`:
                                alert('입력하신 이메일은 이미 사용 중입니다.')
                                break;

                            case `failure_nickname_duplicate`:
                                alert('닉네임 중복입니다. 다른 닉네임 사용하세요');
                                registerForm['nickname'].focus();
                                registerForm['nickname'].select();
                                break;

                            case 'failure_contact_duplicate':
                                alert('전화번호 중복입니다. 다른 전화번호 사용하세요');
                                registerForm['phone'].focus();
                                registerForm['phone'].select();
                                break;
                            case 'success':
                               alert('회원가입 성공!');
                               location.href = "/login"; // 성공 시 로그인 페이지로 이동
                               break;

                            case 'failure':
                            default:
                                alert('회원가입 실패 다시 시도해주세요')
                                  }
                                              } else {
                                                  alert('네트워크 오류');
                                              }
                                          }
                                      };

                                      xhr.send(formData); // 서버로 폼 데이터 전송
                                  };

