package com.karlglans.whattodo.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.karlglans.whattodo.WhattodoApplication.class)
@AutoConfigureMockMvc
abstract class AbstractMockMvcIT  {
}
