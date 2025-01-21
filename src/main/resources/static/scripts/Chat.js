document.getElementById("chat-form").addEventListener("submit", function (e) {
    e.preventDefault(); // 기본 폼 제출 동작 방지

    // 데이터 확인
    const chatRoomId = document.getElementById("chat-form").dataset.chatRoomId;
    const content = document.getElementById("message-content").value.trim();

    console.log("Chat Room ID:", chatRoomId);
    console.log("Content:", content);

    if (!chatRoomId) {
        alert("채팅방 ID가 설정되지 않았습니다.");
        return;
    }

    if (!content) {
        alert("메시지를 입력하세요.");
        return;
    }

    // AJAX 요청
    fetch(`/chat/${chatRoomId}/send`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({ content })
    })
        .then(response => {
            if (response.ok) {
                console.log("Message sent successfully");
                window.location.href = `/chat/${chatRoomId}`;
            } else {
                return response.text().then(errorMessage => {
                    console.error("Error response:", errorMessage);
                    throw new Error(errorMessage);
                });
            }
        })
        .catch(err => {
            console.error("Error:", err.message);
            alert("메시지 전송 중 오류가 발생했습니다.");
        });
});
const chatContainer = document.getElementById("chat-container");
const chatRoomId = document.getElementById("current-chat-room-id").value;

// 드래그 앤드롭 이벤트 처리
chatContainer.addEventListener("dragover", (event) => {
    event.preventDefault(); // 기본 동작 방지
});

chatContainer.addEventListener("drop", async (event) => {
    event.preventDefault();

    const files = event.dataTransfer.files; // 드롭된 파일 목록
    if (files.length === 0) return;

    const chatRoomId = document.getElementById("current-chat-room-id").value; // 현재 채팅방 ID 확인
    const formData = new FormData();
    formData.append("file", files[0]); // 첫 번째 파일만 추가

    try {
        const response = await fetch(`/chat/${chatRoomId}/upload`, { // URL 확인
            method: "POST",
            body: formData,
        });

        if (response.ok) {
            const filePath = await response.text(); // 서버에서 반환된 파일 경로
            console.log("File uploaded successfully:", filePath);

            // UI에 이미지 추가
            const chatMessages = document.getElementById("chat-messages");
            const imgMessage = document.createElement("div");
            imgMessage.className = "message-row message-row__own";
            imgMessage.innerHTML = `
                <div class="message-row__content">
                    <img src="/uploads/${filePath}" alt="Uploaded Image" class="message__image">
                </div>
            `;
            chatMessages.appendChild(imgMessage);

            // 스크롤 하단으로 이동
            chatMessages.scrollTop = chatMessages.scrollHeight;
        } else {
            const errorText = await response.text();
            console.error("Error response:", errorText);
            alert("이미지 업로드 실패: " + errorText);
        }
    } catch (error) {
        console.error("Error uploading file:", error);
        alert("파일 업로드 중 오류가 발생했습니다.");
    }
});



