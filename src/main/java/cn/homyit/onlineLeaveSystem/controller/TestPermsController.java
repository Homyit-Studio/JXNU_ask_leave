package cn.homyit.onlineLeaveSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 州牧
 * @description
 * @since 2022-05-03 20:23
 */
@Slf4j
@RequestMapping("/perms")
@RestController
public class TestPermsController {

    @PreAuthorize("hasAuthority('no')")
    @GetMapping("/test")
    public void testPerms(){
        log.info("您拥有访问权限");
    }
}
