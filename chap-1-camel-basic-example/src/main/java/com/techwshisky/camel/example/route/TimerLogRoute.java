package com.techwshisky.camel.example.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimerLogRoute extends RouteBuilder {

    @Autowired
    private TransformBean transformBean;

    @Autowired
    private ProcessorDemo processorDemo;

    @Override
    public void configure() throws Exception {
        from("timer:myTimer")// basic end point of camel
                .log("${body}") // to print the message body
                .transform().constant("Constant time is : "+LocalDateTime.now())
                .log("${body}")
                .bean(transformBean) // either use transform or bean. You can pass method name as well with comma delimiter
                .log("${body}") //after every transform you can get body using ${body}
                .process(processorDemo) //either create own spirng bean processor or implement camel Processor class
                .log("${body}") // after processor no change in body will be observed
                .to("log:myTimer"); // end point to log the timer
    }
}

@Component
class TransformBean{

    public String getLatestTimePlusTwo(){
        return ""+LocalDateTime.now().plusDays(2);
    }
}

@Component
class TransformBeanMessage{

    public String convertToMilliSeconds(){
        return null;
    }
}

@Component
class ProcessorDemo implements org.apache.camel.Processor {

    private Logger logger= LoggerFactory.getLogger(ProcessorDemo.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("time passed in processor is: "+exchange.getMessage().getBody());
        LocalDateTime localDateTime=LocalDateTime.parse(""+exchange.getMessage().getBody());
        logger.info("current time is: "+localDateTime.minusDays(2));
    }
}
