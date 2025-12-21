<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực OTP - Fashion Store</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background:   linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .otp-container {
            background:   white;
            max-width: 500px;
            width: 100%;
            border-radius: 20px;
            box-shadow:  0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            text-align:   center;
            animation: slideUp 0.5s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .email-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 20px;
            background:  linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 40px;
            color: white;
        }

        h1 {
            color: #333;
            font-size: 28px;
            margin-bottom:   10px;
        }

        .  subtitle {
            color: #666;
            font-size: 14px;
            margin-bottom:   30px;
        }

        .email-sent {
            background:  #e3f2fd;
            padding: 15px;
            border-radius:   10px;
            margin-bottom:  30px;
            border-left:  4px solid #2196f3;
            text-align: left;
        }

        .email-sent strong {
            color: #1976d2;
        }

        .error-message {
            background: #fee;
            color: #c33;
            padding: 12px;
            border-radius:   8px;
            margin-bottom:   20px;
            border-left: 4px solid #c33;
            font-size: 14px;
            animation: shake 0.5s;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        .info-box {
            background: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            border-radius:  8px;
            margin-bottom: 20px;
            text-align: left;
            font-size: 14px;
        }

        .info-box strong {
            color: #856404;
        }

        .  info-box ul {
            margin:   10px 0 0 20px;
            color: #856404;
        }

        .  info-box li {
            margin:   5px 0;
        }

        .otp-inputs {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-bottom: 30px;
        }

        .otp-inputs input {
            width: 50px;
            height: 60px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .otp-inputs input:focus {
            outline:  none;
            border-color:  #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .otp-inputs input.  error {
            border-color:  #c33;
            animation: shake 0.5s;
        }

        .btn-verify {
            width: 100%;
            padding:   14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
        }

        .btn-verify:hover:  not(: disabled) {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-verify:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .  resend-section {
            margin-top: 20px;
            color: #666;
            font-size: 14px;
        }

        . resend-link {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            cursor: pointer;
        }

        .resend-link:hover {
            text-decoration: underline;
        }

        .resend-link.  disabled {
            color: #ccc;
            cursor: not-allowed;
            pointer-events: none;
        }

        .timer {
            color: #ff5722;
            font-weight:  bold;
        }

        @media (max-width: 768px) {
            .otp-container {
                padding: 30px 20px;
            }

            .otp-inputs input {
                width: 45px;
                height: 55px;
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="otp-container">
        <div class="email-icon">📧</div>
        
        <h1>Xác thực Email</h1>
        <p class="subtitle">Nhập mã OTP 6 chữ số đã được gửi đến email của bạn</p>

        <% if (request.getAttribute("otpSent") != null && (Boolean)request.getAttribute("otpSent")) { %>
            <div class="email-sent">
                ✅ Mã OTP đã được gửi đến:   <strong><%= request.getAttribute("email") %></strong>
            </div>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
                <% 
                    Integer remaining = (Integer) request.getAttribute("remainingAttempts");
                    if (remaining != null) {
                        if (remaining > 0) { 
                %>
                            <br><strong>⚠️ Còn <%= remaining %> lần thử!</strong>
                <%      } else { %>
                            <br><strong>❌ Đã hết lượt thử!  Vui lòng đăng ký lại.</strong>
                <%      }
                    }
                %>
            </div>
        <% } %>

        <div class="info-box">
            <strong>⏰ Lưu ý quan trọng:</strong>
            <ul>
                <li>Mã OTP có hiệu lực trong <strong>10 phút</strong></li>
                <li>Bạn có tối đa <strong>5 lần nhập</strong></li>
                <li>Không chia sẻ mã này với bất kỳ ai</li>
            </ul>
        </div>

        <form method="post" action="${pageContext.request. contextPath}/verify-otp" id="otpForm" novalidate>
            <input type="hidden" name="userId" value="<%= request.getAttribute("userId") %>">
            <input type="hidden" name="otp" id="otpValue">
            
            <div class="otp-inputs">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
                <input type="text" maxlength="1" class="otp-input" autocomplete="off" inputmode="numeric">
            </div>

            <button type="button" class="btn-verify" id="submitBtn">Xác thực</button>
        </form>

        <div class="resend-section">
            Không nhận được mã?   
            <a href="#" class="resend-link disabled" id="resendLink">Gửi lại</a>
            <span id="timerSection">(<span class="timer" id="countdown">60</span>s)</span>
        </div>
    </div>

    <script>
        const inputs = document.querySelectorAll('.otp-input');
        const otpForm = document.getElementById('otpForm');
        const submitBtn = document.getElementById('submitBtn');
        const otpValueInput = document.getElementById('otpValue');
        
        // Auto focus first input
        window.addEventListener('load', () => {
            inputs[0].focus();
        });

        // Input handling
        inputs.forEach((input, index) => {
            input.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
                
                if (e.target.value.length === 1 && index < inputs.length - 1) {
                    inputs[index + 1].focus();
                }
                
                input.classList.remove('error');
            });

            input.addEventListener('keydown', (e) => {
                if (e.key === 'Backspace') {
                    if (e.target.value === '' && index > 0) {
                        inputs[index - 1].focus();
                    }
                    input.classList.remove('error');
                }
            });

            input.addEventListener('paste', (e) => {
                e.preventDefault();
                const pastedData = e.clipboardData.getData('text').replace(/[^0-9]/g, '');
                
                if (pastedData.length === 6) {
                    pastedData. split('').forEach((char, i) => {
                        if (inputs[i]) {
                            inputs[i].value = char;
                            inputs[i].classList.remove('error');
                        }
                    });
                    inputs[5].focus();
                }
            });
        });

        // ✅ CLICK BUTTON TO SUBMIT
        submitBtn.addEventListener('click', function() {
            // Collect OTP
            let otp = '';
            inputs. forEach(input => {
                otp += input.value. trim();
            });
            
            console.log('🔍 OTP collected:', otp);
            
            // Validate
            if (otp.length !== 6) {
                alert('Vui lòng nhập đủ 6 chữ số! ');
                inputs.forEach(input => input.classList.add('error'));
                inputs[0].focus();
                return;
            }
            
            // Set OTP value
            otpValueInput.value = otp;
            
            console.log('🔍 OTP set to hidden input:', otpValueInput.value);
            console.log('🔍 Form will submit now...');
            
            // Disable button
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang xác thực...';
            
            // Submit form
            otpForm.submit();
        });

        // Countdown timer
        let seconds = 60;
        const countdownElement = document.getElementById('countdown');
        const resendLink = document.getElementById('resendLink');
        const timerSection = document.getElementById('timerSection');
        
        const interval = setInterval(() => {
            seconds--;
            countdownElement.textContent = seconds;
            
            if (seconds <= 0) {
                clearInterval(interval);
                resendLink.classList.remove('disabled');
                timerSection.style.display = 'none';
            }
        }, 1000);

        // Resend OTP
        resendLink.addEventListener('click', function(e) {
            e.preventDefault();
            
            if (! this.classList.contains('disabled')) {
                const userId = document. querySelector('input[name="userId"]').value;
                
                this.textContent = 'Đang gửi... ';
                this.classList.add('disabled');
                
                fetch('${pageContext.request.contextPath}/resend-otp', {
                    method: 'POST',
                    headers: {
                        'Content-Type':  'application/x-www-form-urlencoded',
                    },
                    body: 'userId=' + encodeURIComponent(userId)
                })
                .then(response => response. json())
                .then(data => {
                    if (data.success) {
                        alert('✅ Mã OTP mới đã được gửi đến email của bạn!');
                        
                        seconds = 60;
                        timerSection.style.display = 'inline';
                        resendLink.textContent = 'Gửi lại';
                        
                        inputs.forEach(input => {
                            input.value = '';
                            input.classList.remove('error');
                        });
                        inputs[0].focus();
                    } else {
                        alert('❌ ' + data. message);
                        resendLink.textContent = 'Gửi lại';
                        resendLink.classList. remove('disabled');
                    }
                })
                .catch(error => {
                    console. error('Error:', error);
                    alert('❌ Lỗi khi gửi lại OTP.  Vui lòng thử lại!');
                    resendLink.textContent = 'Gửi lại';
                    resendLink.classList. remove('disabled');
                });
            }
        });
    </script>
</body>
</html>