package com.example.jwt0926.oauth;

import com.example.jwt0926.dto.user.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthRestController {
    private final OauthService oauthService;

    public OauthRestController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    // http://localhost:8080/login/oauth/github?code={authorization_code}
    // 위와 같은 방식으로 postman에 GET 요청을 보내면
    // 해당 사용자의 정보를 받아올 수 있음
    //
    // authorization_code는
    // http://localhost:8080 실행 후 <Github Login> 버튼 누르면 얻을 수 있음
    // url에서 확인 가능함
    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        LoginResponse loginResponse = oauthService.login(provider, code);

        return ResponseEntity.ok(loginResponse);
    }
}
