package com.cheetah.cheetahtechnicaltest;

import com.cheetah.cheetahtechnicaltest.exceptions.UnexpectedException;
import com.cheetah.cheetahtechnicaltest.models.Pair;
import com.cheetah.cheetahtechnicaltest.services.TagPairingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class CheetahTechnicalTestApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(CheetahTechnicalTestApplication.class);

    private static String[] args;

    @Autowired
    private TagPairingService tagPairingService;

    public static void main(String[] args) throws Exception {
        logger.info("STARTING THE APPLICATION");
//        CheetahTechnicalTestApplication.args = args;
        SpringApplication.run(CheetahTechnicalTestApplication.class, args);
        logger.info("APPLICATION FINISHED");
    }

//    @PostConstruct
//    public void init() {
//        try {
//            List<Pair> pairList = tagPairingService.getPairList(args[0]);
//            for (Pair pair : pairList) {
//                logger.info("i = {}, j = {}, result={}", pair.getFirst(), pair.getSecond(), new ObjectMapper().writeValueAsString(pair.getTags()));
//            }
//        } catch (JsonProcessingException e) {
//            logger.error("JsonProcessingException: Error execute print common tag reason = {}", e.getMessage());
//        } catch (UnexpectedException e) {
//            logger.error("UnexpectedException: Error execute print common tag reason = {}", e.getMessage());
//        } catch (Exception e) {
//            logger.error("Exception: Error execute print common tag reason = {}", e.getMessage());
//        }
//    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("EXECUTING : command line runner");
        if (args.length == 0) {
            logger.error("Args cannot be empty. Please pass in file name/location");
            return;
        }
        try {
            List<Pair> pairList = tagPairingService.getPairList(args[0]);
            for (Pair pair : pairList) {
                logger.info("i = {}, j = {}, result={}", pair.getFirst(), pair.getSecond(), new ObjectMapper().writeValueAsString(pair.getTags()));
            }
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException: Error execute print common tag reason = {}", e.getMessage());
        } catch (UnexpectedException e) {
            logger.error("UnexpectedException: Error execute print common tag reason = {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Exception: Error execute print common tag reason = {}", e.getMessage());
        }
    }
}
