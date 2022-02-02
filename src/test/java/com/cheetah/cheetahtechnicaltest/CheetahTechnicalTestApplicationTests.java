package com.cheetah.cheetahtechnicaltest;

import com.cheetah.cheetahtechnicaltest.exceptions.UnexpectedException;
import com.cheetah.cheetahtechnicaltest.models.Pair;
import com.cheetah.cheetahtechnicaltest.services.TagPairingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CheetahTechnicalTestApplicationTests extends AbstractJUnit4SpringContextTests {

    private final String TEST_1 = "test_1.json";
    private final String TEST_2 = "test_2.json";
    private final String TEST_3 = "test_3.json";
    private final String TEST_4 = "test_4.json";
    private final String INVALID_FILE_PATH = "123.json";
    private final String EMPTY_FILE_PATH = "empty_file.json";
    private final String EMPTY_BODY_FILE = "empty_body.json";
    private final String INVALID_JSON = "invalid_json.json";
    private final String EXPECTED_RESULT_PATH = "expected_result.json";

    protected List<Pair> expectedResult;

    @Autowired
    TagPairingService tagPairingService;

    @BeforeEach
    void setUp() throws IOException {
        File file = tagPairingService.getFile(readClassPathResourcePath(EXPECTED_RESULT_PATH), true);
        String content = FileUtils.readFileToString(file, "UTF-8");
        if (null == content) {
            logger.error("File is null");
            Assertions.fail();
        }
        if (content.isEmpty()) {
            logger.error("File is empty");
            Assertions.fail();
        }
        expectedResult = new ObjectMapper().readValue(content, List.class);
        Assertions.assertTrue(true);
    }


    @DisplayName("Valid File Path")
    @Test
    void validFilePath() throws Exception {
        File file = tagPairingService.getFile(readClassPathResourcePath(TEST_1), true);
        Assertions.assertNotNull(file);
    }

    @DisplayName("Invalid File Path")
    @Test
    void invalidFilePath() {
        try {
            File file = tagPairingService.getFile(readClassPathResourcePath(INVALID_FILE_PATH), true);
            logger.error("Invalid File Path but still not throwing error when param throwErrorIfNotAvailable set to true");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @DisplayName("Invalid File Path But wont throw error")
    @Test
    void invalidFilePathBuWontThrowError() {
        try {
            File file = tagPairingService.getFile(readClassPathResourcePath(INVALID_FILE_PATH), false);
            Assertions.assertTrue(true);
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            logger.error("Invalid File Path but still throwing error when param throwErrorIfNotAvailable set to false");
            Assertions.fail();
        }
    }


    @DisplayName("Empty File")
    @Test
    void emptyFile() throws Exception {
        File file = tagPairingService.getFile(readClassPathResourcePath(EMPTY_FILE_PATH), true);
        String content = FileUtils.readFileToString(file, "UTF-8");
        if (null == content) {
            logger.error("File is null");
            Assertions.fail();
        }
        if (!content.isEmpty()) {
            logger.error("File is not empty");
            Assertions.fail();
        }
        Assertions.assertTrue(true);
    }


    @DisplayName("Invalid Json")
    @Test
    void invalidJson() throws Exception {
        File file = tagPairingService.getFile(readClassPathResourcePath(INVALID_JSON), true);
        String content = FileUtils.readFileToString(file, "UTF-8");
        if (null == content) {
            logger.error("File is null");
            Assertions.fail();
        }
        if (content.isEmpty()) {
            logger.error("File is empty");
            Assertions.fail();
        }
        try {
            JsonNode jsonNode = new ObjectMapper().readValue(content, JsonNode.class);
            logger.error("File is not json this test expected to fail");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }


    @DisplayName("Empty Body File")
    @Test
    void emptyBodyFile() throws IOException {
        File file = tagPairingService.getFile(readClassPathResourcePath(EMPTY_BODY_FILE), true);
        String content = FileUtils.readFileToString(file, "UTF-8");
        if (null == content) {
            logger.error("File is null");
            Assertions.fail();
        }
        if (content.isEmpty()) {
            logger.error("File is empty");
            Assertions.fail();
        }
        try {
            JsonNode jsonNode = new ObjectMapper().readValue(content, JsonNode.class);
            if (!jsonNode.isEmpty()) {
                logger.error("File is not empty json");
                Assertions.fail();
            }
            Assertions.assertTrue(true);
        } catch (Exception e) {
            logger.error("File is json pass but somehow failed the test.");
            Assertions.fail();
        }
    }

    @DisplayName("Tag Pairing Test 1 Input Generic")
    @Test
    void tagPairingTest1() throws Exception {
        List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(TEST_1));
        List<Pair> filterBasedOnExpectedResult = new ArrayList<>();
        pairList.forEach(pair -> {
            if (!expectedResult.contains(pair)) {
                filterBasedOnExpectedResult.add(pair);
            }
        });
        Assertions.assertEquals(expectedResult.size(), filterBasedOnExpectedResult.size());
    }


    @DisplayName("Tag Pairing Test 2 Input some letters are camel case for name and tags")
    @Test
    void tagPairingTest2() throws Exception {
        List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(TEST_2));
        List<Pair> filterBasedOnExpectedResult = new ArrayList<>();
        pairList.forEach(pair -> {
            if (!expectedResult.contains(pair)) {
                filterBasedOnExpectedResult.add(pair);
            }
        });
        Assertions.assertEquals(expectedResult.size(), filterBasedOnExpectedResult.size());
    }

    @DisplayName("Tag Pairing Test 3 Input some letters are camel case for name, tags and same id")
    @Test
    void tagPairingTest3() throws Exception {
        List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(TEST_3));
        List<Pair> filterBasedOnExpectedResult = new ArrayList<>();
        pairList.forEach(pair -> {
            if (!expectedResult.contains(pair)) {
                filterBasedOnExpectedResult.add(pair);
            }
        });
        Assertions.assertEquals(expectedResult.size(), filterBasedOnExpectedResult.size());
    }

    @DisplayName("Tag Pairing Test 4 Input some letters are camel case for name, tags and same name")
    @Test
    void tagPairingTest4() throws Exception {
        List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(TEST_4));
        List<Pair> filterBasedOnExpectedResult = new ArrayList<>();
        pairList.forEach(pair -> {
            if (!expectedResult.contains(pair)) {
                filterBasedOnExpectedResult.add(pair);
            }
        });
        Assertions.assertEquals(expectedResult.size(), filterBasedOnExpectedResult.size());
    }

    @DisplayName("Tag Pairing Test 5 Input empty file")
    @Test
    void tagPairingTest5() {
        try {
            List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(EMPTY_FILE_PATH));
            logger.error("Empty file expected to throw UnexpectedException");
            Assertions.fail();
        } catch (UnexpectedException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @DisplayName("Tag Pairing Test 6 Input empty body file")
    @Test
    void tagPairingTest6() {
        try {
            List<Pair> pairList = tagPairingService.getPairList(readClassPathResourcePath(EMPTY_BODY_FILE));
            logger.error("Empty body file expected to throw UnexpectedException");
            Assertions.fail();
        } catch (UnexpectedException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    private String readClassPathResourcePath(String filePath) {
        URL resource = getClass().getClassLoader().getResource(filePath);
        try {
            return Paths.get(resource.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
