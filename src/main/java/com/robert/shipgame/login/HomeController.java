package com.robert.shipgame.login;

import com.robert.shipgame.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AccountService accountService;

    @GetMapping("/")
    public String home(final Model model, @AuthenticationPrincipal final OidcUser principal) {
        accountService.createOrUpdateAccountFromPrincipal(principal);

        return "index";
    }

}
