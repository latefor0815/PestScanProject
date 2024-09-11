document.addEventListener("DOMContentLoaded", function() {
    console.log("페이지가 로드되었습니다.");

    const signupForm = document.querySelector('form');

    if (signupForm) {
        signupForm.addEventListener('submit', function(event) {
            const username = document.querySelector('input[name="username"]').value;
            const email = document.querySelector('input[name="email"]').value;
            const password = document.querySelector('input[name="password"]').value;

            if (!username || !email || !password) {
                event.preventDefault();
                alert("모든 필드를 입력해주세요.");
            } else {
                console.log("회원가입 시도: ", { username, email, password });
            }
        });
    }
});
