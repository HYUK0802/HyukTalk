// 모달 열기
function openFriendModal() {
    console.log("모달 열기");
    const modal = document.getElementById("friendModal");
    if (modal) {
        console.log("모달 요소 찾음");
        modal.classList.remove("hidden");
        modal.style.display = "block";  // 모달을 보이게 하는 코드 추가
        console.log("hidden 클래스 제거됨");
    } else {
        console.error("모달 요소를 찾을 수 없습니다.");
    }
}


function closeFriendModal() {
    console.log("모달 닫기");  // 함수 실행 확인을 위한 로그
    const modal = document.getElementById("friendModal");
    modal.classList.add("hidden");
    modal.style.display = "none";  // 모달을 숨기기 위한 코드
    document.getElementById("addFriendForm").reset(); // 폼 초기화
    document.getElementById("friendModalMessage").textContent = ""; // 메시지 초기화
}


// 친구 추가 폼 처리
document.getElementById("addFriendForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const friendEmail = document.getElementById("friendEmail").value;
    try {
        const response = await fetch("/friends/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ friendEmail }),
        });

        const messageElement = document.getElementById("friendModalMessage");
        if (response.ok) {
            messageElement.textContent = "친구 추가 성공!";
            messageElement.style.color = "green";
        } else {
            const errorMessage = await response.text();
            messageElement.textContent = "추가 실패: " + errorMessage;
            messageElement.style.color = "red";
        }
    } catch (error) {
        console.error("Error:", error);
        document.getElementById("friendModalMessage").textContent = "오류 발생!";
    }
});


       function startChat(element) {
       const parent = element.closest('.user-component');
       const userEmail = encodeURIComponent(parent.querySelector('input[name="userEmail"]').value);

       let friendEmail;
       // friendEmail과 friendBirthEmail 중 존재하는 값을 가져옴
       const friendEmailField = parent.querySelector('input[name="friendEmail"]');
       const friendBirthEmailField = parent.querySelector('input[name="friendBirthEmail"]');

       if (friendEmailField) {
           friendEmail = encodeURIComponent(friendEmailField.value); // 일반 친구
       } else if (friendBirthEmailField) {
           friendEmail = encodeURIComponent(friendBirthEmailField.value); // 생일 친구
       }

       console.log("User Email:", userEmail);
       console.log("Friend Email:", friendEmail);

       const confirmStart = confirm("채팅 하시겠습니까?");
       if (confirmStart) {
           // 서버로 리다이렉트
           window.location.href = "/chat/start?userEmail=" + userEmail + "&friendEmail=" + friendEmail;
       }
   }