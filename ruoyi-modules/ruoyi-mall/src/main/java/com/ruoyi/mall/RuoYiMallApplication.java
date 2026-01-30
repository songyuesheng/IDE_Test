package com.ruoyi.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class RuoYiMallApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiMallApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  商城模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  ____   ____    __    __   ____   __    ____  ______   .______       _______.  ______\n" +
                " |____| |____|  |  |  |  | |____| |  |  |____| |_____/  |   _  \\     /       | /  __  \\\n" +
                " |      |      |__|  |__| |      |__|  |      |    \\_   |  |_)  |   |   (----`| |  |  |\n" +
                " |      |       __    __  |      __    |      |  |  | \\  |   ___/    \\   \\    | |  |  |\n" +
                " |      |      |  |  |  | |      |  |  |      |  |__|  | |  |   .----)   |   | |__`--'\n" +
                " |      |      |__|  |__| |      |__|  |      |_______/  | _|   |_______/    \\______/\n");
    }
}
