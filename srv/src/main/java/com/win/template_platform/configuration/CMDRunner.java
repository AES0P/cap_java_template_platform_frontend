package com.win.template_platform.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.profesorfalken.jpowershell.PowerShell;

@Component
@Order(1)
public class CMDRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        PowerShell.openSession().executeCommand("cds deploy");
    }
}